package com.cujuvideo.maintanance.application.domain.mapper

import com.cujuvideo.maintanance.application.data.entity.mapper.DataMapper
import com.cujuvideo.maintanance.application.data.model.VideoDataModel
import com.cujuvideo.maintanance.application.domain.viewdatamodel.VideoViewDataModel

/**
 * VideoDataViewMapper
 *
 * Mapper class to transform [VideoDataModel] to [VideoViewDataModel].
 */
class VideoDataViewMapper : DataMapper<VideoDataModel, VideoViewDataModel>() {

    /**
     * Transforms a [VideoDataModel] to a [VideoViewDataModel].
     *
     * @param e The [VideoDataModel] to be transformed.
     * @return The transformed [VideoViewDataModel].
     */
    override fun transform(e: VideoDataModel): VideoViewDataModel {
        return VideoViewDataModel(
            e.videoId,
            e.title,
            e.status,
            e.internalFilePath
        )
    }
}