package com.abeemukthees.shopping

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.abeemukthees.domain.base.Action
import com.abeemukthees.domain.base.State
import com.abeemukthees.domain.statemachine.HomeStateMachine
import com.abeemukthees.shopping.base.BaseViewModel
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.functions.Consumer

class HomeViewModel(homeStateMachine: HomeStateMachine) : BaseViewModel() {

    private val inputRelay: Relay<Action> = PublishRelay.create()
    private val mutableState = MutableLiveData<com.abeemukthees.domain.base.State>()

    val input: Consumer<Action> = inputRelay
    val state: LiveData<State> = mutableState

    init {

        addDisposable(inputRelay.subscribe(homeStateMachine.input))
        addDisposable(homeStateMachine.state.subscribe { state -> mutableState.value = state })

    }
}