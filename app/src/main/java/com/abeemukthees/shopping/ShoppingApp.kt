package com.abeemukthees.shopping

import android.app.Application
import com.abeemukthees.shopping.coordinator.RootFlowCoordinator
import com.abeemukthees.shopping.di.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.startKoin
import org.koin.log.EmptyLogger

class ShoppingApp : Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin(application = this, modules = listOf(shoppingAppModule, stateMachineModule, repositoryModule, coordinatorModule, useCaseModule, viewModelModule), logger = EmptyLogger())
        get<RootFlowCoordinator>()

    }
}