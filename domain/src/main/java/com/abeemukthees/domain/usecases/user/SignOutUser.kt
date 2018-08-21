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

class SignOutUser(private val userRepository: UserRepository, threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread) : ObservableUseCase(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(action: Action, state: State): Observable<Action> {
        return userRepository.signOutUser().map { return@map if (it) UserAction.UserNotSignedInAction else UserAction.ErrorSigningOutUserAction(Throwable("Error signing out user")) }
    }

    fun signOutUser(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> = actions.ofType(UserAction.SignOutUserAction::class.java)
            .switchMap { execute(it, state()).startWith(UserAction.SigningOutUserAction) }

}