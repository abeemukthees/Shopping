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
        return action.ofType(UserAction.UserSignInAction::class.java)
                .switchMap { userRepository.signInUser(Params(it.username, it.password)) }
                .map {

                    return@map if (it.first) UserAction.UserSignedInSuccessfullyAction
                    else UserAction.ErrorSigningInUserAction(it.second
                            ?: Throwable("Error signing in"))
                }
    }


    //val listOfSideEffects = listOf(::validateUserCredentials, ::signInUser)

    fun signInUser(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> = execute(actions, state()).startWith(UserAction.SigningInUserAction)


    fun validateUserCredentials(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> =
            actions.ofType(UserAction.ValidateUserCredentialsAction::class.java).map<Action> {


        println("username = ${it.username}, password = ${it.password}")


        val isUsernameValid = it.username.length > 6
        val isPasswordValid = it.password.length > 6

        val usernameErrorMsg: String? = if (isUsernameValid) null else "Invalid username"
        val passwordErrorMsg: String? = if (isPasswordValid) null else "Invalid password"

        UserAction.UserCredentialsValidatedAction(username = Pair(isUsernameValid, usernameErrorMsg), password = Pair(isPasswordValid, passwordErrorMsg))

            }.startWith(UserAction.ValidatingUserCredentialsAction)

    data class Params(val username: String, val password: String)
}