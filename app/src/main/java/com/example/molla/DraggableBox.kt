package com.example.molla

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

enum class DragAnchors {
    Start,
    Center,
    End,
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableBox(
    actionSize: Dp = 80.dp,
    startAction: @Composable (BoxScope.() -> Unit)? = {},
    endAction: @Composable (BoxScope.() -> Unit)? = {},
    content: @Composable (() -> Unit)
) {
    val density = LocalDensity.current
    val startActionSizePx = with(density) { actionSize.toPx() }
    val endActionSizePx = with(density) { actionSize.toPx() }
    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Center,
            anchors = DraggableAnchors {
                DragAnchors.Start at -startActionSizePx
                DragAnchors.Center at 0f
                DragAnchors.End at endActionSizePx
            },
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        startAction?.let {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(actionSize)
                    .align(Alignment.CenterStart)
            ) {
                startAction()
            }
        }
        endAction?.let {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(actionSize)
                    .align(Alignment.CenterEnd)
            ) {
                endAction()
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        x = -state
                            .requireOffset()
                            .roundToInt(),
                        y = 0,
                    )
                }
                .anchoredDraggable(state, Orientation.Horizontal, reverseDirection = true),
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DraggableBoxPreview() {
    DraggableBox(
        startAction = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(text = "Start")
            }
        },
        endAction = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(text = "End")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Drag me"
            )
        }
    }
}