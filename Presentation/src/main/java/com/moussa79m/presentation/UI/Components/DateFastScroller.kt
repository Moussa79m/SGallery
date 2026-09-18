//package com.moussa79m.presentation.UI.Components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.grid.LazyGridState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material.icons.filled.ArrowDropUp
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableFloatStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.layout.onGloballyPositioned
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.dp
//import com.moussa79m.domain.Model.MediaItem
//import kotlinx.coroutines.launch
//import kotlin.math.abs
//
//@Composable
//fun SonyFastScroller(
//    gridState: LazyGridState,
//    mediaList: List<MediaItem>,
//    modifier: Modifier = Modifier
//) {
//    val coroutineScope = rememberCoroutineScope()
//    var scrollbarHeight by remember { mutableFloatStateOf(0f) }
//    var isDragging by remember { mutableStateOf(false) }
//    var dragProgress by remember { mutableFloatStateOf(0f) }
//    var thumbHeight by remember { mutableFloatStateOf(0f) } // لحفظ طول المؤشر عشان يتسنتر تحت صباعك
//
//    // حساب الاندكس الحالي بناءً على السحب أو السكرول العادي
//    val firstVisibleIndex = gridState.firstVisibleItemIndex
//    val currentIndex = if (isDragging) {
//        (dragProgress * mediaList.size).toInt().coerceIn(0, mediaList.lastIndex)
//    } else {
//        firstVisibleIndex.coerceIn(0, mediaList.lastIndex)
//    }
//
//    // التاريخ اللي هيظهر (مع حماية من الـ Crash لو اللستة فاضية)
//    val currentDate = if (mediaList.isNotEmpty()) mediaList[currentIndex].addedDate else ""
//
//    // 1. حاوية اللمس (عريضة ومخفية عشان صباعك مايفلتش منها أبداً)
//    Box(
//        modifier = modifier
//            .fillMaxHeight()
//            .width(60.dp) // منطقة اللمس العريضة
//            .onGloballyPositioned { scrollbarHeight = it.size.height.toFloat() }
//            .pointerInput(Unit) {
//                detectDragGestures(
//                    onDragStart = { offset ->
//                        isDragging = true
//                        dragProgress = (offset.y / scrollbarHeight).coerceIn(0f, 1f)
//                    },
//                    onDragEnd = {
//                        isDragging = false
//                        // قفزة ذكية وناعمة لما تسيب صباعك
//                        coroutineScope.launch {
//                            smartSmoothScrollTo(gridState, currentIndex)
//                        }
//                    },
//                    onDragCancel = { isDragging = false }
//                ) { change, dragAmount ->
//                    change.consume()
//                    isDragging = true
//                    // تحديث مكان الشريط مع السحب
//                    val currentY = change.position.y
//                    dragProgress = (currentY / scrollbarHeight).coerceIn(0f, 1f)
//
//                    // قفز فوري أثناء السحب لضمان السرعة
//                    val targetIndex = (dragProgress * mediaList.size).toInt().coerceIn(0, mediaList.lastIndex)
//                    coroutineScope.launch {
//                        gridState.scrollToItem(targetIndex)
//                    }
//                }
//            }
//    ) {
//        // 2. إظهار الشريط والتاريخ بس لو بنسحب أو بنعمل سكرول عادي
//        if (isDragging || gridState.isScrollInProgress) {
//
//            // حساب الـ Y عشان المؤشر يتحرك
//            val thumbY = if (isDragging) {
//                (dragProgress * scrollbarHeight) - (thumbHeight / 2) // توسيط تحت الصباع
//            } else {
//                if (mediaList.isNotEmpty()) {
//                    (firstVisibleIndex.toFloat() / mediaList.size) * scrollbarHeight
//                } else 0f
//            }.coerceIn(0f, scrollbarHeight - thumbHeight)
//
//            Row(
//                modifier = Modifier
//                    .offset { IntOffset(0, thumbY.toInt()) }
//                    .onGloballyPositioned { thumbHeight = it.size.height.toFloat() },
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // 3. تصميم المؤشر (الأسهم) زي سوني بالمللي
//                Column(
//                    modifier = Modifier
//                        // زوايا دائرية من اليمين بس عشان يلزق في حافة الشاشة الشمال
//                        .background(Color.Black.copy(alpha = 0.65f)) // لون غامق شفاف
//                        .padding(horizontal = 2.dp, vertical = 6.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.ArrowDropUp,
//                        contentDescription = null,
//                        tint = Color.White,
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Icon(
//                        imageVector = Icons.Filled.ArrowDropDown,
//                        contentDescription = null,
//                        tint = Color.White,
//                        modifier = Modifier.size(24.dp)
//                    )
//                }
//
//                // 4. تصميم كارت التاريخ اللي بيظهر جنبه
//                if (isDragging) {
//                    Spacer(modifier = Modifier.width(8.dp)) // مسافة بين الأسهم والتاريخ
//                    Text(
//                        text = currentDate.toString(),
//                        color = Color.White,
//                        style = MaterialTheme.typography.titleMedium,
//                        modifier = Modifier
//                            .background(
//                                color = Color.Black.copy(alpha = 0.65f),
//                                shape = RoundedCornerShape(8.dp)
//                            )
//                            .padding(horizontal = 16.dp, vertical = 8.dp)
//                    )
//                }
//            }
//        }
//    }
//}
//
//// دالة السكرول الذكي (اللي بتحمي التطبيق من التقطيع)
//suspend fun smartSmoothScrollTo(gridState: LazyGridState, targetIndex: Int) {
//    val currentIndex = gridState.firstVisibleItemIndex
//    val distance = abs(targetIndex - currentIndex)
//    if (distance > 30) {
//        val preTarget = if (targetIndex > currentIndex) targetIndex - 10 else targetIndex + 10
//        gridState.scrollToItem(preTarget)
//    }
//    gridState.animateScrollToItem(targetIndex)
//}