package com.cujuvideo.maintanance.application.data.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cujuvideo.maintanance.application.data.entity.VideoEntity
import com.cujuvideo.maintanance.application.presentation.viewmodel.datastate.UploadState
import kotlinx.coroutines.flow.Flow

/**
 * VideoDao
 *
 * Data Access Object for managing video entities in the database.
 *
 * @property getAllVideos Retrieves all videos from the database.
 * @property insertVideo Inserts a video into the database.
 * @property getVideoById Retrieves a video by its ID from the database.
 * @property updateVideoStatus Updates the status of a video in the database
 */
@Dao
interface VideoDao {

    /**
     * Retrieves all videos from the database.
     *
     * @return A [Flow] emitting a list of [VideoEntity] objects.
     */
    @Query("SELECT * FROM videos_table")
    fun getAllVideos(): Flow<List<VideoEntity>>

    /**
     * Inserts a video into the database.
     *
     * @param videoEntity The [VideoEntity] object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(videoEntity: VideoEntity)

    /**
     * Retrieves a video by its ID from the database.
     *
     * @param videoId The ID of the video to retrieve.
     * @return A [Flow] emitting a [VideoEntity] object or null if not found.
     */
    @Query("SELECT * FROM videos_table WHERE id = :videoId")
    fun getVideoById(videoId: Int): Flow<VideoEntity?>

    /**
     * Updates the status of a video in the database.
     *
     * @param videoId The ID of the video to update.
     * @param uploadState The new [UploadState] of the video.
     */
    @Query("UPDATE videos_table SET status = :uploadState WHERE id = :videoId")
    suspend fun updateVideoStatus(videoId: Int, uploadState: UploadState)

}
