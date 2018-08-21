package com.abeemukthees.shopping

import android.util.Log
import com.abeemukthees.domain.base.NavigateAction
import com.abeemukthees.domain.statemachine.home.HomeStateMachine
import com.abeemukthees.shopping.base.BaseViewModel
import com.abeemukthees.shopping.coordinator.NavigateToOnBoarding

class HomeViewModel(homeStateMachine: HomeStateMachine, navigationCallback: ((NavigateAction) -> Unit)?) : BaseViewModel(homeStateMachine, navigationCallback) {


    init {

        addDisposable(inputRelay.share().subscribe {

            Log.d(TAG, "InputRelay = ${it::class.simpleName}")
            if (it is NavigateToOnBoarding) navigationCallback?.invoke(it)
        })
    }
}