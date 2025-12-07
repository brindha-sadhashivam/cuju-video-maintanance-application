package com.cujuvideo.maintanance.application.domain.viewdatamodel

import com.cujuvideo.maintanance.application.presentation.viewmodel.datastate.UploadState

/**
 * VideoViewDataModel
 *
 * Data class representing a video's view data model.
 *
 * @property videoId The unique identifier of the video.
 * @property title The title of the video.
 * @property status The current status of the video (e.g. RECORDED, UPLOADING).
 * @property internalFilePath The path to the video file on the device.
 */
data class VideoViewDataModel(
    val videoId: Int,
    val title: String,
    val status: UploadState,
    val internalFilePath: String
)
