package com.cujuvideo.maintanance.application.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.cujuvideo.maintanance.application.presentation.viewmodel.datastate.UploadState

/**
 * VideoEntity
 *
 * Entity class representing a video in the database.
 *
 * @property id The unique identifier of the video.
 * @property title The title of the video.
 * @property status The status of the video.
 * @property internalFilePath The internal file path of the video.
 */
@Entity(tableName = "videos_table")
data class VideoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val status: UploadState = UploadState.RECORDED,
    val internalFilePath: String,
)

/**
 * UploadStateConverter
 *
 * Type converter class for converting [UploadState] to [String] and vice versa.
 *
 * @property fromUploadState Converts [UploadState] to [String].
 * @property toUploadState Converts [String] to [UploadState].
 */
class UploadStateConverter {
    @TypeConverter
    fun fromUploadState(uploadState: UploadState): String {
        return uploadState.name
    }

    @TypeConverter
    fun toUploadState(uploadState: String): UploadState {
        return UploadState.valueOf(uploadState)
    }
}