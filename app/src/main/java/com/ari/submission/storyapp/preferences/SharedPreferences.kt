package com.ari.submission.storyapp.preferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences (context: Context){
    val login = "login"
    val myPref = "Main_pref"
    val myToken = "Bearer"
    val sharedPreference: SharedPreferences

    init {
        sharedPreference = context.getSharedPreferences(myPref, Context.MODE_PRIVATE)
    }

    fun setStatusLogin(status: Boolean){
        sharedPreference.edit().putBoolean(login, status).apply()
    }

    fun getStatusLogin(): Boolean{
        return sharedPreference.getBoolean(login,false)
    }

    fun saveUserToken(token: String){
        sharedPreference.edit().putString(myToken,token).apply()
    }

    fun getUserToken(): String? {
        return sharedPreference.getString(myToken," ")
    }

    fun clearUserToken(){
        sharedPreference.edit().remove(myToken).apply()
    }

    fun clearUserLogin(){
        sharedPreference.edit().remove(login).apply()
    }



}