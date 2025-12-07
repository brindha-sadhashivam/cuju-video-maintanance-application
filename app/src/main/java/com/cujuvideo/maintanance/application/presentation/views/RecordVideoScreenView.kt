package com.cujuvideo.maintanance.application.presentation.views

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.cujuvideo.maintanance.application.R
import com.cujuvideo.maintanance.application.presentation.Util.EMPTY_STRING
import com.cujuvideo.maintanance.application.presentation.Util.FETCH_VIDEO_VIEW_ROUTE_KEY
import com.cujuvideo.maintanance.application.presentation.viewmodel.VideosViewModel
import org.koin.androidx.compose.koinViewModel
import java.io.File

/**
 * RecordVideoScreenView
 *
 * Composable function responsible for displaying the video recording screen.
 * Once the recording is complete, a popup is displayed to save the video title.
 */
@Composable
fun RecordVideoScreenView(navController: NavController) {
    val videoViewModel: VideosViewModel = koinViewModel()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var activeRecording: Recording? by remember { mutableStateOf(null) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    // State to manage the Post-Recording Dialog visibility
    var showVideoTitleDialog: Boolean by remember { mutableStateOf(false) }
    var lastRecordedFilePath: String? by remember { mutableStateOf(null) }

    val recorder = remember {
        val qualitySelector = QualitySelector.from(
            Quality.HD,
            FallbackStrategy.higherQualityOrLowerThan(Quality.HD)
        )
        Recorder.Builder().setQualitySelector(qualitySelector).build()
    }

    val videoCapture = remember { VideoCapture.withOutput(recorder) }
    val hasAudioPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    Box {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            videoCapture
                        )
                    } catch (exc: Exception) {
                        Log.e("Camera Recording", "An error occured", exc)
                    }
                }, executor)
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        Button(
            onClick = {
                if (activeRecording == null) {
                    val file =
                        File(
                            videoViewModel.videoDir,
                            "${System.currentTimeMillis()}.mp4"
                        )
                    val outputOptions = FileOutputOptions.Builder(file).build()
                    var pendingRecording = videoCapture.output
                        .prepareRecording(context, outputOptions)

                    // Explicitly check permission before enabling audio
                    if (hasAudioPermission) {
                        pendingRecording = pendingRecording.withAudioEnabled()
                    }
                    activeRecording =
                        pendingRecording.start(ContextCompat.getMainExecutor(context)) { event ->
                            if (event is VideoRecordEvent.Finalize) {
                                if (event.hasError()) {
                                    Log.e("Camera Recording", "An error occurred")
                                } else {
                                    lastRecordedFilePath = file.absolutePath
                                    showVideoTitleDialog = true
                                }
                                activeRecording = null
                            }
                        }
                } else {
                    activeRecording?.stop()
                }
            }, modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(60.dp)
        ) {
            Icon(
                imageVector = if (activeRecording == null) Icons.Default.Videocam else Icons.Default.Stop,
                contentDescription = null
            )
        }

        if (showVideoTitleDialog && lastRecordedFilePath != null) {
            VideoFileNameDialog(
                onDismiss = {
                    showVideoTitleDialog = false
                    lastRecordedFilePath = null
                },
                onSaveDescription = { description ->
                    lastRecordedFilePath?.let { filePath ->
                        videoViewModel.saveRecordedVideo(filePath, description)
                    }
                    showVideoTitleDialog = false
                    lastRecordedFilePath = null
                    navController.navigate(FETCH_VIDEO_VIEW_ROUTE_KEY) {
                        popUpTo(FETCH_VIDEO_VIEW_ROUTE_KEY) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

/**
 * VideoFileNameDialog
 *
 * Composable function responsible for displaying a dialog for getting the title for a video
 */
@Composable
fun VideoFileNameDialog(
    onDismiss: () -> Unit,
    onSaveDescription: (String) -> Unit
) {
    var descriptionText by remember { mutableStateOf(EMPTY_STRING) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.dialog_title_video_captured_text)) },
        text = {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = descriptionText,
                    onValueChange = { descriptionText = it },
                    label = { Text(stringResource(R.string.edit_box_file_name_placeholder)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSaveDescription(descriptionText) }) {
                Text(stringResource(R.string.save_text))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_text))
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}