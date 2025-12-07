package com.cujuvideo.maintanance.application.presentation.viewmodel.datastate

import androidx.annotation.StringRes
import com.cujuvideo.maintanance.application.R
import com.cujuvideo.maintanance.application.domain.viewdatamodel.VideoViewDataModel

/**
 * FetchVideoStateUI
 *
 * Sealed class representing the state of fetching a list of videos.
 *
 * @property Loading Represents the state when fetching the video list is in progress.
 * @property Success Represents the state when the video list is successfully fetched.
 * @property Error Represents the state when an error occurs during fetching the video list.
 */
sealed class FetchVideoStateUI {
    object Loading : FetchVideoStateUI()
    data class Success(val videoList: List<VideoViewDataModel>) : FetchVideoStateUI()
    data class Error(
        val message: String? = null,
        @StringRes val defaultResId: Int = R.string.error_with_message
    ) : FetchVideoStateUI()
}