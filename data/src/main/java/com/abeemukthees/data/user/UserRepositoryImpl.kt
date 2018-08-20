package com.abeemukthees.data.user

import com.abeemukthees.data.DataStoreFactory
import com.abeemukthees.domain.entities.User
import com.abeemukthees.domain.repository.UserRepository
import io.reactivex.Observable

class UserRepositoryImpl(private val dataStoreFactory: DataStoreFactory) : UserRepository {


    override fun getUser(): Observable<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserLoginStatus(): Observable<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}