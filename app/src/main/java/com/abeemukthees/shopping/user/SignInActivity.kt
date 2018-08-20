package com.abeemukthees.shopping.user

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.abeemukthees.domain.base.State
import com.abeemukthees.domain.statemachine.user.UserAction
import com.abeemukthees.domain.statemachine.user.UserState
import com.abeemukthees.shopping.R
import com.abeemukthees.shopping.base.BaseActivity
import com.jakewharton.rxbinding2.view.RxView
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

        addDisposable(RxView.clicks(button_signIn)
                .map { UserAction.UserSignInAction(edit_userName.text.toString(), edit_password.text.toString()) }
                .subscribe(userViewModel.input))

        userViewModel.input.accept(UserAction.CheckSignInStatusAction)


    }


    private fun setupViews(state: State) {
        Log.d(TAG, "setupViews with state = ${state::class.simpleName}")

        when (state) {


            is UserState.CheckingUserSignInStatus -> {

                progressBar.visibility = VISIBLE
            }

            is UserState.ValidatingUserCredentialsState -> {


            }

            is UserState.UserCredentialsValidationState -> {

                Log.d(TAG, "${state.username}, ${state.password}")

                text_input_userName.isErrorEnabled = !state.username.first
                text_input_userName.error = state.username.second

                text_input_password.isErrorEnabled = !state.password.first
                text_input_password.error = state.password.second

                button_signIn.isEnabled = state.username.first && state.password.first
                button_signIn.backgroundTintList = ContextCompat.getColorStateList(this, if (state.username.first && state.password.first) R.color.validSignInButton else R.color.colorAccent)

            }

            is UserState.SigningInUserState -> progressBar.visibility = VISIBLE

            is UserState.UserSignedInSuccessfullyState -> {
                showToastMessage("User signed in successfully")
                progressBar.visibility = GONE
            }

            is UserState.UserSignedInState -> {

                showToastMessage("User signed in")
                progressBar.visibility = GONE
            }

            is UserState.UserSignedOutState -> {

                showToastMessage("User not signed in")
                progressBar.visibility = GONE
            }


            else -> {

                progressBar.visibility = GONE

            }

        }

    }
}
