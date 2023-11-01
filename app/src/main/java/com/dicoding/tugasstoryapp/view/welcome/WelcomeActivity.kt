package com.dicoding.tugasstoryapp.view.welcome

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.airbnb.lottie.LottieAnimationView
import com.dicoding.tugasstoryapp.Utils.Const
import com.dicoding.tugasstoryapp.data.Models.UserPref
import com.dicoding.tugasstoryapp.data.Models.dataStore
import com.dicoding.tugasstoryapp.databinding.ActivityWelcomeBinding
import com.dicoding.tugasstoryapp.view.ViewModelFactory
import com.dicoding.tugasstoryapp.view.login.LoginActivity
import com.dicoding.tugasstoryapp.view.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//class WelcomeActivity : AppCompatActivity() {
//
//    private
//
//    val binding: ActivityWelcomeBinding by lazy {
//        ActivityWelcomeBinding.inflate(layoutInflater)
//    }
//
//    private val ivLogoApp: LottieAnimationView by lazy {
//        binding.ivLogoApp
//    }
//
//    private val activityScope = CoroutineScope(Dispatchers.Main)
//
//    private val WelcomeViewModel by viewModels<WelcomeViewModel> {
//        ViewModelFactory(
//            UserPref.getInstance(dataStore)
//        )
//    }
//
//    private var isLogin = false
//
//
//    override fun
//        onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        // Play the Lottie animation
//        ivLogoApp.playAnimation()
//
//        // Check if the user is logged in
//        WelcomeViewModel.getUser().observe(this) { model ->
//            isLogin = if (model.isLogin) {
//                UserPref.setToken(model.tokenAuth)
//                true
//            } else {
//                false
//            }
//        }
//
//        // Start the next activity after the animation is finished
//        ivLogoApp.addAnimatorListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator) {
//                super.onAnimationEnd(animation)
//
//                // Start the next activity
//                if (isLogin) {
//                    MainActivity()
//                } else {
//                    LoginActivity()
//                }
//            }
//        })
//    }
//}
class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private val activityScope = CoroutineScope(Dispatchers.Main)

    private val ivLogoApp: LottieAnimationView by lazy {
        binding.ivLogoApp
    }

    private val welcomeViewModel by viewModels<WelcomeViewModel> {
        ViewModelFactory(
            UserPref.getInstance(dataStore)
        )
    }

    private var isLogin = false


    override fun
        onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Play the Lottie animation
        ivLogoApp.playAnimation()

        // Check if the user is logged in
        welcomeViewModel.getUser().observe(this) { model ->
            isLogin = if (model.isLogin) {
                UserPref.setToken(model.tokenAuth)
                true
            } else {
                false
            }
        }

        // Start the next activity after the animation is finished
        ivLogoApp.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun
                    onAnimationEnd(animation: Animator) {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.coroutineContext.cancelChildren()
    }
}