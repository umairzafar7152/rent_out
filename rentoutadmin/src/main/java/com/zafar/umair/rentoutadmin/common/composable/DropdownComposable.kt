package com.zafar.umair.rentoutadmin.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownContextMenu(
    options: List<String>,
    modifier: Modifier,
    onActionClick: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        modifier = modifier,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        Icon(
            modifier = Modifier
                .padding(8.dp, 0.dp)
                .menuAnchor(),
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More"
        )

        ExposedDropdownMenu(
            modifier = Modifier.width(180.dp),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption) },
                    onClick = {
                        onActionClick(selectionOption)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    @StringRes label: Int,
    value: String,
    options: List<String>,
    modifier: Modifier,
    isEnabled: Boolean,
    onNewValue: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
//    var selectedOptionText by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        modifier = modifier,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            value = value,
            onValueChange = {},
            label = { Text(stringResource(label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded) },
            colors = dropdownColors()
        )

        if (isEnabled)
            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = {
                            Text(text = selectionOption)
                        },
                        onClick = {
//                        selectedOptionText = selectionOption
                            onNewValue(selectionOption)
                            isExpanded = false
                        }
                    )
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun dropdownColors(): TextFieldColors {
    return ExposedDropdownMenuDefaults.textFieldColors(
//    backgroundColor = MaterialTheme.colorScheme.onPrimary,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
//    trailingIconColor = MaterialTheme.colorScheme.onSurface,
        focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.primary
    )
}
