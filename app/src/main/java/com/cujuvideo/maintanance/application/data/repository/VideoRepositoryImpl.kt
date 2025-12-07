package com.cujuvideo.maintanance.application.data.repository

import com.cujuvideo.maintanance.application.data.datasource.dao.VideoDao
import com.cujuvideo.maintanance.application.data.entity.VideoEntity
import com.cujuvideo.maintanance.application.data.entity.mapper.VideoDataMapper
import com.cujuvideo.maintanance.application.data.model.VideoDataModel
import com.cujuvideo.maintanance.application.domain.repository.VideoRepository
import com.cujuvideo.maintanance.application.presentation.viewmodel.datastate.UploadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * VideoRepository
 *
 * Repository class responsible for managing videos.
 *
 * @param videoDao The DAO for video operations.
 * @param videoDataMapper The mapper to transform video entities to data models.
 */
class VideoRepositoryImpl(val videoDao: VideoDao, val videoDataMapper: VideoDataMapper) :
    VideoRepository {

    /**
     * Fetches a list of videos.
     *
     * @return A [Flow] emitting a list of [VideoDataModel].
     */
    override fun fetchVideos(): Flow<List<VideoDataModel>> {
        return videoDao.getAllVideos().map { it ->
            videoDataMapper.transform(it)
        }
    }

    /**
     * Saves a recorded video to the local storage.
     *
     * @param filePath The path to the recorded video file.
     * @param description The description associated with the video.
     */
    override suspend fun saveRecordedVideo(filePath: String, description: String) {
        val videoEntity = VideoEntity(
            title = description,
            internalFilePath = filePath,
        )
        videoDao.insertVideo(videoEntity)
    }

    /**
     * Fetches a video's detail.
     *
     * @param videoId The ID of the video to fetch.
     * @return A [Flow] emitting a [VideoDataModel] or null if not found.
     */
    override fun fetchVideoDetail(videoId: Int): Flow<VideoDataModel?> {
        return videoDao.getVideoById(videoId).map { entity ->
            entity?.let {
                videoDataMapper.transform(it)
            } ?: throw Exception("Video not found")
        }
    }

    /**
     * Uploads a video with the specified video ID.
     *
     * @param videoId The ID of the video to be uploaded.
     * @param uploadState The upload state of the video.
     */
    override suspend fun uploadVideo(videoId: Int, uploadState: UploadState) {
        videoDao.updateVideoStatus(videoId, uploadState)
    }
}