package com.abeemukthees.shopping.di

import com.abeemukthees.data.DataStoreFactory
import com.abeemukthees.data.ProductRepositoryImpl
import com.abeemukthees.data.executor.JobExecutor
import com.abeemukthees.data.user.UserRepositoryImpl
import com.abeemukthees.domain.executor.PostExecutionThread
import com.abeemukthees.domain.executor.ThreadExecutor
import com.abeemukthees.domain.repository.ProductRepository
import com.abeemukthees.domain.repository.UserRepository
import com.abeemukthees.domain.statemachine.home.HomeStateMachine
import com.abeemukthees.domain.statemachine.user.UserStateMachine
import com.abeemukthees.domain.usecases.product.GetProducts
import com.abeemukthees.domain.usecases.user.GetUser
import com.abeemukthees.domain.usecases.user.GetUserLoginStatus
import com.abeemukthees.domain.usecases.user.SignInUser
import com.abeemukthees.domain.usecases.user.SignOutUser
import com.abeemukthees.shopping.HomeViewModel
import com.abeemukthees.shopping.UiThread
import com.abeemukthees.shopping.coordinator.HomeFlowCoordinator
import com.abeemukthees.shopping.coordinator.Navigator
import com.abeemukthees.shopping.coordinator.RootFlowCoordinator
import com.abeemukthees.shopping.coordinator.SignInFlowCoordinator
import com.abeemukthees.shopping.user.UserViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val shoppingAppModule = applicationContext {

    bean { JobExecutor() as ThreadExecutor }
    bean { UiThread() as PostExecutionThread }
}

val stateMachineModule = applicationContext {

    factory { HomeStateMachine(get()) }
    factory { UserStateMachine(get(), get(), get(), get()) }
}

val repositoryModule = applicationContext {

    bean { DataStoreFactory(get()) }
    bean { ProductRepositoryImpl(get()) as ProductRepository }
    bean { UserRepositoryImpl(get()) as UserRepository }
}

val coordinatorModule = applicationContext {

    bean { Navigator() }
    bean { RootFlowCoordinator(get(), get(), get()) }
    bean { SignInFlowCoordinator(get()) }
    bean { HomeFlowCoordinator(get()) }
}

val useCaseModule = applicationContext {

    factory { GetProducts(get(), get(), get()) }
    factory { GetUserLoginStatus(get(), get(), get()) }
    factory { GetUser(get(), get(), get()) }
    factory { SignInUser(get(), get(), get()) }
    factory { SignOutUser(get(), get(), get()) }
}


val viewModelModule = applicationContext {

    viewModel { HomeViewModel(get(), (get() as SignInFlowCoordinator)::navigate) }
    viewModel { UserViewModel(get(), get(), null) }
}
