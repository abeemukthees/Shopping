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

class GetUserLoginStatus(private val userRepository: UserRepository, threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread) : ObservableUseCase(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(action: Action, state: State): Observable<Action> {
        return userRepository.getUserLoginStatus().map { return@map if (it) UserAction.UserSignedInAction else UserAction.UserNotSignedInAction }
    }

    fun checkUserSignInStatus(actions: Observable<Action>, state: StateAccessor<State>): Observable<Action> {
        //println("SideEffect -> action = checkUserSignInStatus ${actions::class.simpleName}, state = ${state()::class.simpleName}")
        return actions.ofType(UserAction.CheckSignInStatusAction::class.java)
                .switchMap { execute(it, state()).startWith(UserAction.StartCheckingUserSignInStatusAction) }


    }
}