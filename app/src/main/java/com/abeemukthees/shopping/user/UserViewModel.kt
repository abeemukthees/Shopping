package com.abeemukthees.shopping.user

import com.abeemukthees.domain.base.NavigateAction
import com.abeemukthees.domain.statemachine.user.UserStateMachine
import com.abeemukthees.shopping.base.BaseViewModel
import com.abeemukthees.shopping.coordinator.SignInFlowCoordinator

class UserViewModel(userStateMachine: UserStateMachine, signInFlowCoordinator: SignInFlowCoordinator, navigationCallback: ((NavigateAction) -> Unit)?) : BaseViewModel(userStateMachine, navigationCallback)