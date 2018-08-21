package com.abeemukthees.data.user

import com.abeemukthees.data.DataStoreFactory
import com.abeemukthees.domain.entities.User
import com.abeemukthees.domain.repository.UserRepository
import com.abeemukthees.domain.usecases.user.SignInUser
import io.reactivex.Observable

class UserRepositoryImpl(private val dataStoreFactory: DataStoreFactory) : UserRepository {

    override fun getUser(): Observable<User> = dataStoreFactory.userDataStore.getUser()

    override fun getUserLoginStatus(): Observable<Boolean> = dataStoreFactory.userDataStore.getUserSignInStatus()

    override fun signInUser(params: SignInUser.Params): Observable<Pair<Boolean, Throwable?>> = dataStoreFactory.userDataStore.signInUser(params.username, params.password)


    override fun signOutUser(): Observable<Boolean> = dataStoreFactory.userDataStore.signOutUser()
}