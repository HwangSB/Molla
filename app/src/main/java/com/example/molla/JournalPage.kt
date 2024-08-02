package com.example.molla

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun JournalPage(modifier: Modifier) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        items(100) {
            Text(text = "Hello, Jounal!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JournalPagePreview() {
    JournalPage(Modifier)
}