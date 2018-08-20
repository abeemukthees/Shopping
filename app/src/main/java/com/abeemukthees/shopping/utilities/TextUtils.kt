package com.abeemukthees.shopping.utilities

import java.util.regex.Matcher
import java.util.regex.Pattern

object TextUtils {

    fun isUserNameValid(userName: CharSequence): Boolean {
        return isEmailValid(userName) || userName.length >= 10 && android.util.Patterns.PHONE.matcher(userName).matches()
    }

    fun isEmailValid(email: CharSequence): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isPhoneNumberValid(phoneNumber: CharSequence): Boolean {
        return phoneNumber.length >= 10 && android.util.Patterns.PHONE.matcher(phoneNumber).matches()
    }

    fun trimEnd(charSequence: CharSequence): String {
        return charSequence.toString().replace("\\s+$".toRegex(), "")
    }
}
