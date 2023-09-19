package com.zafar.umair.rentoutadmin.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.zafar.umair.rentoutadmin.R.string as AppText
import com.zafar.umair.rentoutadmin.common.composable.BasicButton
import com.zafar.umair.rentoutadmin.common.ext.basicButton
import kotlinx.coroutines.delay

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun SplashScreen(
  openAndPopUp: (String, String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: SplashViewModel = hiltViewModel()
) {
  Column(
    modifier =
      modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (viewModel.showError.value) {
      Text(text = stringResource(AppText.generic_error))

      BasicButton(AppText.try_again, Modifier.basicButton()) { viewModel.onAppStart(openAndPopUp) }
    } else {
      CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
    }
  }

  LaunchedEffect(true) {
    delay(SPLASH_TIMEOUT)
    viewModel.onAppStart(openAndPopUp)
  }
}
