package com.abeemukthees.domain.base

import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

interface BaseStateMachine {

    val input: Relay<Action>

    val state: Observable<State>

    fun reducer(state: State, action: Action): State
}