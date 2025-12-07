package com.cujuvideo.maintanance.application.presentation.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.cujuvideo.maintanance.application.R
import com.cujuvideo.maintanance.application.domain.viewdatamodel.VideoViewDataModel
import com.cujuvideo.maintanance.application.presentation.Util
import com.cujuvideo.maintanance.application.presentation.viewmodel.VideosViewModel
import com.cujuvideo.maintanance.application.presentation.viewmodel.datastate.UploadState
import com.cujuvideo.maintanance.application.presentation.viewmodel.datastate.VideoDetailStateUI

/**
 * PlayVideoView
 *
 * A composable screen that displays a video player with standard media controls.
 * It provides a detailed view of the selected video and related actions:
 *
 * Displays video title, status, creation timestamp.
 * Includes an upload button for pending videos.
 * Manages the player lifecycle for smooth navigation.
 */
@Composable
fun PlayVideoView(videosViewModel: VideosViewModel, navController: NavController) {

    val videoDetailStateUi = videosViewModel.videoDetailStateUi.collectAsStateWithLifecycle()

    when (videoDetailStateUi.value) {
        VideoDetailStateUI.IDLE -> {}
        is VideoDetailStateUI.Success -> {
            val videoViewDataModel =
                (videoDetailStateUi.value as VideoDetailStateUI.Success).videoViewDataModel
            VidePlayer(videosViewModel, videoViewDataModel, navController)
        }

        is VideoDetailStateUI.Error -> {}
    }

}

/**
 * VidePlayer
 *
 * A composable function responsible for displaying a video player with standard media controls.
 * It provides a detailed view of the selected video and related actions:
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VidePlayer(
    videosViewModel: VideosViewModel,
    videoDetail: VideoViewDataModel,
    navController: NavController
) {
    val filePath = videoDetail.internalFilePath
    val context = LocalContext.current
    val timeStamp = Util.extractTimeStampAndGetTimeStamp(filePath)
    val bitmap = Util.generateVideoThumbnail(filePath)
    var showThumbnailOverlay by remember { mutableStateOf(true) }
    var showPlayerView by remember { mutableStateOf(true) }
    var isPlayerReleased by remember { mutableStateOf(false) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = filePath.toUri()
            val mediaItem = androidx.media3.common.MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = false

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY && playWhenReady) {
                        showThumbnailOverlay = false
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        showThumbnailOverlay = false
                    }
                }
            })
        }
    }

    val releaseAndNavigate: () -> Unit = {
        showPlayerView = false

        exoPlayer.stop()
        exoPlayer.clearMediaItems()
        exoPlayer.release()
        isPlayerReleased = true

        navController.popBackStack()
    }

    BackHandler(enabled = true) {
        releaseAndNavigate()
    }

    Column {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = videoDetail.title,
                                fontSize = 14.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Created at $timeStamp",
                                fontSize = 13.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = videoDetail.status.name,
                                fontSize = 12.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            releaseAndNavigate()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Player",
                            )
                        }
                    },
                    actions = {
                        if (videoDetail.status != UploadState.UPLOADED) {
                            IconButton(onClick = {
                                videosViewModel.uploadVideo(videoDetail.videoId)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Upload,
                                    contentDescription = "Upload Video"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.LightGray,
                        titleContentColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            if (showPlayerView) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = true
                        }
                    },
                    modifier = Modifier.padding(paddingValues)
                )

                if (showThumbnailOverlay && bitmap != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                exoPlayer.playWhenReady = true
                                showThumbnailOverlay = false
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = stringResource(id = R.string.image_content_description_thumbnail),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Icon(
                            imageVector = Icons.Default.PlayCircleFilled,
                            contentDescription = stringResource(id = R.string.image_content_description_play),
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(72.dp)
                        )
                    }
                }
            }
        }
    }

    DisposableEffect(key1 = exoPlayer) {
        onDispose { // release expo player resources if not manually released
            if (!isPlayerReleased) {
                exoPlayer.release()
            }
        }
    }
}