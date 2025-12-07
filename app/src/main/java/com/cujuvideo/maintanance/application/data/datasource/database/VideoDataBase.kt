package com.cujuvideo.maintanance.application.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cujuvideo.maintanance.application.data.datasource.dao.VideoDao
import com.cujuvideo.maintanance.application.data.entity.UploadStateConverter
import com.cujuvideo.maintanance.application.data.entity.VideoEntity

/**
 * VideoDatabase
 *
 * Room database for storing video entities.
 *
 * @property videoDao The DAO for managing video entities.
 * @constructor Creates an instance of [VideoDatabase].
 */
@Database(entities = [VideoEntity::class], version = 1)
@TypeConverters(UploadStateConverter::class)
abstract class VideoDatabase : RoomDatabase(){

    /**
     * Retrieves the DAO for managing video entities.
     *
     * @return The DAO for managing video entities.
     */
    abstract fun videoDao(): VideoDao
}
