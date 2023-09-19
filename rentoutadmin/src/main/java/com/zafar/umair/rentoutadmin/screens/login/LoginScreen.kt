package com.zafar.umair.rentoutadmin.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.zafar.umair.rentoutadmin.R.string as AppText
import com.zafar.umair.rentoutadmin.common.composable.BasicButton
import com.zafar.umair.rentoutadmin.common.composable.BasicTextButton
import com.zafar.umair.rentoutadmin.common.composable.BasicToolbar
import com.zafar.umair.rentoutadmin.common.composable.EmailField
import com.zafar.umair.rentoutadmin.common.composable.PasswordField
import com.zafar.umair.rentoutadmin.common.ext.basicButton
import com.zafar.umair.rentoutadmin.common.ext.fieldModifier
import com.zafar.umair.rentoutadmin.common.ext.textButton

@Composable
fun LoginScreen(
  openAndPopUp: (String, String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: LoginViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState

  BasicToolbar(AppText.login_details)

  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    EmailField(uiState.email, viewModel::onEmailChange, Modifier.fieldModifier())
    PasswordField(uiState.password, viewModel::onPasswordChange, Modifier.fieldModifier())

    BasicButton(AppText.sign_in, Modifier.basicButton()) { viewModel.onSignInClick(openAndPopUp) }

    BasicTextButton(AppText.forgot_password, Modifier.textButton()) {
      viewModel.onForgotPasswordClick()
    }
  }
}
