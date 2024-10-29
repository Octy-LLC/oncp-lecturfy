package com.lecturfy.components

import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.activity.ComponentActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class VideoPlayerViewModel(private val context: Context, private val videoUrls: List<String>) :
    ViewModel() {
    private val player = ExoPlayer.Builder(context).build()
    private val _isFullscreen = MutableStateFlow(false)
    val isFullscreen = _isFullscreen.asStateFlow()

    init {
        val mediaItems = videoUrls.map { MediaItem.fromUri(it) }
        player.setMediaItems(mediaItems)
        player.prepare()
    }

    fun toggleFullscreen() {
        _isFullscreen.value = !_isFullscreen.value
    }

    fun getPlayer() = player

    override fun onCleared() {
        player.release()
    }
}

class VideoPlayerViewModelFactory(
    private val context: Context,
    private val videoUrls: List<String>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoPlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoPlayerViewModel(context, videoUrls) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(screenContext: ComponentActivity, videoUrls: List<String>) {
    val context = LocalContext.current
    val viewModel: VideoPlayerViewModel =
        viewModel(factory = VideoPlayerViewModelFactory(context, videoUrls))
    val isFullscreen = viewModel.isFullscreen.collectAsState()

    if (isFullscreen.value) {
        screenContext.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val dialog = Dialog(context).apply {
            setContentView(PlayerView(context).also { playerView ->
                playerView.player = viewModel.getPlayer()
                playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                playerView.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                playerView.setFullscreenButtonClickListener {
                    viewModel.toggleFullscreen()
                }
                playerView.setShowNextButton(true)
                playerView.setShowPreviousButton(true)
            })
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            window?.setBackgroundDrawableResource(android.R.color.black)
            show()
        }
        DisposableEffect(Unit) {
            onDispose {
                dialog.dismiss()
            }
        }
    } else {
        screenContext.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = viewModel.getPlayer()
                    setShowNextButton(true)
                    setShowPreviousButton(true)
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                    setFullscreenButtonClickListener {
                        viewModel.toggleFullscreen()
                    }
                }
            }, modifier = Modifier.background(Color.Black)
        )
    }
}


