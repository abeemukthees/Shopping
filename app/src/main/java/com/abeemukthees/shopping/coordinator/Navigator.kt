package com.abeemukthees.shopping.coordinator

import com.abeemukthees.shopping.HomeActivity
import com.abeemukthees.shopping.base.BaseActivity
import com.abeemukthees.shopping.splash.SplashActivity
import com.abeemukthees.shopping.user.OnBoardingActivity
import com.abeemukthees.shopping.user.SignInActivity
import org.jetbrains.anko.startActivity

class Navigator {

    private var activity: BaseActivity? = null


    fun setBaseActivity(activity: BaseActivity?) {
        this.activity = activity
    }


    fun showSplash() {

        activity?.startActivity<SplashActivity>()
    }

    fun showSignIn() {

        activity?.startActivity<SignInActivity>()

    }

    fun showOnBoarding() {

        activity?.startActivity<OnBoardingActivity>()
    }

    fun showHome() {

        activity?.startActivity<HomeActivity>()

    }
}