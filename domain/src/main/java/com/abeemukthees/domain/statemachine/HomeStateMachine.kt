package com.abeemukthees.domain.statemachine

import com.abeemukthees.domain.base.Action
import com.abeemukthees.domain.base.State
import com.abeemukthees.domain.entities.Product
import com.abeemukthees.domain.usecases.GetProducts
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

interface LoadDataAction : Action

sealed class HomeAction : Action {

    object LoadInitialDataAction : HomeAction(), LoadDataAction

    object LoadMoreDataAction : HomeAction(), LoadDataAction

    //object LoadingData : HomeAction()


}

data class DataLoaded(val products: List<Product>, val page: Int) : HomeAction()

data class ErrorLoadingMoreDataAction(val error: Throwable, val page: Int) : HomeAction()

private data class ShowErrorLoadingInitialDataAction(val error: Throwable, val page: Int = 0) : HomeAction()

private data class HideErrorLoadingInitialDataAction(val error: Throwable, val page: Int = 0) : HomeAction()

data class ShowErrorLoadingMoreDataAction(val error: Throwable, val page: Int) : HomeAction()

data class HideErrorLoadingMoreDataAction(val error: Throwable, val page: Int) : HomeAction()

data class StartLoadingMoreData(val page: Int) : HomeAction()


interface ContainsCollection {

    val items: List<Product>
    val page: Int
}


sealed class HomeState : State {

    object InitialHomeState : HomeState()

    object LoadingFirstTimeState : HomeState()

    data class ErrorLoadingInitialDataState(val errorMessage: String) : HomeState()

    data class ErrorLoadingMoreDataState(val error: Throwable) : HomeState()

    data class ShowDataState(override val items: List<Product>, override val page: Int) : HomeState(), ContainsCollection

    data class ShowDataAndLoadMoreState(override val items: List<Product>, override val page: Int) : HomeState(), ContainsCollection

    data class ShowDataAndLoadMoreErrorState(override val items: List<Product>, override val page: Int, val errorMessage: String) : HomeState(), ContainsCollection

}

class HomeStateMachine(getProducts: GetProducts) {

    val input: Relay<Action> = PublishRelay.create()

    val state: Observable<State> = input
            .doOnNext { println("Input Action ${it.javaClass.simpleName}") }
            .reduxStore(
                    initialState = HomeState.InitialHomeState,
                    sideEffects = getProducts.listOfSideEffects,
                    reducer = ::reducer)
            .distinctUntilChanged()
            .doOnNext { println("RxStore state ${it.javaClass.simpleName}") }

    /**
     * The state reducer.
     * Takes Actions and the current state to calculate the new state.
     */
    private fun reducer(state: State, action: Action): State {
        //println("Reducing state =  ${state::class.simpleName}, action = ${action::class.simpleName}")
        return when (action) {

            is StartLoadingMoreData -> {

                if (state is ContainsCollection && state.page >= 1) {
                    HomeState.ShowDataAndLoadMoreState(state.items, state.page)
                } else HomeState.LoadingFirstTimeState
            }

            is DataLoaded -> {
                val items = if (state is ContainsCollection) {
                    state.items + action.products
                } else action.products

                HomeState.ShowDataState(items, action.page)
            }

            is ErrorLoadingMoreDataAction -> {
                if (action.page == 1) HomeState.ErrorLoadingInitialDataState(action.error.localizedMessage)
                else {
                    // page > 1 is handled in showAndHideLoadingErrorSideEffect()
                    state
                }
            }

            is ShowErrorLoadingMoreDataAction -> {
                if (state !is ContainsCollection) {
                    throw IllegalStateException("We never loaded the first page")
                }
                HomeState.ShowDataAndLoadMoreErrorState(state.items, state.page, action.error.localizedMessage)
            }

            is HideErrorLoadingMoreDataAction -> {
                if (state !is ContainsCollection) {
                    throw IllegalStateException("We never loaded the first page")
                }
                HomeState.ShowDataState(state.items, state.page)
            }

            is HomeAction.LoadInitialDataAction, HomeAction.LoadMoreDataAction -> state

            else -> throw IllegalStateException("Invalid state")
        }


    }
}