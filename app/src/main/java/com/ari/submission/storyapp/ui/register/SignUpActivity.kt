package com.ari.submission.storyapp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ari.submission.storyapp.background.ApiConfig
import com.ari.submission.storyapp.background.SignupResponse
import com.ari.submission.storyapp.databinding.ActivitySignUpBinding
import com.ari.submission.storyapp.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    companion object{
        private const val TAG ="SignUpActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //button signup
        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            when{
                name.isEmpty()->{
                    binding.edRegisterName.error = "Name is required"
                }
                email.isEmpty()->{
                    binding.edRegisterEmail.error = "Email is required"
                }
                password.isEmpty()->{
                    binding.edRegisterPassword.error = "Password is required"
                }else->{
                    register(name,email,password)
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun register(name: String, email: String, password: String){
        val client = ApiConfig().getApiService().register(name,email,password)
        client.enqueue(object: Callback<SignupResponse>{
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody!=null && !responseBody.error){
                        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Log.e(TAG, "onResponse: Gagal " + response.message())
                    }
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

}