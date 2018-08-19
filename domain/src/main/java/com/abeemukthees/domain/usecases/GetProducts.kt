package com.abeemukthees.domain.usecases

import com.abeemukthees.domain.base.Action
import com.abeemukthees.domain.base.State
import com.abeemukthees.domain.common.SortBy
import com.abeemukthees.domain.executor.PostExecutionThread
import com.abeemukthees.domain.executor.ThreadExecutor
import com.abeemukthees.domain.interactor.ObservableUseCase
import com.abeemukthees.domain.repository.ProductRepository
import com.abeemukthees.domain.statemachine.*
import com.freeletics.rxredux.StateAccessor
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class GetProducts(private val productRepository: ProductRepository, threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread) : ObservableUseCase(threadExecutor, postExecutionThread) {


    override fun buildUseCaseObservable(action: Observable<Action>, state: State): Observable<Action> {
        val page = (if (state is ContainsCollection) state.page else 0) + 1
        println("GetProducts, Is State ContainsCollection = ${state is ContainsCollection}, State = ${state::class.simpleName},  calculated page = $page")
        return productRepository.getProducts(Params(SortBy.NEWEST, page))
                .map<Action> { DataLoaded(it, page) }
                .onErrorReturn { error -> ErrorLoadingMoreDataAction(error, page) }
                .startWith(StartLoadingMoreData(page))
    }


    private fun loadFirstPageSideEffect(
            actions: Observable<Action>,
            state: StateAccessor<State>
    ): Observable<Action> =
            actions.ofType(HomeAction.LoadInitialDataAction::class.java)
                    .filter { state() !is ContainsCollection } // If first page has already been loaded, do nothing
                    .switchMap {
                        loadProducts(actions, state())
                    }

    private fun loadNextPageSideEffect(
            actions: Observable<Action>,
            state: StateAccessor<State>
    ): Observable<Action> =
            actions
                    .ofType(HomeAction.LoadMoreDataAction::class.java)
                    .switchMap {
                        loadProducts(actions, state())
                    }


    private fun showAndHideLoadingErrorSideEffect(
            actions: Observable<Action>,
            state: StateAccessor<State>
    ): Observable<Action> =
            actions.ofType(ErrorLoadingMoreDataAction::class.java)
                    .filter { it.page > 1 }
                    .switchMap { action ->
                        Observable.timer(3, TimeUnit.SECONDS)
                                .map<Action> { HideErrorLoadingMoreDataAction(action.error, action.page) }
                                .startWith(ShowErrorLoadingMoreDataAction(action.error, action.page))
                    }

    val listOfSideEffects = listOf(::loadFirstPageSideEffect, ::loadNextPageSideEffect, ::showAndHideLoadingErrorSideEffect)

    private fun loadProducts(actions: Observable<Action>, state: State): Observable<Action> {
        return execute(actions, state)
    }

    data class Params(val sortBy: SortBy, val page: Int)
}