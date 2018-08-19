package com.abeemukthees.domain.base

import com.abeemukthees.domain.entities.User

interface UserState : State

sealed class UserS : UserState {

    object UserLoggedIn : UserS()

    object UserLoggedOut : UserS()

    data class UserInfo(val user: User) : UserS()
}