package com.mirodeon.genericapp.activity.login

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.mirodeon.genericapp.R
import com.mirodeon.genericapp.activity.login.utils.AuthenticateManager
import com.mirodeon.genericapp.activity.main.MainActivity
import com.mirodeon.genericapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private var binding: ActivityLoginBinding? = null
    private var validIconDrawable: Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        validIconDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.ico_valid_green, theme)

        setContentView(binding?.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        AuthenticateManager(
            this,
            binding?.root,
            ResourcesCompat.getDrawable(resources, R.drawable.ico_valid_green, theme),
            ResourcesCompat.getColor(resources, R.color.green, theme),
            ResourcesCompat.getColor(resources, R.color.black, theme),
            binding?.checkBoxRememberMe,
            binding?.emailInputLayout,
            binding?.emailEditText,
            getString(R.string.email_error_hint),
            binding?.passwordInputLayout,
            binding?.passwordEditText,
            getString(R.string.password_error_hint),
            binding?.connectButton
        ) { displayMainActivity() }

    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun displayMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent).also {
            finish()
        }
    }

}