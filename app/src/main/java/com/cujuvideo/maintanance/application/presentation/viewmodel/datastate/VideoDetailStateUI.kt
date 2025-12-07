package com.cujuvideo.maintanance.application.presentation.viewmodel.datastate

import androidx.annotation.StringRes
import com.cujuvideo.maintanance.application.R
import com.cujuvideo.maintanance.application.domain.viewdatamodel.VideoViewDataModel

/**
 * VideoDetailStateUI
 *
 * Sealed class representing the state of fetching video details.
 *
 * @property IDLE Represents the initial state.
 * @property Success Represents the state when video details are successfully fetched.
 * @property Error Represents the state when an error occurs during fetching video details.
 */
sealed class VideoDetailStateUI() {
    object IDLE : VideoDetailStateUI()
    data class Success(val videoViewDataModel: VideoViewDataModel) : VideoDetailStateUI()
    data class Error(
        val message: String? = null,
        @StringRes val defaultResId: Int = R.string.no_video_found_text
    ) : VideoDetailStateUI()
}