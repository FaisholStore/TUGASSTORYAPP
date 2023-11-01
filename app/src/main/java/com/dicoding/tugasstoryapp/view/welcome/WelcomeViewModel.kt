package com.dicoding.tugasstoryapp.view.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.tugasstoryapp.data.Models.UserModel
import com.dicoding.tugasstoryapp.data.Models.UserPref

class WelcomeViewModel (private val prefrerences: UserPref): ViewModel() {

    fun getUser():LiveData<UserModel>{
        return prefrerences.getUser().asLiveData()
    }

}