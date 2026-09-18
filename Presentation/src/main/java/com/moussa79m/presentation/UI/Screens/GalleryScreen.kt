package com.moussa79m.presentation.UI.Screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.moussa79m.presentation.GalleryFilter
import com.moussa79m.presentation.UI.Components.CustomGalleryGrid
import com.moussa79m.presentation.UI.Components.FullScreenViewer
import com.moussa79m.presentation.ViewModel.GalleryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel
) {
    val states by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showAlbumScreen by remember { mutableStateOf(false) }

    var selectedMediaIndex by remember { mutableStateOf<Int?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO
        )
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val hasPermission = remember {
        mutableStateOf(permissionsToRequest.all {
            ContextCompat.checkSelfPermission(
                context, it
            ) == PackageManager.PERMISSION_GRANTED
        })
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionMap ->
            val areGranted = permissionMap.values.reduce { acc, next -> acc && next }
            hasPermission.value = areGranted
            if (areGranted) {
                viewModel.loadMedia(GalleryFilter.All)
            }
        })
    BackHandler(enabled = states.currentFilter is GalleryFilter.All) {
        showAlbumScreen = false
    }
    LaunchedEffect(Unit) {
        if (!hasPermission.value) {
            permissionLauncher.launch(permissionsToRequest)
        }
    }
    var columnCount by remember { mutableIntStateOf(3) }
    var currentZoom by remember { mutableFloatStateOf(1f) }
    val gridState = rememberLazyGridState()
    ModalNavigationDrawer(
        drawerState = drawerState, gesturesEnabled = drawerState.isOpen, drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerShape = RectangleShape,
                modifier = Modifier.width(300.dp)
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    "S Gallery",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp)
                )
//                HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
                //images
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Photo,
                            contentDescription = null,
                            tint = Color(0xFFFFA500)
                        )
                        //////// put color in colors file after that
                    },
