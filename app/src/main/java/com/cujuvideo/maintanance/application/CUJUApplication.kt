package com.cujuvideo.maintanance.application

import android.app.Application
import com.cujuvideo.maintanance.application.di.databaseModule
import com.cujuvideo.maintanance.application.di.repositoryModule
import com.cujuvideo.maintanance.application.di.storageModule
import com.cujuvideo.maintanance.application.di.usecaseModule
import com.cujuvideo.maintanance.application.di.viewModelModule
import com.cujuvideo.maintanance.application.di.workManagerModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

/**
 * CUJUApplication : Main Application class, responsible for koin injection
 */
class CUJUApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CUJUApplication)
            workManagerFactory()
            modules(
                listOf(
                    databaseModule,
                    workManagerModule,
                    repositoryModule,
                    usecaseModule,
                    storageModule,
                    viewModelModule
                )
            )
        }
    }
}