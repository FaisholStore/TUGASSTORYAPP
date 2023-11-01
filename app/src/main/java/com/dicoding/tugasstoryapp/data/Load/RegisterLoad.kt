package com.dicoding.tugasstoryapp.data.Load

import com.google.gson.annotations.SerializedName

data class RegisterLoad(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)
