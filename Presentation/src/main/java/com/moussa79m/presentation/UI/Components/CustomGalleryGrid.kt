//package com.moussa79m.presentation.UI.Components
//package com.moussa79m.presentation.UI.Components
//
//import android.view.MotionEvent
//import android.view.ScaleGestureDetector
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.nestedscroll.nestedScroll
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.recyclerview.widget.DefaultItemAnimator
//import androidx.recyclerview.widget.RecyclerView
//import com.arasthel.spannedgridlayoutmanager.SpanSize
//import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager
//import com.moussa79m.domain.Model.MediaItem
//import com.moussa79m.presentation.GalleryMediaAdapter
//import com.moussa79m.presentation.SimpleGalleryItem
//
//// ركز هنا: يجب استخدام الـ Adapter الجديد المخصص للتواريخ والصور
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CustomGalleryGrid(
//    groupedMedia: Map<String, List<MediaItem>>,
//    showMemories: Boolean,
//    onZoomChange: (Int) -> Unit,
//    mediaList: List<MediaItem>,
//    columnCount: Int,
//    onMediaClick: (MediaItem) -> Unit
//) {
//    val context = LocalContext.current
//
//    // 1. تحضير القائمة (تم تغيير اسم المتغير الداخلي لمنع التداخل)
//    val listItems = remember(groupedMedia) {
//        val items = mutableListOf<SimpleGalleryItem>()
//        groupedMedia.forEach { (date, groupList) ->
//            items.add(SimpleGalleryItem(isHeader = true, dateTitle = date))
//            groupList.forEach {
//                items.add(SimpleGalleryItem(isHeader = false, media = it))
//            }
//        }
//        items
//    }
//
//    // 2. حساس الزووم
////    val scaleDetector = remember {
////        var scaleFactor = 1f
////        var currentCols = columnCount
////        ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
////            override fun onScale(detector: ScaleGestureDetector): Boolean {
////                scaleFactor *= detector.scaleFactor
////                if (scaleFactor > 1.15 && currentCols > 1) {
////                    currentCols--
////                    onZoomChange(currentCols)
////                    scaleFactor = 1f
////                    return true
////                } else if (scaleFactor < .85 && currentCols < 8) {
////                    currentCols++
////                    onZoomChange(currentCols)
////                    scaleFactor = 1f
////                    return true
////                }
////                return false
////            }
////        })
////    }
//
//    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
//    val nestedScrollInterop = rememberNestedScrollInteropConnection()
//    Scaffold(
//        modifier = Modifier
//            .fillMaxSize()
////            .nestedScroll(scrollBehavior.nestedScrollConnection)
//        ,topBar = {
//            Column(modifier = Modifier.background(Color.White)) {
//                // إصلاح مشكلة الذكريات: استخدمنا mediaList المبعوثة من الشاشة مباشرة
//                if (showMemories && mediaList.isNotEmpty()) {
//                    MemoriesBanner(mediaList = mediaList)
//                }
//            }
//        }
//    ) { paddingValues ->
//
//        // 3. شبكة الصور السريعة
//        AndroidView(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .nestedScroll(nestedScrollInterop)
////                .background(Color.White)
//            ,
//            factory = { ctx ->
//
//                val rv = RecyclerView(ctx)
//                var currentCols = columnCount
//
//                // 1. استخدام المحرك الأصلي
//                val spannedManager = SpannedGridLayoutManager(
//                    orientation = SpannedGridLayoutManager.Orientation.VERTICAL,
//                    spans = currentCols
//                )
//
//                val adapter = GalleryMediaAdapter(onMediaClick)
//
//                spannedManager.spanSizeLookup = SpannedGridLayoutManager.SpanSizeLookup { position ->
//                    val item = adapter.currentList.getOrNull(position) ?: return@SpanSizeLookup SpanSize(1, 1)
//                    if (item.isHeader) return@SpanSizeLookup SpanSize(currentCols, 1)
//
//                    val prevItem = adapter.currentList.getOrNull(position - 1)
//                    if (prevItem?.isHeader == true && currentCols >= 3) {
//                        return@SpanSizeLookup SpanSize(2, 2)
//                    }
//                    return@SpanSizeLookup SpanSize(1, 1)
//                }
//
//                rv.layoutManager = spannedManager
//                rv.adapter = adapter
//
//                // 2. السر في حركة "الطيران": تبطيء الأنيميشن عشان تلحق تشوف الصور وهي بتتحرك لمكانها
//                val animator = DefaultItemAnimator()
//                animator.moveDuration = 300
//                animator.changeDuration = 300
//                rv.itemAnimator = animator
//                rv.isNestedScrollingEnabled = true
//
//                // 3. حساس الزووم الحقيقي (بدون أي Scale وهمي)
//                var accumulatedScale = 1f
//                val scaleDetector = ScaleGestureDetector(ctx, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
//
//                    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
//                        accumulatedScale = 1f
//                        return true
//                    }
//
//                    override fun onScale(detector: ScaleGestureDetector): Boolean {
//                        accumulatedScale *= detector.scaleFactor
//                        var changed = false
//
//                        // تغيير الأعمدة أثناء حركة الأصابع (الصور هتطير لحظياً)
//                        if (accumulatedScale > 1.25f && currentCols > 1) {
//                            currentCols--
//                            changed = true
//                            accumulatedScale = 1f // تصفير العداد
//                        } else if (accumulatedScale < 0.75f && currentCols < 8) {
//                            currentCols++
//                            changed = true
//                            accumulatedScale = 1f // تصفير العداد
//                        }
//
//                        if (changed) {
//                            spannedManager.spans = currentCols
//                            // إجبار الكروت على تغيير مقاسها والطيران لمكانها الجديد
//                            rv.adapter?.notifyItemRangeChanged(0, rv.adapter?.itemCount ?: 0)
//                            onZoomChange(currentCols)
//                        }
//                        return true
//                    }
//                })
//
//                // 4. الحل القطعي للسكرول: لا تتدخل في السكرول إلا لو فيه صباعين على الشاشة
//                rv.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
//                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
//                        scaleDetector.onTouchEvent(e)
//                        // لو حساس الزووم شغال، امنع السكرول.. لو لأ، سيبه يسكرول طلقة
//                        return scaleDetector.isInProgress
//                    }
//
//                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
//                        scaleDetector.onTouchEvent(e)
//                    }
//                })
//
//                rv
//            }
////            factory = { ctx ->
////                // هنا بنعمل Custom RecyclerView سريع عشان نلقط اللمسة من المنبع
////                val rv = RecyclerView(ctx)
////                val gridLayoutManager = GridLayoutManager(ctx, columnCount)
////                val adapter = GalleryMediaAdapter(onMediaClick)
////// معادلة الأعمدة
////                gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
////                    override fun getSpanSize(position: Int): Int {
////                        val item =adapter.currentList.getOrNull(position)?:return 1
////                        if (item.isHeader)return gridLayoutManager.spanCount
////
////                   val prevItem=adapter.currentList.getOrNull(position-1)
////                        if (prevItem?.isHeader==true && gridLayoutManager.spanCount>=3)return 2
////                    return 1
////                    }
////                }
////                rv.layoutManager=gridLayoutManager
////                rv.adapter=adapter
////                rv.itemAnimator= DefaultItemAnimator()
////                rv.isNestedScrollingEnabled=true // for memories sliding
////// --- السر الحقيقي لسوني: حساس الزووم الداخلي المستقل ---
////
////                var currentCols=columnCount
////                val scalDetector= ScaleGestureDetector(ctx,object : ScaleGestureDetector.SimpleOnScaleGestureListener()
////                {
////                    override fun onScale(detector: ScaleGestureDetector): Boolean {
////                        var changed= false
////                        if(detector.scaleFactor>1.15 && currentCols>1){
////                            currentCols--
////                            changed=true
////                        }else if(detector.scaleFactor<.85f && currentCols<8){
////                            currentCols++
////                            changed=true
////                        }
////                        if (changed){
////                            gridLayoutManager.spanCount=currentCols
////                            adapter.notifyItemRangeChanged(0,adapter.itemCount)
////                            onZoomChange(currentCols)
////                        }
////                        return changed
////                    }
////                })
////
////                rv.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener(){
////                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
////                    scalDetector.onTouchEvent(e)
////                    return scalDetector.isInProgress
////                    }
////
////                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
////                        scalDetector.onTouchEvent(e)
////                    }
////                })
////                rv
////            },
//            ,update = { rv ->
//             val adapter=rv.adapter as GalleryMediaAdapter
//                adapter.submitList(listItems)
//            }
//        )
//    }
//}
//
package com.moussa79m.presentation.UI.Components

import MediaItemCard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dokar.pinchzoomgrid.PinchItemTransitions
import com.dokar.pinchzoomgrid.PinchZoomGridLayout
import com.dokar.pinchzoomgrid.rememberPinchZoomGridState
import com.moussa79m.domain.Model.MediaItem


@Composable
fun CustomGalleryGrid(
    groupedMedia: Map<String, List<MediaItem>>,
    showMemories: Boolean,
    mediaList: List<MediaItem>,
    onMediaClick: (MediaItem) -> Unit
) {
    // 1. تحديد مستويات الزووم
    val cellsList = remember {
        listOf(
            GridCells.Fixed(8),
            GridCells.Fixed(6),
            GridCells.Fixed(4),
            GridCells.Fixed(3),
            GridCells.Fixed(2),
            GridCells.Fixed(1)
        )
    }

    val state = rememberPinchZoomGridState(
        cellsList = cellsList,
        initialCellsIndex = 3 // البداية بـ 3 أعمدة
    )

    Box(modifier = Modifier.fillMaxSize()) {
        PinchZoomGridLayout(state = state) {
            LazyVerticalGrid(
                columns = gridCells,
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                userScrollEnabled = !state.isZooming // 🚀 السطر ده بيمنع التقطيع وسرقة اللمس الرأسي وقت الزووم
            ) {

                // --- شريط الذكريات ---
                if (showMemories && mediaList.isNotEmpty()) {
                    item(
                        span = { GridItemSpan(maxLineSpan) },
                        contentType = "Banner"
                    ) {
                        MemoriesBanner(
                            mediaList = mediaList,
                            modifier = Modifier.pinchItem(
                                key = "banner",
                                transitions = PinchItemTransitions.Translate // عشان مايتمطش مع الزووم
                            )
                        )
                    }
                }

                // --- التواريخ والصور ---
                groupedMedia.forEach { (date, itemsList) ->
                    // التاريخ (Header)
                    item(
                        span = { GridItemSpan(maxLineSpan) },
                        key = "header_$date",
                        contentType = "Header"
                    ) {
                        Text(
                            text = date,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .pinchItem(
                                    key = "header_$date",
                                    transitions = PinchItemTransitions.Translate // يتحرك بس وميكبرش
                                )
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }

                    // الصور
                    items(
                        items = itemsList,
                        key = { it.id },
                        contentType = { "MediaItem" }
                    ) { mediaItem ->

                        MediaItemCard(
                            mediaItem = mediaItem,
                            onClick = { onMediaClick(mediaItem) },
                            modifier = Modifier.pinchItem(key = mediaItem.id) // أنيميشن الطيران والزووم
                        )
                    }
                }

            }
//            SonyFastScroller(gridState=gridState,mediaList=mediaList, modifier = Modifier.align(
//                Alignment.CenterEnd).padding(vertical = 200.dp))

        }

    }
}