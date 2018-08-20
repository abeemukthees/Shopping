package com.abeemukthees.domain.repository

import com.abeemukthees.domain.entities.User
import com.abeemukthees.domain.usecases.user.SignInUser
import io.reactivex.Completable
import io.reactivex.Observable

interface UserRepository {

    fun getUser(): Observable<User>

    fun getUserLoginStatus(): Observable<Boolean>

    fun signInUser(params: SignInUser.Params): Observable<Pair<Boolean, Throwable?>>

    fun signOutUser(): Completable
}