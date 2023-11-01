package com.dicoding.tugasstoryapp.view.story

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.dicoding.tugasstoryapp.R
import com.dicoding.tugasstoryapp.Utils.createCustomTempFile
import com.dicoding.tugasstoryapp.Utils.rotateImage
import com.dicoding.tugasstoryapp.Utils.uriToFile
import com.dicoding.tugasstoryapp.databinding.ActivityAddStoryBinding
import com.dicoding.tugasstoryapp.view.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream


class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding

    private lateinit var currentPhoto: String
    private val addStoryViewModels by viewModels<AddStoryViewModels>()
    private var getFile: File? = null

    private var launcherActivityGalerry = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                addStoryViewModels.setFile(myFile)
            }
        }
    }
    private val launcherActivityCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val file = File(currentPhoto)
            file.let { file ->
                val bitmap = BitmapFactory.decodeFile(file.path)
                rotateImage(bitmap, currentPhoto).compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    FileOutputStream(file)
                )
                addStoryViewModels.setFile(file)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Permission()) {
            ActivityCompat.requestPermissions(
                this@AddStoryActivity,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        setupAction()
        setupViewModels()
    }

    private fun setupAction() {
        with(binding) {
            btnCamera.setOnClickListener { startTakePhoto() }
            btnGallery.setOnClickListener { openGallery() }
            btnUpload.setOnClickListener {
                edAddDescription.clearFocus()
                uploadImage()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherActivityGalerry.launch(chooser)
    }

    private fun uploadImage() {
       when{
           getFile == null -> Snackbar.make(binding.root, getString(R.string.please_input_image), Snackbar.LENGTH_SHORT).show()
           binding.edAddDescription.text.isNullOrBlank() -> {
               binding.edAddDescription.error = getString(R.string.erordeskripsi)
               binding.edAddDescription.requestFocus()
           }
           else -> {
               val file = getFile as File
               val description = binding.edAddDescription.text.toString()
               addStoryViewModels.postingStory(file, description)
           }
       }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoUri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.dicoding.storyapp",
                it
            )
            currentPhoto = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherActivityCamera.launch(intent)
        }
    }

    private fun setupViewModels() {
        addStoryViewModels.isPosted.observe(this) { isSuccess ->
            if (isSuccess) {
                val dialogBuilder = AlertDialog.Builder(this)
                    .setTitle("Berhasil")
                    .setMessage("Story app anda berhasil di upload")
                    .setPositiveButton("OK") { _, _ ->
                        MainActivity.start(this)
                        finish()
                    }
                    .setOnDismissListener {
                        MainActivity.start(this)
                        finish()
                    }
                val dialog = dialogBuilder.create()
                dialog.show()
            } else {
                Snackbar.make(binding.root, "Gagal Diupload", Snackbar.LENGTH_SHORT).show()
            }
        }
        addStoryViewModels.hasUploaded.observe(this) { file ->
            if (file != null) {
                Log.d(TAG, "true - with file")
                getFile = file
                binding.ivThumbnail.setImageBitmap(BitmapFactory.decodeFile(file.path))
                binding.ivThumbnail.setPadding(0)
            } else {
                binding.ivThumbnail.setImageResource(R.drawable.baseline_image_24)
                binding.ivThumbnail.setPadding(32)
                Log.d(TAG, "true - without file")
            }
        }

        addStoryViewModels.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(value: Boolean) {
        binding.progressIndicator.isVisible = value
        binding.btnUpload.isInvisible = value
        binding.btnGallery.isEnabled = !value
        binding.btnCamera.isEnabled = !value
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!Permission()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun Permission() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "AddStoryActivity"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, AddStoryActivity::class.java)
            context.startActivity(starter)
        }
    }
}