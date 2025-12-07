package com.cujuvideo.maintanance.application.data.entity.mapper

import com.cujuvideo.maintanance.application.data.entity.VideoEntity
import com.cujuvideo.maintanance.application.data.model.VideoDataModel

/**
 * VideoDataMapper
 *
 * Mapper class to transform [VideoEntity] to [VideoDataModel].
 */
class VideoDataMapper : DataMapper<VideoEntity, VideoDataModel>() {

    /**
     * Transforms a [VideoEntity] to a [VideoDataModel].
     *
     * @param e The [VideoEntity] to be transformed.
     * @return The transformed [VideoDataModel].
     */
    override fun transform(e: VideoEntity): VideoDataModel {
        return VideoDataModel(
            e.id,
            e.title,
            e.status,
            e.internalFilePath
        )
    }
}