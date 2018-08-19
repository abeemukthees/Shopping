package com.abeemukthees.shopping.di

import com.abeemukthees.data.DataStoreFactory
import com.abeemukthees.data.ProductRepositoryImpl
import com.abeemukthees.data.executor.JobExecutor
import com.abeemukthees.domain.executor.PostExecutionThread
import com.abeemukthees.domain.executor.ThreadExecutor
import com.abeemukthees.domain.repository.ProductRepository
import com.abeemukthees.domain.statemachine.HomeStateMachine
import com.abeemukthees.domain.usecases.GetProducts
import com.abeemukthees.shopping.HomeViewModel
import com.abeemukthees.shopping.UiThread
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

val ShoppingAppModule: Module = applicationContext {

    bean { DataStoreFactory(get()) }
    bean { ProductRepositoryImpl(get()) as ProductRepository }
    bean { JobExecutor() as ThreadExecutor }
    bean { UiThread() as PostExecutionThread }
    bean { HomeStateMachine(get()) }
}

val useCaseModule = applicationContext {

    factory { GetProducts(get(), get(), get()) }

}


val viewModelModule = applicationContext {

    viewModel { HomeViewModel(get()) }

}
