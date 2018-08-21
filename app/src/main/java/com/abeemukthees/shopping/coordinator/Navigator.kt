package com.abeemukthees.shopping.coordinator

import com.abeemukthees.shopping.HomeActivity
import com.abeemukthees.shopping.base.BaseActivity
import com.abeemukthees.shopping.splash.SplashActivity
import com.abeemukthees.shopping.user.SignInActivity
import org.jetbrains.anko.startActivity

class Navigator(private val activity: BaseActivity) {


    fun showSplash() {

        activity.startActivity<SplashActivity>()
    }

    fun showSignIn() {

        activity.startActivity<SignInActivity>()

    }

    fun showHome() {

        activity.startActivity<HomeActivity>()

    }
}