package com.cujuvideo.maintanance.application.data.model

import com.cujuvideo.maintanance.application.presentation.viewmodel.datastate.UploadState

/**
 * VideoDataModel
 *
 * Data class representing a video's data model.
 *
 * @property videoId The ID of the video.
 * @property title The title of the video.
 * @property status The status of the video.
 * @property internalFilePath The internal file path of the video.
 */
data class VideoDataModel(
    val videoId: Int,
    val title: String,
    val status: UploadState,
    val internalFilePath: String
)