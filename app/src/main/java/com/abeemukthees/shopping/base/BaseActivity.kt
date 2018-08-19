package com.abeemukthees.shopping.base

import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity : AppCompatActivity() {

    protected val TAG = this.javaClass.simpleName

    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun showToastMessage(message: String?, duration: Int = Toast.LENGTH_SHORT) {
        message?.let { Toast.makeText(this, it, duration).show() }
    }
}