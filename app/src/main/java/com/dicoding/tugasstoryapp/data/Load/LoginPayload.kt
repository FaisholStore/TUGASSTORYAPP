package com.dicoding.tugasstoryapp.data.Load

import com.google.gson.annotations.SerializedName

data class LoginPayload(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)
