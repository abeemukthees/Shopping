package com.abeemukthees.shopping.coordinator

import android.util.Log
import com.abeemukthees.domain.repository.UserRepository

class RootFlowCoordinator(userRepository: UserRepository, private val signInFlowCoordinator: SignInFlowCoordinator, private val homeFlowCoordinator: HomeFlowCoordinator) {


    private val TAG = RootFlowCoordinator::class.simpleName


    init {

        Log.d(TAG, "Init... RootFlowCoordinator")

        userRepository.getUserLoginStatus().subscribe { isUserSignedIn ->

            Log.d(TAG, "User sign in status $isUserSignedIn")

            if (isUserSignedIn) homeFlowCoordinator.gotoHome()
            else signInFlowCoordinator.signInUser()

        }
    }
}