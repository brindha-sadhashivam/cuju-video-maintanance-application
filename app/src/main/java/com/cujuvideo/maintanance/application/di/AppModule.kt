package com.cujuvideo.maintanance.application.di

import android.content.Context
import androidx.room.Room
import com.cujuvideo.maintanance.application.data.datasource.database.VideoDatabase
import com.cujuvideo.maintanance.application.data.entity.mapper.VideoDataMapper
import com.cujuvideo.maintanance.application.data.repository.VideoRepositoryImpl
import com.cujuvideo.maintanance.application.data.worker.VideoUploadWorker
import com.cujuvideo.maintanance.application.domain.mapper.VideoDataViewMapper
import com.cujuvideo.maintanance.application.domain.repository.VideoRepository
import com.cujuvideo.maintanance.application.domain.usecase.FetchVideoDetailUseCase
import com.cujuvideo.maintanance.application.domain.usecase.FetchVideoListUseCase
import com.cujuvideo.maintanance.application.domain.usecase.SaveRecordedVideoUseCase
import com.cujuvideo.maintanance.application.domain.usecase.UploadVideoUseCase
import com.cujuvideo.maintanance.application.presentation.viewmodel.VideosViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import java.io.File

/**
 * Koin modules for dependency injection.
 *
 * Module providing database dependencies.
 *
 * @property databaseModule Module providing database dependencies.
 */
val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            VideoDatabase::class.java,
            "video_database"
        ).build()
    }

    single { get<VideoDatabase>().videoDao() }
}

/**
 * Module providing work manager dependencies.
 *
 * @property workManagerModule Module providing work manager dependencies.
 */
val workManagerModule = module {
    worker { VideoUploadWorker(get(), get(), get()) }
}

/**
 * Module providing repository dependencies.
 *
 * @property repositoryModule Module providing repository dependencies.
 */
val repositoryModule = module {
    single { VideoDataMapper() }
    single<VideoRepository> { VideoRepositoryImpl(get(), get()) }
}

/**
 * Module providing use case dependencies.
 *
 * @property usecaseModule Module providing use case dependencies.
 */
val usecaseModule = module {
    single { VideoDataViewMapper() }
    single { FetchVideoListUseCase(get(), get()) }
    single { SaveRecordedVideoUseCase(get()) }
    single { FetchVideoDetailUseCase(get(), get()) }
    single { UploadVideoUseCase(get(), get()) }
}

/**
 * Module providing storage dependencies.
 *
 * @property storageModule Module providing storage dependencies.
 */
val storageModule = module {
    single { createAppVideoDir(get()) }
}

/**
 * Module providing view model dependencies.
 *
 * @property viewModelModule Module providing view model dependencies.
 */
val viewModelModule = module {
    viewModel {
        VideosViewModel(
            get(), get(),
            get(), get(),
            get(), get()
        )
    }
}

/**
 * Creates the app video directory.
 *
 * @param context The application context.
 * @return The app video directory.
 */
fun createAppVideoDir(context: Context): File {
    val dir = File(context.filesDir, "videos")
    if (!dir.exists()) dir.mkdir()
    return dir
}