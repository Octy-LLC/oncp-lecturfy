package com.lecturfy.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay

@Composable
fun AudioPlayer(
    playList: List<Audio>,
    primaryColor: Color = Color.Black,
    primaryActiveColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    secondaryColor: Color = Color.Gray,
    controlScale: Float = 1f,
    width: Dp? = null,
    height: Dp? = null,
    outerPadding: Dp = 16.dp,
    showTitle: Boolean = true,
    showSlider: Boolean = true,
    showPreviousNextControls: Boolean = true,
    autoPlay: Boolean = false
) {
    val context = LocalContext.current
    val player = remember { ExoPlayer.Builder(context).build() }
    DisposableEffect(player) { onDispose { player.release() } }

    val playingSongIndex = remember { mutableIntStateOf(0) }
    val isPlaying = remember { mutableStateOf(false) }
    val shouldCheckTime = remember { mutableStateOf(true) }
    val currentPosition = remember { mutableLongStateOf(0) }
    val sliderPosition = remember { mutableLongStateOf(0) }
    val totalDuration = remember { mutableLongStateOf(0) }

    LaunchedEffect(player.currentMediaItemIndex) {
        playingSongIndex.intValue = player.currentMediaItemIndex
    }

    LaunchedEffect(Unit) {
        playList.forEach {
            val mediaItem = MediaItem.fromUri(Uri.parse(it.url))
            player.addMediaItem(mediaItem)
        }
        player.prepare()
        if (autoPlay) {
            player.play()
            isPlaying.value = true
        }
    }

    LaunchedEffect(player.currentPosition, player.isPlaying) {
        currentPosition.longValue = player.currentPosition
        sliderPosition.longValue = currentPosition.longValue
    }


    DisposableEffect(player) {
        shouldCheckTime.value = true
        onDispose { shouldCheckTime.value = false }
    }
    LaunchedEffect(shouldCheckTime) {
        while (shouldCheckTime.value) {
            delay(50)
            currentPosition.longValue = player.currentPosition
            sliderPosition.longValue = currentPosition.longValue
        }
    }

    LaunchedEffect(player.duration) {
        if (player.duration > 0) {
            totalDuration.longValue = player.duration
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(backgroundColor)
            .let { modifier ->
                var updatedModifier = modifier
                if (width != null) {
                    updatedModifier = updatedModifier.width(width)
                }
                if (height != null) {
                    updatedModifier = updatedModifier.height(height)
                }
                updatedModifier
            }
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(outerPadding)
        ) {
            if (showTitle) {
                TrackTitle(
                    playList = playList,
                    playingSongIndex = playingSongIndex.value,
                    primaryColor = primaryColor
                )
            }
            if (showSlider) {
                TrackSlider(
                    value = sliderPosition.longValue.toFloat(),
                    onValueChange = {
                        sliderPosition.longValue = it.toLong()
                    },
                    onValueChangeFinished = {
                        currentPosition.longValue = sliderPosition.longValue
                        player.seekTo(sliderPosition.longValue)
                    },
                    songDuration = totalDuration.longValue.toFloat(),
                    primaryColor = primaryColor,
                    primaryActiveColor = primaryActiveColor,
                    secondaryColor = secondaryColor
                )
                TrackTime(
                    currentPosition = currentPosition.longValue,
                    totalDuration = totalDuration.longValue,
                    primaryColor = primaryColor
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showPreviousNextControls && playList.size > 1) {
                    ControlButton(
                        icon = Icons.Filled.SkipPrevious,
                        size = 40.dp,
                        scale = controlScale,
                        primaryColor = primaryColor,
                        onClick = {
                            val previousIndex =
                                if (playingSongIndex.value == 0) playList.size - 1 else playingSongIndex.value - 1
                            player.seekTo(previousIndex, 0)
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
                ControlButton(
                    icon = if (isPlaying.value) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    size = 50.dp,
                    onClick = {
                        if (isPlaying.value) {
                            player.pause()
                        } else {
                            player.play()
                        }
                        isPlaying.value = player.isPlaying
                    },
                    primaryColor = primaryColor,
                    scale = controlScale
                )
                if (showPreviousNextControls && playList.size > 1) {
                    Spacer(modifier = Modifier.width(10.dp))
                    ControlButton(
                        icon = Icons.Filled.SkipNext,
                        size = 40.dp,
                        scale = controlScale,
                        primaryColor = primaryColor,
                        onClick = {
                            val nextIndex =
                                if (playingSongIndex.value == playList.size - 1) 0 else playingSongIndex.value + 1
                            player.seekTo(nextIndex, 0)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AudioPlayerSmall(
    playList: List<Audio>,
    primaryColor: Color = Color.Black,
    primaryActiveColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    secondaryColor: Color = Color.Gray,
    controlScale: Float = 1f,
    width: Dp? = null,
    height: Dp? = null,
    outerPadding: Dp = 16.dp,
    showTitle: Boolean = true,
    showSlider: Boolean = true,
    autoPlay: Boolean = false
) {
    val context = LocalContext.current
    val player = remember { ExoPlayer.Builder(context).build() }
    DisposableEffect(player) { onDispose { player.release() } }

    val playingSongIndex = remember { mutableIntStateOf(0) }
    val isPlaying = remember { mutableStateOf(false) }
    val shouldCheckTime = remember { mutableStateOf(true) }
    val currentPosition = remember { mutableLongStateOf(0) }
    val sliderPosition = remember { mutableLongStateOf(0) }
    val totalDuration = remember { mutableLongStateOf(0) }

    LaunchedEffect(player.currentMediaItemIndex) {
        playingSongIndex.intValue = player.currentMediaItemIndex
    }

    LaunchedEffect(Unit) {
        playList.forEach {
            val mediaItem = MediaItem.fromUri(Uri.parse(it.url))
            player.addMediaItem(mediaItem)
        }
        player.prepare()
        if (autoPlay) {
            player.play()
            isPlaying.value = true
        }
    }

    LaunchedEffect(player.currentPosition, player.isPlaying) {
        currentPosition.longValue = player.currentPosition
        sliderPosition.longValue = currentPosition.longValue
    }


    DisposableEffect(player) {
        shouldCheckTime.value = true
        onDispose { shouldCheckTime.value = false }
    }
    LaunchedEffect(shouldCheckTime) {
        while (shouldCheckTime.value) {
            delay(50)
            currentPosition.longValue = player.currentPosition
            sliderPosition.longValue = currentPosition.longValue
        }
    }

    LaunchedEffect(player.duration) {
        if (player.duration > 0) {
            totalDuration.longValue = player.duration
        }
    }

    Row(
        modifier = Modifier
            .background(backgroundColor)
            .padding(outerPadding)
            .let { modifier ->
                var updatedModifier = modifier
                if (width != null) {
                    updatedModifier = updatedModifier.width(width)
                }
                if (height != null) {
                    updatedModifier = updatedModifier.height(height)
                }

                updatedModifier
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ControlButton(
            icon = if (isPlaying.value) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            size = 100.dp,
            onClick = {
                if (isPlaying.value) {
                    player.pause()
                } else {
                    player.play()
                }
                isPlaying.value = player.isPlaying
            },
            primaryColor = primaryColor,
            scale = controlScale
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (showTitle) {
                TrackTitle(
                    playList = playList,
                    playingSongIndex = playingSongIndex.value,
                    primaryColor = primaryColor,
                    padding = 5.dp
                )
            }
            if (showSlider) {
                TrackSlider(
                    value = sliderPosition.longValue.toFloat(),
                    onValueChange = {
                        sliderPosition.longValue = it.toLong()
                    },
                    onValueChangeFinished = {
                        currentPosition.longValue = sliderPosition.longValue
                        player.seekTo(sliderPosition.longValue)
                    },
                    songDuration = totalDuration.longValue.toFloat(),
                    primaryColor = primaryColor,
                    primaryActiveColor = primaryActiveColor,
                    secondaryColor = secondaryColor
                )
                TrackTime(
                    currentPosition = currentPosition.longValue,
                    totalDuration = totalDuration.longValue,
                    primaryColor = primaryColor,
                    padding = 2.dp
                )
            }
        }
    }
}

@Composable
fun ControlButton(
    icon: ImageVector,
    size: Dp,
    primaryColor: Color,
    scale: Float,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size * scale)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size((size / 1.5f) * scale),
            imageVector = icon,
            tint = primaryColor,
            contentDescription = null
        )
    }
}


@Composable
fun TrackTitle(
    playList: List<Audio>,
    playingSongIndex: Int,
    primaryColor: Color,
    padding: Dp = 3.dp
) {
    playList.getOrNull(playingSongIndex)?.let { currentSong ->
        Text(
            text = currentSong.title ?: "",
            modifier = Modifier.padding(vertical = padding),
            color = primaryColor,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun TrackTime(
    currentPosition: Long,
    totalDuration: Long,
    primaryColor: Color,
    padding: Dp = 8.dp
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = currentPosition.convertToText(),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            color = primaryColor,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        val remainTime = totalDuration - currentPosition
        Text(
            text = if (remainTime >= 0) remainTime.convertToText() else "",
            modifier = Modifier.padding(padding),
            color = primaryColor,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun TrackSlider(
    value: Float,
    onValueChange: (newValue: Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    songDuration: Float,
    primaryColor: Color,
    primaryActiveColor: Color,
    secondaryColor: Color
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        valueRange = 0f..songDuration,
        colors = SliderDefaults.colors(
            thumbColor = primaryColor,
            activeTrackColor = primaryActiveColor,
            inactiveTrackColor = secondaryColor
        )
    )
}

private fun Long.convertToText(): String {
    val sec = this / 1000
    val minutes = sec / 60
    val seconds = sec % 60

    val minutesString = if (minutes < 10) "0$minutes" else minutes.toString()
    val secondsString = if (seconds < 10) "0$seconds" else seconds.toString()

    return "$minutesString:$secondsString"
}

data class Audio(
    val title: String,
    val url: String
)
