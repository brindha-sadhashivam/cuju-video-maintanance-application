package com.cujuvideo.maintanance.application.domain.repository

import com.cujuvideo.maintanance.application.data.model.VideoDataModel
import com.cujuvideo.maintanance.application.presentation.viewmodel.datastate.UploadState
import kotlinx.coroutines.flow.Flow

/**
 * VideoRepository
 *
 * Interface representing the repository for managing videos.
 *
 * @property fetchVideos Fetches a list of videos.
 * @property saveRecordedVideo Saves a recorded video to the local storage.
 * @property fetchVideoDetail Fetches a video's detail.
 * @property uploadVideo Uploads a video with the specified video ID.
 */
interface VideoRepository {

    /**
     * Fetches a list of videos.
     *
     * @return A [Flow] emitting a list of [VideoDataModel].
     */
    fun fetchVideos(): Flow<List<VideoDataModel>>

    /**
     * Saves a recorded video to the local storage.
     */
    suspend fun saveRecordedVideo(filePath: String, description: String)

    /**
     * Fetches a video's detail.
     */
    fun fetchVideoDetail(videoId: Int): Flow<VideoDataModel?>

    /**
     * Uploads a video with the specified video ID.
     */
    suspend fun uploadVideo(videoId: Int, uploadState: UploadState)

}