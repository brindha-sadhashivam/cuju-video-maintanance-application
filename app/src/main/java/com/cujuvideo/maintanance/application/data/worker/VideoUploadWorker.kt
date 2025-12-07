package com.cujuvideo.maintanance.application.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cujuvideo.maintanance.application.domain.repository.VideoRepository
import com.cujuvideo.maintanance.application.presentation.Util.KEY_VIDEO_ID
import com.cujuvideo.maintanance.application.presentation.Util.VIDEO_UPLOAD_DELAY
import com.cujuvideo.maintanance.application.presentation.viewmodel.datastate.UploadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * VideoUploadWorker
 *
 * Worker class responsible for uploading a video.
 *
 * @param context The application context.
 * @param workerParams The worker parameters.
 * @param videoRepository The repository for managing videos.
 */
class VideoUploadWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val videoRepository: VideoRepository
) : CoroutineWorker(context, workerParams) {

    /**
     * Executes the worker to upload a video.
     *
     * @return The result of the worker execution.
     */
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val videoId = inputData.getInt(KEY_VIDEO_ID, -1)
        try {
            delay(VIDEO_UPLOAD_DELAY)
            videoRepository.uploadVideo(videoId, UploadState.UPLOADED)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}