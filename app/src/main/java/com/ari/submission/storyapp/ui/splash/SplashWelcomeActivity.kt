package com.ari.submission.storyapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ari.submission.storyapp.databinding.ActivitySplashWelcomeBinding
import com.ari.submission.storyapp.preferences.SharedPreferences
import com.ari.submission.storyapp.ui.home.MainActivity
import com.ari.submission.storyapp.ui.login.LoginActivity
import com.ari.submission.storyapp.ui.register.SignUpActivity

class SplashWelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashWelcomeBinding

    private lateinit var sph: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sph = SharedPreferences(this)
        if (sph.getStatusLogin()){
            startActivity(Intent(this@SplashWelcomeActivity, MainActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val login = Intent(this@SplashWelcomeActivity, LoginActivity::class.java)
            startActivity(login)
        }

        binding.btnRegister.setOnClickListener {
            val register = Intent(this@SplashWelcomeActivity, SignUpActivity::class.java)
            startActivity(register)
        }
    }
}