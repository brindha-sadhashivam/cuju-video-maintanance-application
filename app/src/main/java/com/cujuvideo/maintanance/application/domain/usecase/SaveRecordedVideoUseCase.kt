package com.cujuvideo.maintanance.application.domain.usecase

import com.cujuvideo.maintanance.application.domain.repository.VideoRepository

/**
 * SaveRecordedVideoUseCase
 *
 * Use case responsible for saving a recorded video to the local storage.
 *
 * @param videoRepository The repository for managing videos.
 */
class SaveRecordedVideoUseCase(val videoRepository: VideoRepository) {

    /**
     * Saves a recorded video to the local storage.
     *
     * @param videoFilePath The path to the recorded video file.
     */
    suspend operator fun invoke(videoFilePath: String, description: String) {
        videoRepository.saveRecordedVideo(videoFilePath, description)
    }

}