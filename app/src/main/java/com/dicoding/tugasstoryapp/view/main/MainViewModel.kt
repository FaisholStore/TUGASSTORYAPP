package com.dicoding.tugasstoryapp.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugasstoryapp.Response.GetStoriesResponse
import com.dicoding.tugasstoryapp.Response.StoryItem
import com.dicoding.tugasstoryapp.data.Api.ApiConfig
import com.dicoding.tugasstoryapp.data.Models.UserPref
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel (private val preference: UserPref): ViewModel()  {
    private val _listStory = MutableLiveData<List<StoryItem>>()
    val listStory: LiveData<List<StoryItem>> = _listStory

    private val _loadingScreen = MutableLiveData<Boolean>()
    val loadingScreen: LiveData<Boolean> = _loadingScreen

    fun getStories() {
        _loadingScreen.value = true
        val cilent = ApiConfig.getApiService().getStories()
        cilent.enqueue(object : Callback<GetStoriesResponse> {
            override fun onResponse(
                call: Call<GetStoriesResponse>,
                response: Response<GetStoriesResponse>
            ) {
                _loadingScreen.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _listStory.value = responseBody.listStory ?: emptyList()
                        Log.d(TAG, responseBody.message.toString())
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
                _loadingScreen.value = false
                Log.e(TAG, "onFailure2: Gagal")
            }
        })
    }

    fun logout() {
        viewModelScope.launch {
            preference.Logout()
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}