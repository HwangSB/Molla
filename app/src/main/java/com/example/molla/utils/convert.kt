package com.example.molla.utils

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.text.SimpleDateFormat
import java.util.Locale

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

fun parseDateToMonthDay(dateString: String): String {
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
    val output = SimpleDateFormat("yyyy MM dd", Locale.getDefault())
    val date = input.parse(dateString)

    return if (date != null) {
        output.format(date)
    } else {
        ""
    }
}