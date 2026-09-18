package com.moussa79m.sgallary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moussa79m.data.MediaRepositoryImp
import com.moussa79m.domain.GalleryUseCases
import com.moussa79m.domain.UseCases.CopyMediaUseCase
import com.moussa79m.domain.UseCases.CreateAlbumUseCase
import com.moussa79m.domain.UseCases.DeleteMediaUseCase
import com.moussa79m.domain.UseCases.GetAlbumsUseCase
import com.moussa79m.domain.UseCases.GetImagesOnlyUseCase
import com.moussa79m.domain.UseCases.GetMediaUseCase
import com.moussa79m.domain.UseCases.GetVideosOnlyUseCase
import com.moussa79m.domain.UseCases.MoveMediaUseCase
import com.moussa79m.domain.UseCases.getMediaByAlbum
import com.moussa79m.presentation.UI.Screens.GalleryScreen
import com.moussa79m.presentation.ViewModel.GalleryViewModel
import com.moussa79m.sgallary.ui.theme.SGallaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val context=applicationContext
        val repository= MediaRepositoryImp(context)
        val useCases= GalleryUseCases(
            getImagesOnly = GetImagesOnlyUseCase(repository),
            getVideosOnly = GetVideosOnlyUseCase(repository),
            getAllMedia = GetMediaUseCase(repository),
            getAlbumsUseCase = GetAlbumsUseCase(repository),
            deleteMedia = DeleteMediaUseCase(repository),
            getMediaByAlbumsUseCase = getMediaByAlbum(repository),
            deleteMediaUseCase = DeleteMediaUseCase(repository),
            createAlbumUseCase = CreateAlbumUseCase(repository),
            copyMediaUseCase = CopyMediaUseCase(repository),
            moveMediaUseCase = MoveMediaUseCase(repository)

        )
        val factory=object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(GalleryViewModel::class.java)){

                    return GalleryViewModel(useCases) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
        setContent {
            SGallaryTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                            val viewModel: GalleryViewModel= viewModel(factory=factory)

                    GalleryScreen(viewModel =viewModel )
                }
            }
        }
    }
}
