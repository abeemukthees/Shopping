package com.abeemukthees.shopping.splash

import android.os.Bundle
import com.abeemukthees.shopping.HomeActivity
import com.abeemukthees.shopping.base.BaseActivity
import org.jetbrains.anko.startActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity<HomeActivity>()
    }
}
