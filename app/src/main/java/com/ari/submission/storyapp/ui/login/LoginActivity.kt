package com.ari.submission.storyapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ari.submission.storyapp.background.ApiConfig
import com.ari.submission.storyapp.background.LoginResponse
import com.ari.submission.storyapp.databinding.ActivityLoginBinding
import com.ari.submission.storyapp.ui.home.MainActivity
import com.ari.submission.storyapp.ui.register.SignUpActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sph: com.ari.submission.storyapp.preferences.SharedPreferences

    companion object{
        private const val TAG = "LoginActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sph = com.ari.submission.storyapp.preferences.SharedPreferences(this)
        if (sph.getStatusLogin()){
            val main = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(main)
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val register = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(register)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when{
                email.isEmpty()->{
                    binding.edLoginEmail.error = "Email is Required"
                }
                password.isEmpty()->{
                    binding.edLoginPassword.error = "Password is Required"
                }
                else->{
                        login(email,password)
                }
            }
        }
    }

    private fun login(email: String, password: String){
        val client = ApiConfig().getApiService().login(email,password)
        client.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody!=null && !responseBody.error){
                        sph.saveUserToken(responseBody.loginResult.token)
                        sph.setStatusLogin(true)
                        val main = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(main)
                        finishAffinity()
                    }else{
                        Log.e(TAG, "onResponse: Gagal" + response.message())
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}