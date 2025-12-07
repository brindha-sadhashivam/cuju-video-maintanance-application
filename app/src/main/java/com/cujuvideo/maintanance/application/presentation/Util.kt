package com.cujuvideo.maintanance.application.presentation

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.util.Size
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility class providing common helper methods and application-wide constants.
 *
 * This stateless class centralizes reusable logic such as date formatting,
 * video thumbnail generation.
 */
object Util {
    const val EMPTY_STRING = ""
    const val FETCH_VIDEO_VIEW_ROUTE_KEY = "FetchVideoView"
    const val RECORD_VIDEO_SCREEN_ROUTE_KEY = "RecordVideoScreenView"
    const val PERMISSION_REQUEST_SCREEN_ROUTE_KEY = "PermissionRequestScreenView"
    const val VIDEO_PLAYER_SCREEN_ROUTE_KEY = "VideoPlayer/{id}"
    const val VIDEO_ID_ARG = "id"
    const val SDF_VIDEO_TIMESTAMP_FORMAT = "MMM dd, yyyy HH:mm a"
    const val DEFAULT_THUMBNAIL_WIDTH = 1280
    const val DEFAULT_THUMBNAIL_HEIGHT = 720
    const val STATEFLOW_STOP_TIMEOUT_MS = 5000L
    const val VIDEO_UPLOAD_DELAY = 3000L
    const val KEY_VIDEO_ID = "videoId"

    /**
     * Generates a video thumbnail from the provided video file path.
     *
     * @param filePath The path to the video file.
     * @return A [Bitmap] representing the video thumbnail, or null if an error occurs.
     */
    fun generateVideoThumbnail(filePath: String): Bitmap? {
        return try {
            // Provide the constants here
            ThumbnailUtils.createVideoThumbnail(
                File(filePath), Size(
                    DEFAULT_THUMBNAIL_WIDTH,
                    DEFAULT_THUMBNAIL_HEIGHT
                ), null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Extracts the timestamp from the video file name and formats it.
     *
     * @param filePath The path to the video file.
     * @return A formatted string which represents the created timestamp.
     */
    fun extractTimeStampAndGetTimeStamp(filePath: String): String {
        val file = File(filePath)
        val timeStamp = file.nameWithoutExtension
        val timestampLong = timeStamp.toLong()
        val sdf = SimpleDateFormat(SDF_VIDEO_TIMESTAMP_FORMAT, Locale.getDefault())
        val date = Date(timestampLong)

        return sdf.format(date)
    }
}