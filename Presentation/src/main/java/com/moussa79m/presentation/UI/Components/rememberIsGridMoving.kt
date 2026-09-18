package com.moussa79m.presentation.UI.Components

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun rememberIsGridMoving(
    gridState: LazyGridState,
    idleDebounceMs: Long = 200L
): State<Boolean> {
    val moving = remember { mutableStateOf(false) }
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.isScrollInProgress }.collectLatest { scrolling ->
            if (scrolling) {
                moving.value = true
            } else {
                delay(idleDebounceMs)
                moving.value = false
            }
        }
    }
    return moving
}