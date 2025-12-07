package com.cujuvideo.maintanance.application.domain.usecase

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.cujuvideo.maintanance.application.data.worker.VideoUploadWorker
import com.cujuvideo.maintanance.application.domain.repository.VideoRepository
import com.cujuvideo.maintanance.application.presentation.Util.KEY_VIDEO_ID
import com.cujuvideo.maintanance.application.presentation.viewmodel.datastate.UploadState

/**
 * UploadVideoUseCase
 *
 * Use case responsible for uploading a video using worker thread
 * and update the status to Local storage.
 *
 * @param videoRepository The repository for managing videos.
 * @param context The application context.
 */
class UploadVideoUseCase(
    val videoRepository: VideoRepository,
    val context: Context
) {

    /**
     * Uploads a video with the specified video ID
     * and call repository to update the status .
     *
     * @param videoId The ID of the video to be uploaded.
     */
    suspend operator fun invoke(videoId: Int) {
        videoRepository.uploadVideo(videoId, UploadState.UPLOADING)
        val inputData = Data.Builder()
            .putInt(KEY_VIDEO_ID, videoId)
            .build()

        val uploadRequest = OneTimeWorkRequestBuilder<VideoUploadWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(uploadRequest)
    }

}