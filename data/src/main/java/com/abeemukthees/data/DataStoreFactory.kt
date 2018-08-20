package com.abeemukthees.data

import android.content.Context
import com.abeemukthees.data.dummy.DummyDataStore
import com.abeemukthees.data.user.UserDataStore

class DataStoreFactory(context: Context) {

    val dummyDataStore = DummyDataStore(context)
    val userDataStore = UserDataStore(context)

}