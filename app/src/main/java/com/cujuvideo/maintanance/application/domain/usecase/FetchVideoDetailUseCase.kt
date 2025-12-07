package com.cujuvideo.maintanance.application.domain.usecase

import com.cujuvideo.maintanance.application.domain.mapper.VideoDataViewMapper
import com.cujuvideo.maintanance.application.domain.repository.VideoRepository
import com.cujuvideo.maintanance.application.domain.viewdatamodel.VideoViewDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * FetchVideoDetailUseCase
 *
 * Use case responsible for fetching a video's detail.
 *
 * @param videoRepository The repository for managing videos.
 * @param videoDataViewMapper The mapper to transform video entities to view data models.
 */
class FetchVideoDetailUseCase(
    val videoRepository: VideoRepository,
    val videoDataViewMapper: VideoDataViewMapper
) {

    /**
     * Fetches a video's detail.
     *
     * @param videoId The ID of the video to fetch.
     */
    operator fun invoke(videoId: Int): Flow<VideoViewDataModel> {
        return videoRepository.fetchVideoDetail(videoId).map { dataModel ->
            dataModel?.let {
                videoDataViewMapper.transform(dataModel)
            } ?: throw Exception("Video not found")
        }
    }
}