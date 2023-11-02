package com.dicoding.tugasstoryapp.view.Detail


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.tugasstoryapp.Response.DetailStoryResponse
import com.dicoding.tugasstoryapp.Response.StoryItem
import com.dicoding.tugasstoryapp.Utils.Event
import com.dicoding.tugasstoryapp.data.Api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailView : ViewModel() {
    //membuat variabel detail
    private val _detailstorie = MutableLiveData<StoryItem>()
    val detailStory: LiveData<StoryItem> = _detailstorie

    private val _loading = MutableLiveData<Boolean>()
    val loadingScreen: LiveData<Boolean> = _loading

    //untuk menghasilkan string
    private val _snackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = _snackBarText


    //membuat reques ke api menggunakan callback

    fun getDetailStory(userId: String) {
        _loading.value = true

        val call = ApiConfig.getApiService().getDetailStory(userId)
        call.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error!!) {
                        _detailstorie.value = responseBody. story ?: StoryItem()
                        Log.d(TAG, responseBody.message.toString())
                    } else {
                        _snackBarText.value = Event(response.message())
                        Log.e(TAG, "gagalData:${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _loading.value = false
                _snackBarText.value = Event("gagalData: Gagal, ${t.message ?: ""}")
                Log.e(TAG, "gagalData: Gagal")
            }

        })
    }

    companion object {
        private const val TAG = "DetailView"
    }

}