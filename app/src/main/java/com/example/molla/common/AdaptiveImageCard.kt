package com.example.molla.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun AdaptiveImageCard(images: List<ImageBitmap>) {
    when (images.size) {
        0 -> {
            Spacer(modifier = Modifier.height(8.dp))
        }
        1, 2 -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(vertical = 16.dp)
            ) {
                for (image in images) {
                    Image(
                        bitmap = image,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }
        }
        else -> {
            LazyRow(
                modifier = Modifier
                    .height(120.dp)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                itemsIndexed(images) { _, image ->
                    Image(
                        bitmap = images[1],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(120.dp)
                    )
                }
            }
        }
    }
}