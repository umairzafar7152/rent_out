package com.zafar.umair.rentout.screens.sign_up

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zafar.umair.rentout.R.string as AppText
import com.zafar.umair.rentout.common.composable.*
import com.zafar.umair.rentout.common.ext.basicButton
import com.zafar.umair.rentout.common.ext.fieldModifier

@Composable
fun SignUpScreen(
  openAndPopUp: (String, String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: SignUpViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState
  val fieldModifier = Modifier.fieldModifier()

  BasicToolbar(AppText.create_account)

  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    EmailField(uiState.email, viewModel::onEmailChange, fieldModifier)
    PasswordField(uiState.password, viewModel::onPasswordChange, fieldModifier)
    RepeatPasswordField(uiState.repeatPassword, viewModel::onRepeatPasswordChange, fieldModifier)
    SignUpField(
      text = AppText.name,
      value = uiState.name,
      maxLines = 1,
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
      isEnabled = true,
      onNewValue = viewModel::onNameChange,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    )
    SignUpField(
      text = AppText.address,
      value = uiState.address,
      maxLines = 1,
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
      isEnabled = true,
      onNewValue = viewModel::onAddressChange,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    )
    SignUpField(
      text = AppText.phone,
      value = uiState.phone,
      maxLines = 1,
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
      isEnabled = true,
      onNewValue = viewModel::onPhoneChange,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    )

    BasicButton(AppText.create_account, Modifier.basicButton()) {
      viewModel.onSignUpClick(openAndPopUp)
    }
  }
}
