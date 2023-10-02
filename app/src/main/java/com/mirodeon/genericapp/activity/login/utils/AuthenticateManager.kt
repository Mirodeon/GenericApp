package com.mirodeon.genericapp.activity.login.utils

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mirodeon.genericapp.utils.sharedPref.SharedPrefManager
import com.mirodeon.genericapp.utils.validator.LoginValidator

class AuthenticateManager(
    activity: Activity,
    private val view: View?,
    private val validIcon: Drawable?,
    private val colorValid: Int,
    private val colorInvalid: Int,
    private val checkBoxRememberMe: MaterialCheckBox?,
    private val layoutEmail: TextInputLayout?,
    private val inputEmail: TextInputEditText?,
    private val textEmailError: String,
    private val layoutPassword: TextInputLayout?,
    private val inputPassword: TextInputEditText?,
    private val textPasswordError: String,
    private val loginButton: MaterialButton?,
    val onLoginAction: () -> Unit
) {
    private val sharedPrefManager: SharedPrefManager = SharedPrefManager(activity)

    init {
        initValueForm()

        addAfterTextChanged(
            { LoginValidator.isEmailValid(it) },
            layoutEmail,
            inputEmail,
            textEmailError
        )

        addAfterTextChanged(
            { LoginValidator.isPasswordValid(it) },
            layoutPassword,
            inputPassword,
            textPasswordError
        )

        loginButton?.setOnClickListener {
            authenticateUser()
        }
    }

    private fun initValueForm() {
        val userName =
            sharedPrefManager.sharedPref.getString(SharedPrefManager.KeyPref.LOGIN_KEY.value, "")
        val password =
            sharedPrefManager.sharedPref.getString(SharedPrefManager.KeyPref.PASSWORD_KEY.value, "")
        if (!userName.isNullOrBlank() && !password.isNullOrBlank()) {
            inputEmail?.setText(userName)
            inputPassword?.setText(password)
            checkBoxRememberMe?.isChecked = true

            toggleInputValidity(
                LoginValidator.isEmailValid(userName),
                layoutEmail,
                inputEmail,
                textEmailError
            )

            toggleInputValidity(
                LoginValidator.isPasswordValid(password),
                layoutPassword,
                inputPassword,
                textPasswordError
            )

            toggleButtonValidity()
        }
    }

    private fun toggleButtonValidity() {
        val login = inputEmail?.text?.toString()
        val password = inputPassword?.text?.toString()
        val validity = LoginValidator.loginValidity(login, password)
        loginButton?.isEnabled = validity == LoginValidator.LoginValidity.VALID
    }

    private fun toggleInputValidity(
        isValid: Boolean,
        layout: TextInputLayout?,
        input: TextInputEditText?,
        errorText: String
    ) {
        if (isValid) {
            layout?.isErrorEnabled = false
            input?.setTextColor(colorValid)
            layout?.endIconDrawable = validIcon
            layout?.endIconMode = TextInputLayout.END_ICON_CUSTOM
            layout?.setEndIconTintList(ColorStateList.valueOf(colorValid))
        } else {
            layout?.error = errorText
            layout?.isErrorEnabled = true
            input?.setTextColor(colorInvalid)
            layout?.endIconDrawable = null
        }
    }

    private fun addAfterTextChanged(
        validator: (inputText: String?) -> Boolean,
        layout: TextInputLayout?,
        input: TextInputEditText?,
        errorText: String
    ) {
        input?.doAfterTextChanged {
            val inputText = input.text?.toString()
            toggleButtonValidity()
            toggleInputValidity(validator(inputText), layout, input, errorText)
        }
    }

    private fun setPref(email: String?, password: String?) {
        with(sharedPrefManager.editor) {
            checkBoxRememberMe?.isChecked?.let {
                if (it) {
                    this.putString(SharedPrefManager.KeyPref.LOGIN_KEY.value, email.toString())
                    this.putString(
                        SharedPrefManager.KeyPref.PASSWORD_KEY.value,
                        password.toString()
                    )
                } else {
                    this.remove(SharedPrefManager.KeyPref.LOGIN_KEY.value)
                    this.remove(SharedPrefManager.KeyPref.PASSWORD_KEY.value)
                }
                this.apply()
            }
        }
    }

    private fun authenticateUser() {
        val email = inputEmail?.text?.toString()
        val password = inputPassword?.text?.toString()
        if (LoginValidator.loginValidity(email, password) == LoginValidator.LoginValidity.VALID) {
            setPref(email, password)
            onLoginAction()
        } else {
            view?.let {
                Snackbar.make(
                    it,
                    "Credentials Invalids.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}