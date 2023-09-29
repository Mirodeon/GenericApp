package com.mirodeon.genericapp.utils.validator

import java.util.regex.Pattern

class LoginValidator {
    companion object {
        /**
         * Regular expression pattern to match email addresses. It excludes double quoted local parts
         * and the special characters #&~!^`{}/=$*?| that are included in RFC5321.
         * @hide
         */
        private val EMAIL_REGEX: Pattern = android.util.Patterns.EMAIL_ADDRESS

        /**
         * Regular expression pattern to match a password. Must have at least 8 characters
         * containing 1 uppercased character, 1 digit and 1 special character #?!@$%^&*
         * @hide
         */
        private val PASSWORD_REGEX: Pattern =
            Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")

        fun loginValidity(email: String?, password: String?): LoginValidity {
            val isEmailValid = isEmailValid(email)
            val isPasswordValid = isPasswordValid(password)

            return if (isEmailValid && isPasswordValid) {
                LoginValidity.VALID
            } else {
                LoginValidity.ERROR
            }
        }

        fun isEmailValid(email: String?): Boolean {
            return !email.isNullOrEmpty() && EMAIL_REGEX.matcher(email).matches()
        }

        fun isPasswordValid(password: String?): Boolean {
            return !password.isNullOrEmpty() && PASSWORD_REGEX.matcher(password).matches()
        }
    }

    enum class LoginValidity {
        VALID,
        ERROR
    }
}