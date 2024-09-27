package com.example.molla.utils

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun convertBase64ToByteArray(base64: String): ByteArray {
    return Base64.decode(base64, Base64.DEFAULT)
}

fun convertBase64ToImageBitmap(base64: String): ImageBitmap {
    val imageByteArray = convertBase64ToByteArray(base64)
    val imageBitmap = BitmapFactory
        .decodeByteArray(imageByteArray, 0, imageByteArray.size)
        .asImageBitmap()
    return imageBitmap
}