package com.dicoding.tugasstoryapp.view.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.tugasstoryapp.data.Models.UserModel
import com.dicoding.tugasstoryapp.data.Models.UserPreference

class WelcomeViewModel (private val prefrerences: UserPreference): ViewModel() {

    fun getUser():LiveData<UserModel>{
        return prefrerences.getUser().asLiveData()
    }

}