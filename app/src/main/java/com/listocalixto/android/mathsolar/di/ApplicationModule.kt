package com.listocalixto.android.mathsolar.di

import android.content.Context
import androidx.room.Room
import com.listocalixto.android.mathsolar.BaseApplication
import com.listocalixto.android.mathsolar.app.Constants.APP_DATABASE_NAME
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.data.source.ApplicationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideRoomInstance(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ApplicationDatabase::class.java, APP_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun providePVProjectDao(db: ApplicationDatabase) = db.pvProjectDao

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun providesApplication(@ApplicationContext app: Context): BaseApplication = app as BaseApplication

}