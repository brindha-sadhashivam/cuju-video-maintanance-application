package com.cujuvideo.maintanance.application.domain.usecase

import com.cujuvideo.maintanance.application.domain.mapper.VideoDataViewMapper
import com.cujuvideo.maintanance.application.domain.repository.VideoRepository
import com.cujuvideo.maintanance.application.domain.viewdatamodel.VideoViewDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * FetchVideoListUseCase
 *
 * Use case responsible for fetching a list of videos.
 *
 * @param videoRepository The repository for managing videos.
 * @param videoDataViewMapper The mapper to transform video entities to view data models.
 */
class FetchVideoListUseCase(
    val videoRepository: VideoRepository,
    val videoDataViewMapper: VideoDataViewMapper
) {

    /**
     * Fetches a list of videos.
     *
     * @return A [Flow] emitting a list of [VideoViewDataModel].
     */
    operator fun invoke(): Flow<List<VideoViewDataModel>> {
        return videoRepository.fetchVideos().map {
            videoDataViewMapper.transform(it)
        }
    }
}