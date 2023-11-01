package com.dicoding.tugasstoryapp.view.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.dicoding.tugasstoryapp.R
import com.dicoding.tugasstoryapp.databinding.ActivityRegisterBinding
import com.dicoding.tugasstoryapp.view.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisViewModels>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        with(binding) {
            btnSignIn.setOnClickListener {
                LoginActivity.start(this@RegisterActivity)
            }

            btnSignUp.setOnClickListener {
                val name = EdtRegisterName.text.toString()
                val email = EdtRegisterEmail.text.toString()
                val password = EdtRegisterPassword.text.toString()
                when {
                    name.isEmpty() -> {
                        EdtRegisterName.error = "Nama tidak boleh kosong"
                        EdtRegisterName.requestFocus()
                    }
                    email.isEmpty() -> {
                        EdtRegisterEmail.error = "Email tidak boleh kosong"
                        EdtRegisterEmail.requestFocus()
                    }
                    password.isEmpty() -> {
                        EdtRegisterPassword.error = "Passoword tidak boleh kosong"
                        EdtRegisterPassword.requestFocus()
                    }
                    else -> {
                        registerViewModel.register(name, email, password)
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        registerViewModel.register.observe(this) { isSuccess ->
            if (isSuccess){
                val dialogBuilder = AlertDialog.Builder(this)
                    .setTitle("Registrasi Berhasil")
                    .setMessage("Akun anda berhasil didaftar. Silahkan Login Terlebih dahulu untuk mengakses daftar story")
                    .setPositiveButton("Oke") { _,_ ->
                        LoginActivity.start(this)
                        finish()
                    }
                    .setOnDismissListener {
                        LoginActivity.start(this)
                        finish()
                    }
                val dialog = dialogBuilder.create()
                dialog.show()
            }
        }
        registerViewModel.snackbarText.observe(this) { text ->
            when {
                text.contains("taken") -> {
                    binding.EdtRegisterEmail.error = getString(R.string.email_created)
                    binding.EdtRegisterEmail.requestFocus()
                }
                text.contains("created") -> {
                    // Do Nothing or perform actions if needed
                }
                text.contains("must be a valid email") -> {
                    binding.EdtRegisterEmail.error = getString(R.string.Email_protect)
                    binding.EdtRegisterEmail.requestFocus()
                }
                text.contains("Password must be at least 6 characters long") -> {
                    binding.EdtRegisterPassword.error = getString(R.string.invalid_password)
                    binding.EdtRegisterPassword.requestFocus()
                }
                else -> Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
            }
        }

        registerViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }
    private fun showLoading(value: Boolean) {
        binding.btnSignIn.isEnabled = !value  // Menonaktifkan tombol saat sedang loading
        binding.btnSignUp.isInvisible = value // Menyembunyikan tombol SignUp saat sedang loading
        binding.pbLoadingScreen.isVisible = value // Menampilkan ProgressBar saat sedang loading
    }


    private fun setupView() {
        binding.EdtRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                val passwordLength = password.length
                val hasUpperCase = password.any { it.isUpperCase() }
                val hasLowerCase = password.any { it.isLowerCase() }
                val hasDigit = password.any { it.isDigit() }
                val hasSpecialChar = password.any { !it.isLetterOrDigit() }

                val isPasswordValid = passwordLength >= 8 &&
                        hasUpperCase &&
                        hasLowerCase &&
                        hasDigit &&
                        hasSpecialChar

                binding.btnSignUp.isEnabled = isPasswordValid
            }

        })
    }



    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, RegisterActivity::class.java)
            context.startActivity(starter)
        }
    }
}