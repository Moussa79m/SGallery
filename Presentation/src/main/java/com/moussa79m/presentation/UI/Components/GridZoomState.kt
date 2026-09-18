package com.moussa79m.presentation.UI.Components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class GridZoomState(
    initialColumns: Int = 3,
    val minColumns: Int = 2,
    val maxColumns: Int = 6
) {
    var columnCount by mutableIntStateOf(initialColumns)
        private set

    val visualScale = Animatable(1f)

    // تراكم الزووم أثناء اللمس، مش State عشان ميعملش recomposition مع كل حركة
    var pendingScale: Float = 1f

    suspend fun onGestureEnd() {
        if (pendingScale == 1f) {
            visualScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
            return
        }

        val oldCount = columnCount
        val delta = when {
            pendingScale > 1.15f -> -1 // pinch-out → صور أكبر → أعمدة أقل
            pendingScale < 0.85f -> 1  // pinch-in → صور أصغر → أعمدة أكتر
            else -> 0
        }
        val newCount = (oldCount + delta).coerceIn(minColumns, maxColumns)

        if (newCount != oldCount) {
            columnCount = newCount
            // نبدأ بنفس الفرق البصري القديم عشان الانتقال يبقى ناعم من غير قفزة
            val compensationScale = oldCount.toFloat() / newCount.toFloat()
            visualScale.snapTo(compensationScale)
        }

        visualScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
        pendingScale = 1f
    }
}