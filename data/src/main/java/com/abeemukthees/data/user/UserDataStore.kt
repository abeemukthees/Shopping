package com.abeemukthees.data.user

import android.content.Context
import android.content.SharedPreferences
import com.abeemukthees.domain.entities.User
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class UserDataStore(private val context: Context) {

    private val TAG = UserDataStore::class.java.simpleName

    private val USER_DATA = "userData"

    private val USER_ID = "userId"
    private val USER_NAME = "username"
    private val IS_USER_SIGNED_IN = "IsUserSignedIn"

    private fun getUserDataSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
    }

    private fun getUserDataSharedPreferencesEditor(): SharedPreferences.Editor {
        return getUserDataSharedPreferences().edit()
    }

    fun updateUserInfo(user: User): Observable<Boolean> {

        val editor = getUserDataSharedPreferencesEditor()
        editor.putString(USER_ID, user.id)
        editor.putString(USER_NAME, user.name)
        editor.putBoolean(IS_USER_SIGNED_IN, true)
        return Observable.just(editor.commit())

    }

    fun setUserSignInStatus(isUserSignedIn: Boolean) = Observable.just(getUserDataSharedPreferencesEditor().putBoolean(IS_USER_SIGNED_IN, isUserSignedIn).commit()).delay(3, TimeUnit.SECONDS)

    fun getUserSignInStatus(): Observable<Boolean> = Observable.just(getUserDataSharedPreferences().getBoolean(IS_USER_SIGNED_IN, false)).delay(3, TimeUnit.SECONDS)

    fun getUser(): Observable<User> {

        val user = User(getUserDataSharedPreferences().getString(USER_ID, "NA"),
                getUserDataSharedPreferences().getString(USER_NAME, "NA"),
                "a@b.com",
                "9876543210",
                ""
        )

        return Observable.just(user).delay(3, TimeUnit.SECONDS)
    }

    fun signInUser(username: String, password: String): Observable<Pair<Boolean, Throwable?>> {
        val user = User("id1",
                username,
                "a@b.com",
                "9876543210",
                ""
        )

        return updateUserInfo(user).map { if (it) Pair(true, null) else Pair(false, Throwable("Error signing in user")) }.delay(3, TimeUnit.SECONDS)
    }

    fun signOutUser(): Observable<Boolean> = Observable.just(getUserDataSharedPreferencesEditor().putBoolean(IS_USER_SIGNED_IN, false).commit()).delay(3, TimeUnit.SECONDS)
}