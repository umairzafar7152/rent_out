package com.zafar.umair.rentout.screens.sign_up

data class SignUpUiState(
  val email: String = "",
  val password: String = "",
  val repeatPassword: String = "",
  val name: String = "",
  val address: String = "",
  val phone: String = ""
)
