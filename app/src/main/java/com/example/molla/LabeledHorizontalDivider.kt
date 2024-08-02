package com.example.molla

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LabeledHorizontalDivider(label: String) {
    Row {
        Box(
            modifier = Modifier
                .width(16.dp)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.outlineVariant,

            )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
                .align(Alignment.CenterVertically)
        )
    }
}