//                    الجدول الزمني (الكل)
                    label = {
                        Text(
                            text = "All content",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    selected = !showAlbumScreen && states.currentFilter is GalleryFilter.All,
                    onClick = {
                        showAlbumScreen = false
                        viewModel.loadMedia(GalleryFilter.All)
                        scope.launch { drawerState.close() }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedIconColor = Color.Transparent,
                        selectedIconColor = Color(0xFFF0F0F0)
                    ),
                    shape = RectangleShape
                )
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E))
//folders
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            tint = Color(0xFFFFC107)
                        )
                        //////// put color in colors file after that
                    },
                    label = {
                        Text(
                            text = "Folders",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }, selected = showAlbumScreen, onClick = {
                        showAlbumScreen = true
                        scope.launch { drawerState.close() }
                    }, colors = NavigationDrawerItemDefaults.colors(
                        unselectedIconColor = Color.Transparent,
                        selectedIconColor = Color(0xFFF0F0F0)
                    ), shape = RectangleShape
                )
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E))
                //images
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Landscape,
                            contentDescription = null,
                            tint = Color.Blue
                        )
                        //////// put color in colors file after that
                    },
                    label = {
                        Text(
                            text = "Images",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    selected = !showAlbumScreen && states.currentFilter is GalleryFilter.Images,
                    onClick = {
                        showAlbumScreen = false
                        viewModel.loadMedia(GalleryFilter.Images)
                        scope.launch { drawerState.close() }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedIconColor = Color.Transparent,
                        selectedIconColor = Color(0xFFF0F0F0)
                    ),
                    shape = RectangleShape
                )
                //video
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = null,
                            tint = Color(0xFFE53935)
                        )
                        //////// put color in colors file after that
                    },
                    label = {
                        Text(
                            text = "Videos",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    selected = !showAlbumScreen && states.currentFilter is GalleryFilter.Videos,
                    onClick = {
                        showAlbumScreen = false
                        viewModel.loadMedia(GalleryFilter.Videos)
                        scope.launch { drawerState.close() }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedIconColor = Color.Transparent,
                        selectedIconColor = Color(0xFFF0F0F0)
                    ),
                    shape = RectangleShape
                )

            }

        }) {
//        Scaffold(topBar = {
//            TopAppBar(
//                title = {
//                    val titleText = if (!showAlbumScreen) {
//                        when (states.currentFilter) {
//                            is GalleryFilter.All -> "All content"
//                            is GalleryFilter.Album -> (states.currentFilter as GalleryFilter.Album).albumName // understand why
//                            is GalleryFilter.Videos -> "Videos"
//                            is GalleryFilter.Images -> "Images"
//                        }
//                    } else {
//                        "Folders"
//                    }
//                    Text(titleText)
//                }, navigationIcon = {
//                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
//                        Icon(Icons.Default.Menu, contentDescription = "القائمة")
//                    }
//                }, colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.surface
//                )
//            )
//        })
        //        { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
//                .then(
//                    if (!showAlbumScreen) {
//                        Modifier.pointerInput(Unit) {
//                            detectTransformGestures { _, _, zoom, _ ->
//                                currentZoom *= zoom
//                                when {
//                                    currentZoom > 1.2f && columnCount > 1 -> {
//                                        columnCount--
//                                        currentZoom = 1f
//                                    }
//
//                                    currentZoom < 0.8f && columnCount < 6 -> {
//                                        columnCount++
//                                        currentZoom = 1f
//                                    }
//                                }
//                            }
//                        }
//                    } else Modifier)
                    ) {
            if (!hasPermission.value) {
                Text(
                    text = "نحتاج إلى صلاحية الوصول للصور والفيديوهات لعرض المعرض",
                    Modifier.align(Alignment.Center)
                )

            } else if (states.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (states.errorMessage != null) {
                Text(
                    text = states.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                if (showAlbumScreen) {
                    AlbumGrid(albums = states.albumsList, onAlbumClick = { albumName ->
                        showAlbumScreen = false
                        viewModel.loadMedia(GalleryFilter.Album(albumName))
                    })
                } else {

//                    fast way
                    CustomGalleryGrid(
//                        onZoomChange = {newCount->columnCount=newCount},
                        groupedMedia = states.groupedMedia,
                        showMemories = states.currentFilter is GalleryFilter.All,
                        mediaList = states.mediaList,
//                        columnCount=columnCount,
                        onMediaClick = { clickedMedia ->
                            selectedMediaIndex = states.mediaList.indexOf(clickedMedia)
                        })


                    //lag way
//                    LazyVerticalGrid(
//                        state = gridState,
//                        columns = GridCells.Fixed(columnCount),
//                        modifier = Modifier.fillMaxSize(),
//                        contentPadding = PaddingValues(3.dp),
//                        horizontalArrangement = Arrangement.spacedBy(2.dp),
//                        verticalArrangement = Arrangement.spacedBy(2.dp)
//                    ) {
//                        if (states.currentFilter is GalleryFilter.All && states.mediaList.isNotEmpty()) {
//                            // for ignore lines in grid and let item take maxWidth
//
//                            item(span = { GridItemSpan(maxLineSpan) }) {
//                                MemoriesBanner(mediaList = states.mediaList)
//                            }
//                        }
//                        // Header التاريخ (يأخذ عرض الشاشة بالكامل)
//
//                        states.groupedMedia.forEach { (date, mediaItems) ->
//
//                            item(
//                                span = { GridItemSpan(maxLineSpan) },
//                                key = "header_$date"
//                            ) {
//                                Text(
//                                    text = date,
//                                    style = MaterialTheme.typography.titleMedium,
//                                    color = MaterialTheme.colorScheme.onSurface,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
//                                        .padding(
//                                            horizontal = 8.dp, vertical = 12.dp
//                                        )
//                                        .animateItem()
//                                )
//                            }
//                            //gridState
//                            // الصور كعناصر مستقلة (عشان تطير وتعيد ترتيب نفسها)
//                            items(
//                                items = mediaItems,
//                                key = { it.id }// الـ ID هو اللي بيعرف Compose كل صورة عشان ينقلها لمكانها الجديد
//                                ,
//                                span = { mediaItems ->
//                                    // القاعدة اللي أنت وضحتها:
//                                    // لو الأعمدة 3 فما فوق (لحد 8) وأول صورة -> تاخد عمودين
//                                    // لو الأعمدة 1 أو 2 -> كل الصور تاخد عمود واحد طبيعي
//                                    val isFirstItem = mediaItems == states.mediaList.first()
//                                    if (isFirstItem && columnCount >= 3) {
//                                        GridItemSpan(2)
//                                    } else {
//                                        GridItemSpan(1)
//                                    }
//                                })
//                            { mediaItem ->
//                                MediaItemCard(
//                                    mediaItem = mediaItem,
//                                    onClick = {
//                                        selectedMediaIndex = states.mediaList.indexOf(mediaItem)
//
//                                    },
//                                    modifier = Modifier.animateItem(
//                                        fadeInSpec = null,
//                                        fadeOutSpec = null,
//                                        placementSpec = spring(
//                                            dampingRatio = Spring.DampingRatioLowBouncy,
//                                            stiffness = Spring.StiffnessLow
//                                        )
//                                    )
//                                )
//                            }
//
//                        }
//                    }
                }
            }
            TopAppBar(
                title = {
                    if (showAlbumScreen) {
                        Text(text = "Albums", color = Color.White)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            Icons.Default.Menu,
                            "Menu",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, "Options", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = .7f), Color.Transparent
                            )
                        )
                    )

//
            )
            AnimatedVisibility(
                visible = selectedMediaIndex != null, modifier = Modifier.matchParentSize()
            ) {
                selectedMediaIndex.let { index ->
                    index?.let {
                        FullScreenViewer(
                            mediaList = states.mediaList,
                            initialIndex = index,
                            onDismiss = { selectedMediaIndex = null })
                    }

                }

            }
        }
    }

}



