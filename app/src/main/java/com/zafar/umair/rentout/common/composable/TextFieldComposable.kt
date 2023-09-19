@file:OptIn(ExperimentalMaterial3Api::class)

package com.zafar.umair.rentout.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.zafar.umair.rentout.R.drawable as AppIcon
import com.zafar.umair.rentout.R.string as AppText

@Composable
fun BasicField(
    @StringRes text: Int,
    value: String,
    maxLines: Int,
    keyboardOptions: KeyboardOptions,
    isEnabled: Boolean,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        enabled = isEnabled,
        keyboardOptions = keyboardOptions,
        onValueChange = { onNewValue(it) },
        maxLines = maxLines,
        label = { Text(stringResource(text)) },
        placeholder = { Text(stringResource(text)) }
    )
}

@Composable
fun SignUpField(
    @StringRes text: Int,
    value: String,
    maxLines: Int,
    keyboardOptions: KeyboardOptions,
    isEnabled: Boolean,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        enabled = isEnabled,
        keyboardOptions = keyboardOptions,
        onValueChange = { onNewValue(it) },
        maxLines = maxLines,
        label = { Text(stringResource(text)) },
        placeholder = { Text(stringResource(text)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Abc, contentDescription = "Text") }
    )
}

@Composable
fun EmailField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(AppText.email)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") }
    )
}

@Composable
fun PasswordField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    PasswordField(value, AppText.password, onNewValue, modifier)
}

@Composable
fun RepeatPasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PasswordField(value, AppText.repeat_password, onNewValue, modifier)
}

@Composable
private fun PasswordField(
    value: String,
    @StringRes placeholder: Int,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    val icon =
        if (isVisible) painterResource(AppIcon.ic_visibility_on)
        else painterResource(AppIcon.ic_visibility_off)

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(painter = icon, contentDescription = "Visibility")
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation
    )
}

@Composable
fun ChatTextField(
    @StringRes text: Int,
    value: String,
    maxLines: Int,
    keyboardOptions: KeyboardOptions,
    isVisible: Boolean,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSendClick: () -> Unit
) {
//  val isVisible by remember {
//    derivedStateOf {
//      value.isNotBlank()
//    }
//  }
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        keyboardOptions = keyboardOptions,
        onValueChange = { onNewValue(it) },
        maxLines = maxLines,
        placeholder = { Text(stringResource(text)) },
        trailingIcon = {
            if (isVisible) {
                IconButton(
                    onClick = onSendClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Clear"
                    )
                }
            }
        }

    )
}