package com.abeemukthees.domain.usecases.user

import com.abeemukthees.domain.base.Action
import com.abeemukthees.domain.base.State
import com.abeemukthees.domain.executor.PostExecutionThread
import com.abeemukthees.domain.executor.ThreadExecutor
import com.abeemukthees.domain.interactor.ObservableUseCase
import com.abeemukthees.domain.repository.UserRepository
import com.abeemukthees.domain.statemachine.user.UserAction
import com.freeletics.rxredux.StateAccessor
import io.reactivex.Observable

class SignInUser(private val userRepository: UserRepository, threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread) : ObservableUseCase(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(action: Observable<Action>, state: State): Observable<Action> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    val listOfSideEffects = listOf(::validateUserCredentials)

    private fun validateUserCredentials(
            actions: Observable<Action>,
            state: StateAccessor<State>
    ): Observable<Action> = actions.ofType(UserAction.ValidateUserCredentialsAction::class.java).map {


        println("username = ${it.username}, password = ${it.password}")


        val isUsernameValid = it.username.length > 6
        val isPasswordValid = it.password.length > 6

        val usernameErrorMsg: String? = if (isUsernameValid) null else "Invalid username"
        val passwordErrorMsg: String? = if (isPasswordValid) null else "Invalid password"

        UserAction.UserCredentialsValidatedAction(username = Pair(isUsernameValid, usernameErrorMsg), password = Pair(isPasswordValid, passwordErrorMsg))

    }
}