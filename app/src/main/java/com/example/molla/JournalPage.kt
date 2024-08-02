package com.example.molla

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun JournalPage(modifier: Modifier) {
    // TODO: Load journal data from server

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "2024년 7월",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            LabeledHorizontalDivider("25일")
        }
        items(1) { index ->
            ExpandableCard(index)
        }
        item {
            LabeledHorizontalDivider("24일")
        }
        items(3) { index ->
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Hello, Jounal $index!",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    AdaptiveImageCard(
                        images = listOf(
                            ImageBitmap.imageResource(R.drawable.login_naver),
                            ImageBitmap.imageResource(R.drawable.login_naver),
                            ImageBitmap.imageResource(R.drawable.login_naver)
                        )
                    )
                    Text(
                        text = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        maxLines = 3, // Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis, // TextOverflow.Clip
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ExpandableCard(index: Int) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Hello, Jounal $index!",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            AdaptiveImageCard(
                images = listOf(
                    ImageBitmap.imageResource(R.drawable.login_naver),
                    ImageBitmap.imageResource(R.drawable.login_naver),
                    ImageBitmap.imageResource(R.drawable.login_naver)
                )
            )
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
                maxLines = if (!expanded) { 3 } else { Int.MAX_VALUE },
                overflow = if (!expanded) { TextOverflow.Ellipsis } else { TextOverflow.Clip },
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdaptiveImageCard(images: List<ImageBitmap>) {
    when (images.size) {
        0 -> {
            Spacer(modifier = Modifier.height(8.dp))
        }
        1 -> {
            Image(
                bitmap = images[0],
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(vertical = 16.dp)
            )
        }
        2 -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(vertical = 16.dp)
            ) {
                Image(
                    bitmap = images[0],
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                Image(
                    bitmap = images[1],
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
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

@Preview(showBackground = true)
@Composable
fun JournalPagePreview() {
    JournalPage(Modifier)
}