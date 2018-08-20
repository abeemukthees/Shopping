package com.abeemukthees.domain.statemachine.user

import com.abeemukthees.domain.base.Action
import com.abeemukthees.domain.base.BaseStateMachine
import com.abeemukthees.domain.base.State
import com.abeemukthees.domain.usecases.user.GetUser
import com.abeemukthees.domain.usecases.user.GetUserLoginStatus
import com.abeemukthees.domain.usecases.user.SignInUser
import com.abeemukthees.domain.usecases.user.SignOutUser
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable


sealed class UserAction : Action {

    object CheckSignInStatusAction : UserAction()

    object StartCheckingUserSignInStatusAction : UserAction()

    object UserSignedInAction : UserAction()

    object UserNotSignedInAction : UserAction()

    data class ValidateUserCredentialsAction(val username: String, val password: String) : UserAction()

    object ValidatingUserCredentialsAction : UserAction()

    data class UserCredentialsValidatedAction(val username: Pair<Boolean, String?>, val password: Pair<Boolean, String?>) : UserAction()

    //object UserCredentialsValidAction : UserAction()

    //object UserCredentialsInValidAction : UserAction()

    data class UserSignInAction(val username: String, val password: String) : UserAction()

    object UserSignedInSuccessfullyAction : UserAction()

    object SigningInUserAction : UserAction()

    data class ErrorSigningInUserAction(val error: Throwable) : UserAction()
}

sealed class UserState : State {

    object InitialUserState : UserState()

    object CheckingUserSignInStatus : UserState()

    //data class UserSignInStatusLoadedState(val isUserSignedIn: Boolean) : UserState()

    object ValidatingUserCredentialsState : UserState()

    data class UserCredentialsValidationState(val username: Pair<Boolean, String?>, val password: Pair<Boolean, String?>) : UserState()

    object SigningInUserState : UserState()

    object UserSignedInSuccessfullyState : UserState()

    data class ErrorSigningInUserState(val error: Throwable) : UserState()

    data class ErrorLoadingUserSignInState(val error: Throwable) : UserState()

    data class ErrorLoadingUserState(val error: Throwable) : UserState()

    object UserSignedInState : UserState()

    object UserSignedOutState : UserState()

}


class UserStateMachine(getUserLoginStatus: GetUserLoginStatus, getUser: GetUser, signInUser: SignInUser, signOutUser: SignOutUser) : BaseStateMachine {

    init {
        println("Init... UserStateMachine")
    }

    override val input: Relay<Action> = PublishRelay.create()

    override val state: Observable<State> = input
            .doOnNext { println("Input Action ${it.javaClass.simpleName}") }
            .reduxStore(
                    initialState = UserState.InitialUserState,
                    sideEffects = listOf(getUserLoginStatus::checkUserSignInStatus, signInUser::validateUserCredentials, signInUser::signInUser),
                    reducer = ::reducer)
            .distinctUntilChanged()
            .doOnNext { println("RxStore state ${it.javaClass.simpleName}") }


    override fun reducer(state: State, action: Action): State {
        println("Reducing state =  ${state::class.simpleName}, action = ${action::class.simpleName}")
        return when (action) {

            is UserAction.CheckSignInStatusAction, is UserAction.StartCheckingUserSignInStatusAction, is UserAction.SigningInUserAction -> UserState.CheckingUserSignInStatus


            is UserAction.ValidateUserCredentialsAction, is UserAction.ValidatingUserCredentialsAction -> UserState.ValidatingUserCredentialsState

            is UserAction.UserCredentialsValidatedAction -> {

                UserState.UserCredentialsValidationState(action.username, action.password)
            }

            is UserAction.UserSignInAction -> UserState.SigningInUserState

            is UserAction.UserSignedInSuccessfullyAction -> UserState.UserSignedInSuccessfullyState

            is UserAction.ErrorSigningInUserAction -> UserState.ErrorSigningInUserState(action.error)

            is UserAction.UserSignedInAction -> UserState.UserSignedInState

            is UserAction.UserNotSignedInAction -> UserState.UserSignedOutState

            else -> state
        }
    }
}