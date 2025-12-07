package com.cujuvideo.maintanance.application.presentation.viewmodel.datastate

/**
 * UploadState
 *
 * Enum representing the different states of a video upload.
 *
 * @property RECORDED Represents a video that has been recorded but not yet uploaded.
 * @property UPLOADED Represents a video that has been successfully uploaded.
 * @property UPLOADING Represents a video that is currently being uploaded.
 */
enum class UploadState {
    RECORDED,
    UPLOADED,
    UPLOADING
}