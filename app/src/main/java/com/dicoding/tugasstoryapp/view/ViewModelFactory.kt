package com.dicoding.tugasstoryapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.tugasstoryapp.data.Models.UserPreference
import com.dicoding.tugasstoryapp.view.login.LoginViewModels
import com.dicoding.tugasstoryapp.view.main.MainViewModel
import com.dicoding.tugasstoryapp.view.welcome.WelcomeViewModel

class ViewModelFactory(private val preference: UserPreference) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(LoginViewModels::class.java) -> {
                LoginViewModels(preference) as T
            }
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(preference) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(preference) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}