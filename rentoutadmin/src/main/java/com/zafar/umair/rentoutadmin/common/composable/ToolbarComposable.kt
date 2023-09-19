@file:OptIn(ExperimentalMaterial3Api::class)

package com.zafar.umair.rentoutadmin.common.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun BasicToolbar(@StringRes title: Int) {
  TopAppBar(title = { Text(stringResource(title)) }, colors = TopAppBarDefaults.smallTopAppBarColors())
}

@Composable
fun ActionToolbar(
  @StringRes title: Int,
  @DrawableRes endActionIcon: Int,
  modifier: Modifier,
  endAction: () -> Unit
) {
  TopAppBar(
    title = { Text(stringResource(title)) },
    colors = TopAppBarDefaults.smallTopAppBarColors(),
    actions = {
      Box(modifier) {
        IconButton(onClick = endAction) {
          Icon(painter = painterResource(endActionIcon), contentDescription = "Action")
        }
      }
    }
  )
}

@Composable
private fun toolbarColor(darkTheme: Boolean = isSystemInDarkTheme()): Color {
  return if (darkTheme) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
}
