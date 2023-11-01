package com.dicoding.tugasstoryapp.Response

import com.google.gson.annotations.SerializedName

data class GetStoriesResponse(

    @field:SerializedName("listStory")
    val listStory: List<StoryItem>? = null,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String? = null
)