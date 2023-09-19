package com.zafar.umair.rentoutadmin.common.ext

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.textButton(): Modifier {
  return this.fillMaxWidth().padding(16.dp, 8.dp, 16.dp, 0.dp)
}

fun Modifier.roundText(color: Color): Modifier {
  return this.background(color, shape = RoundedCornerShape(20.dp)).padding(12.dp)
}

fun Modifier.filledBasicButton(color: Color): Modifier {
  return this.background(color, shape = RoundedCornerShape(20.dp))
}

fun Modifier.textButtonWrapText(): Modifier {
  return this.wrapContentWidth().padding(16.dp, 8.dp, 16.dp, 0.dp)
}

fun Modifier.basicButton(): Modifier {
  return this.fillMaxWidth().padding(16.dp, 8.dp)
}

fun Modifier.card(): Modifier {
  return this.padding(16.dp, 0.dp, 16.dp, 8.dp)
}

fun Modifier.contextMenu(): Modifier {
  return this.wrapContentWidth()
}

fun Modifier.alertDialog(): Modifier {
  return this.wrapContentWidth().wrapContentHeight()
}

fun Modifier.dropdownSelector(): Modifier {
  return this.fillMaxWidth()
}

fun Modifier.fieldModifier(): Modifier {
  return this.fillMaxWidth().padding(16.dp, 4.dp)
}

fun Modifier.toolbarActions(): Modifier {
  return this.wrapContentSize(Alignment.TopEnd)
}

fun Modifier.spacer(): Modifier {
  return this.fillMaxWidth().padding(12.dp)
}

fun Modifier.smallSpacer(): Modifier {
  return this.fillMaxWidth().height(8.dp)
}
