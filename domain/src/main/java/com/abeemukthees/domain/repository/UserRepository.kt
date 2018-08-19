package com.abeemukthees.domain.repository

import com.abeemukthees.domain.entities.User
import io.reactivex.Observable

interface UserRepository {

    fun getUser(): Observable<User>

    fun getUserLoginStatus(): Observable<Boolean>
}