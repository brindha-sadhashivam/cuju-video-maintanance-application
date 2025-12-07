package com.cujuvideo.maintanance.application.presentation.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.cujuvideo.maintanance.application.R
import com.cujuvideo.maintanance.application.domain.viewdatamodel.VideoViewDataModel
import com.cujuvideo.maintanance.application.presentation.Util
import com.cujuvideo.maintanance.application.presentation.Util.PERMISSION_REQUEST_SCREEN_ROUTE_KEY
import com.cujuvideo.maintanance.application.presentation.viewmodel.VideosViewModel
import com.cujuvideo.maintanance.application.presentation.viewmodel.datastate.FetchVideoStateUI
import org.koin.androidx.compose.koinViewModel

/**
 * VideosListView
 *
 * Composable function responsible for displaying a list of videos.
 *
 * @param navController The NavController used for navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideosListView(navController: NavController) {

    val videosViewModel: VideosViewModel = koinViewModel()
    val videoStateList = videosViewModel.fetchVideStateUi.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.title_video_list),
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.LightGray,
                titleContentColor = Color.White
            )
        )
    }, floatingActionButton = {
        Box(modifier = Modifier.padding(16.dp)) {
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                containerColor = Color.LightGray,
                onClick = { navController.navigate(PERMISSION_REQUEST_SCREEN_ROUTE_KEY) }) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    text = stringResource(id = R.string.record_video_text)
                )
            }
        }
    }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {

            when (videoStateList.value) {
                is FetchVideoStateUI.Loading -> {
                    Text(
                        text = stringResource(R.string.screen_loading_text),
                        color = Color.Black,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is FetchVideoStateUI.Success -> {
                    val videoList = (videoStateList.value as FetchVideoStateUI.Success).videoList
                    if (videoList.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_video_found_text),
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn {
                            items(videoList, key = { videoItem -> videoItem.videoId }) { data ->
                                VideoItem(data, navController)
                            }
                        }
                    }
                }

                is FetchVideoStateUI.Error -> {
                    val error = (videoStateList.value as FetchVideoStateUI.Error).message
                    Text(
                        text = error ?: stringResource(R.string.no_video_found_text),
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun VideoItem(
    videoList: VideoViewDataModel,
    navController: NavController
) {
    val filePath = videoList.internalFilePath
    val videoId = videoList.videoId
    val bitmap = Util.generateVideoThumbnail(filePath)?.asImageBitmap()
    val timeStamp = Util.extractTimeStampAndGetTimeStamp(filePath)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp)
            .clickable(
                onClick = {
                    navController.navigate("VideoPlayer/$videoId")
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 60.dp)
                .aspectRatio(16f / 9f),
            contentAlignment = Alignment.TopStart,
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = stringResource(R.string.image_content_description_thumbnail),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.BrokenImage,
                    contentDescription = stringResource(R.string.image_content_description_no_thumbnail),
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
            }
            Icon(
                imageVector = Icons.Default.PlayCircleFilled,
                contentDescription = stringResource(R.string.image_content_description_play),
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = videoList.title
            )
            Text(
                text =stringResource(R.string.created_time_text, timeStamp)
            )
            Text(
                text = videoList.status.name,
                fontWeight = FontWeight.Bold
            )
        }
    }

    HorizontalDivider(
        thickness = 0.5.dp,
        color = Color.LightGray,
        modifier = Modifier.padding(8.dp)
    )
}

