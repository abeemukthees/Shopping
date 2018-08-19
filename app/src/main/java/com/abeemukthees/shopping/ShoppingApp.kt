package com.abeemukthees.shopping

import android.app.Application
import com.abeemukthees.shopping.di.ShoppingAppModule
import com.abeemukthees.shopping.di.useCaseModule
import com.abeemukthees.shopping.di.viewModelModule
import org.koin.android.ext.android.startKoin
import org.koin.log.EmptyLogger

class ShoppingApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(application = this, modules = listOf(ShoppingAppModule, useCaseModule, viewModelModule), logger = EmptyLogger())

    }
}