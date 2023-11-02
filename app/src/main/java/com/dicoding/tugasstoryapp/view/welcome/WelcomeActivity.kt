package com.dicoding.tugasstoryapp.view.welcome
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.airbnb.lottie.LottieAnimationView
import com.dicoding.tugasstoryapp.Utils.Const
import com.dicoding.tugasstoryapp.data.Models.UserPreference
import com.dicoding.tugasstoryapp.data.Models.dataStore
import kotlinx.coroutines.*
import com.dicoding.tugasstoryapp.databinding.ActivityWelcomeBinding
import com.dicoding.tugasstoryapp.view.ViewModelFactory
import com.dicoding.tugasstoryapp.view.login.LoginActivity
import com.dicoding.tugasstoryapp.view.main.MainActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private val activityScope = CoroutineScope(Dispatchers.Main)
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var ivLogoApp: LottieAnimationView
    private var isLogin = false

    private val welcomeViewModel by viewModels<WelcomeViewModel> {
        ViewModelFactory(
            UserPreference.getInstance(dataStore)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connectivityManager = getSystemService(ConnectivityManager::class.java)
        ivLogoApp = binding.ivLogoApp
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Check internet connectivity
        if (isNetworkAvailable()) {
            // Play the Lottie animation
            ivLogoApp.playAnimation()


            // Check if the user is logged in
            welcomeViewModel.getUser().observe(this) { model ->
                isLogin = if (model.isLogin) {
                    UserPreference.setToken(model.tokenAuth)
                    true
                } else {
                    false
                }
            }

            // Start the next activity after the animation is finished
            ivLogoApp.addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    activityScope.launch {
                        delay(Const.DELAY_SPLASH_SCREEN)
                        runOnUiThread {
                            if (isLogin) {
                                MainActivity.start(this@WelcomeActivity)
                            } else {
                                LoginActivity.start(this@WelcomeActivity)
                            }
                            finish()
                        }
                    }
                }
            })
        } else {
            showNoInternetDialog()
        }
    }

    private fun showNoInternetDialog() {
        AlertDialog.Builder(this)
            .setTitle("Koneksi Terputus")
            .setMessage("Tidak ada koneksi internet. Silakan cek koneksi Anda.")
            .setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


    override fun onDestroy() {
        super.onDestroy()
        activityScope.coroutineContext.cancelChildren()
    }
}
