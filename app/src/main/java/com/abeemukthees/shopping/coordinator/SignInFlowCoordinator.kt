package com.abeemukthees.shopping.coordinator

import com.abeemukthees.domain.base.NavigateAction

class SignInFlowCoordinator(private val navigator: Navigator) {

    fun signInUser() {
        navigator.showSignIn()
    }

    fun navigate(navigateAction: NavigateAction) {
        when (navigateAction) {

            is NavigateToOnBoarding -> navigator.showOnBoarding()
        }
    }
}