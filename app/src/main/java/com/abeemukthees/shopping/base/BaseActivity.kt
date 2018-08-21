package com.abeemukthees.shopping.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.abeemukthees.shopping.coordinator.Navigator
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.get

abstract class BaseActivity : AppCompatActivity() {

    protected val TAG = this.javaClass.simpleName

    private val compositeDisposable = CompositeDisposable()

    private val navigator = get<Navigator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.setBaseActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        navigator.setBaseActivity(null)
    }

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun showToastMessage(message: String?, duration: Int = Toast.LENGTH_SHORT) {
        message?.let { Toast.makeText(this, it, duration).show() }
    }
}