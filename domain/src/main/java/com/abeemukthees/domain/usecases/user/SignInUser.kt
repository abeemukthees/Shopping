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

    override fun buildUseCaseObservable(action: Action, state: State): Observable<Action> {
        //println("signInUser buildUseCaseObservable  ${action.share().blockingFirst()::class.simpleName}, ${state::class.simpleName}")
        return if (action is UserAction.UserSignInAction) userRepository.signInUser(Params(action.username, action.password))
                .map {
                    return@map if (it.first) UserAction.UserSignedInSuccessfullyAction
                    else UserAction.ErrorSigningInUserAction(it.second
                            ?: Throwable("Error signing in"))
                }
        else Observable.just(UserAction.ErrorSigningInUserAction(Throwable("Error signing in user")))
    }


    //val listOfSideEffects = listOf(::validateUserCredentials, ::signInUser)

    fun signInUser(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
        return actions
                //.doOnNext { println("signInUser ${it::class.simpleName}, ${state()::class.simpleName}") }
                .ofType(UserAction.UserSignInAction::class.java).switchMap { execute(it, state()).startWith(UserAction.SigningInUserAction) }
    }


    fun validateUserCredentials(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> =
            actions.ofType(UserAction.ValidateUserCredentialsAction::class.java)
                    .map { receivedAction ->
                        println("username = ${receivedAction.username}, password = ${receivedAction.password}")

                        val isUsernameValid = receivedAction.username.length > 6
                        val isPasswordValid = receivedAction.password.length > 6

                        val usernameErrorMsg: String? = if (isUsernameValid) null else "Invalid username"
                        val passwordErrorMsg: String? = if (isPasswordValid) null else "Invalid password"

                        (UserAction.UserCredentialsValidatedAction(username = Pair(isUsernameValid, usernameErrorMsg), password = Pair(isPasswordValid, passwordErrorMsg)) as Action)
                    }.startWith(UserAction.ValidatingUserCredentialsAction)

    data class Params(val username: String, val password: String)
}