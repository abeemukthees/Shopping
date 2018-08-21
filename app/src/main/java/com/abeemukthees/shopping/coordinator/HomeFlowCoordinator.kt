package com.abeemukthees.shopping.coordinator

class HomeFlowCoordinator(private val navigator: Navigator) {

    fun gotoHome() {
        navigator.showHome()
    }
}