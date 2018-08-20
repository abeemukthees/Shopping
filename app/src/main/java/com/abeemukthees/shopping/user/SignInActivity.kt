package com.abeemukthees.shopping.user

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import com.abeemukthees.domain.base.State
import com.abeemukthees.domain.statemachine.user.UserAction
import com.abeemukthees.domain.statemachine.user.UserState
import com.abeemukthees.shopping.R
import com.abeemukthees.shopping.base.BaseActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_signin.*
import org.koin.android.architecture.ext.viewModel

class SignInActivity : BaseActivity() {

    private val userViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val usernameChangeObservable = RxTextView.textChanges(edit_userName)/*.map { RxUtils.trimEndFunction }*/.map { it.toString() }.skip(1)
        val passwordChangeObservable = RxTextView.textChanges(edit_password)/*.map { RxUtils.trimEndFunction }*/.map { it.toString() }.skip(1)

        addDisposable(Observable.combineLatest(
                usernameChangeObservable,
                passwordChangeObservable,
                BiFunction { t1: String, t2: String -> UserAction.ValidateUserCredentialsAction(t1, t2) })
                .subscribe(userViewModel.input))

        userViewModel.state.observe(this, Observer {
            setupViews(it!!)
        })
    }


    private fun setupViews(state: State) {
        Log.d(TAG, "setupViews with state = ${state::class.simpleName}")

        when (state) {

            is UserState.UserCredentialsValidationState -> {

                Log.d(TAG, "${state.username}, ${state.password}")

                text_input_userName.isErrorEnabled = !state.username.first
                text_input_userName.error = state.username.second

                text_input_password.isErrorEnabled = !state.password.first
                text_input_password.error = state.password.second

                button_signIn.isEnabled = state.username.first && state.password.first
                button_signIn.backgroundTintList = ContextCompat.getColorStateList(this, if (state.username.first && state.password.first) R.color.validSignInButton else R.color.colorAccent)

            }
        }

    }
}
