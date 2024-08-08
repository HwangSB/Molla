package com.example.molla

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DropdownMenuComponent(
    expanded: MutableState<Boolean>,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        DropdownMenuItem(
            text = { Text("게시글 수정") },
            onClick = {
                onEditClick()
                expanded.value = false
            }
        )
        DropdownMenuItem(
            text = { Text("게시글 삭제") },
            onClick = {
                onDeleteClick()
                expanded.value = false
            }
        )
        DropdownMenuItem(
            text = { Text("신고") },
            onClick = {
                onReportClick()
                expanded.value = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDropdownMenuComponent() {
    val expanded = remember { mutableStateOf(true) }
    DropdownMenuComponent(
        expanded = expanded,
        onEditClick = {},
        onDeleteClick = {},
        onReportClick = {}
    )
}