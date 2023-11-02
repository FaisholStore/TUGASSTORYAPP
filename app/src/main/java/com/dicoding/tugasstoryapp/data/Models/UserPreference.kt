package com.dicoding.tugasstoryapp.data.Models

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.tugasstoryapp.Utils.Const
import com.dicoding.tugasstoryapp.data.Api.ApiConfig

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {


    // getuser
    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preference ->
            UserModel(
                preference[AUTH_KEY] ?: "", preference[STATE_KEY] ?: false

            )
        }
    }

    suspend fun SaveUser(user: UserModel) {
        dataStore.edit { preference ->
            preference[AUTH_KEY] = user.tokenAuth
            preference[STATE_KEY] = user.isLogin
        }
    }

    suspend fun Logout() {
        dataStore.edit { preference ->
            preference[AUTH_KEY] = ""
            preference[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val AUTH_KEY = stringPreferencesKey(Const.KEY_AUTH_PREFERENCES)
        private val STATE_KEY = booleanPreferencesKey(Const.KEY_STATE_PREFERENCES)

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
        //set token
        fun setToken(token: String) {
            ApiConfig.setToken(token)
        }

    }

}