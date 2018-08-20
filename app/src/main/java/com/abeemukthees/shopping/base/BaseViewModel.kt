package com.abeemukthees.shopping.base

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.abeemukthees.domain.base.Action
import com.abeemukthees.domain.base.BaseStateMachine
import com.abeemukthees.domain.base.State
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

abstract class BaseViewModel(stateMachine: BaseStateMachine) : ViewModel() {

    protected val TAG = this.javaClass.simpleName

    private val inputRelay: Relay<Action> = PublishRelay.create()
    private val mutableState = MutableLiveData<State>()

    val input: Consumer<Action> = inputRelay
    val state: LiveData<State> = mutableState

    private val compositeDisposable = CompositeDisposable()

    init {

        //Log.d(TAG,"Init... $TAG")

        addDisposable(inputRelay.subscribe(stateMachine.input))
        addDisposable(stateMachine.state.subscribe { state -> mutableState.value = state })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        //Log.d(TAG,"onCleared")
    }

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }
}