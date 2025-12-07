package com.cujuvideo.maintanance.application.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cujuvideo.maintanance.application.domain.usecase.FetchVideoDetailUseCase
import com.cujuvideo.maintanance.application.domain.usecase.FetchVideoListUseCase
import com.cujuvideo.maintanance.application.domain.usecase.SaveRecordedVideoUseCase
import com.cujuvideo.maintanance.application.domain.usecase.UploadVideoUseCase
import com.cujuvideo.maintanance.application.presentation.Util.STATEFLOW_STOP_TIMEOUT_MS
import com.cujuvideo.maintanance.application.presentation.Util.VIDEO_ID_ARG
import com.cujuvideo.maintanance.application.presentation.viewmodel.datastate.FetchVideoStateUI
import com.cujuvideo.maintanance.application.presentation.viewmodel.datastate.VideoDetailStateUI
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

/**
 * VideosViewModel
 *
 * ViewModel responsible for managing the state and business logic related to videos.
 *
 * @param fetchUseCase The use case for fetching a list of videos.
 * @param saveRecordedVideoUseCase The use case for saving a recorded video.
 * @param fetchVideoDetailUseCase The use case for fetching video details.
 * @param uploadVideoUseCase The use case for uploading a video.
 * @param videoDir The directory where recorded videos are stored.
 * @param savedStateHandle The saved state handle used for passing data between destinations.
 */
class VideosViewModel(
    fetchUseCase: FetchVideoListUseCase,
    val saveRecordedVideoUseCase: SaveRecordedVideoUseCase,
    fetchVideoDetailUseCase: FetchVideoDetailUseCase,
    val uploadVideoUseCase: UploadVideoUseCase,
    val videoDir: File,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val videoId = savedStateHandle.get<Int>(VIDEO_ID_ARG) ?: 0

    val videoDetailStateUi: StateFlow<VideoDetailStateUI> =
        fetchVideoDetailUseCase(videoId).map { videoDetail ->
            VideoDetailStateUI.Success(videoDetail)
        }.catch {
            VideoDetailStateUI.Error(it.message)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATEFLOW_STOP_TIMEOUT_MS),
            initialValue = VideoDetailStateUI.IDLE
        )

    val fetchVideStateUi: StateFlow<FetchVideoStateUI> =
        fetchUseCase().map { videoList -> FetchVideoStateUI.Success(videoList) }
            .catch { FetchVideoStateUI.Error(it.message) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STATEFLOW_STOP_TIMEOUT_MS),
                initialValue = FetchVideoStateUI.Loading
            )

    /**
     * Saves a recorded video to the video directory.
     *
     * @param filePath The path to the recorded video file.
     * @param description The description associated with the video.
     */
    fun saveRecordedVideo(filePath: String, description: String) {
        viewModelScope.launch {
            saveRecordedVideoUseCase(filePath, description)
        }
    }

    /**
     * Uploads a video with the specified video ID.
     *
     * @param videoId The ID of the video to be uploaded.
     */
    fun uploadVideo(videoId: Int) {
        viewModelScope.launch {
            uploadVideoUseCase(videoId)
        }
    }
}