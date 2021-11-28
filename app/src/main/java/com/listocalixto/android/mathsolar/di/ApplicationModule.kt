package com.listocalixto.android.mathsolar.di

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.listocalixto.android.mathsolar.BaseApplication
import com.listocalixto.android.mathsolar.app.Constants.APP_DATABASE_NAME
import com.listocalixto.android.mathsolar.app.Constants.FREE_NEWS_BASE_URL
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.MainDispatcher
import com.listocalixto.android.mathsolar.app.CoroutinesQualifiers.IoDispatcher
import com.listocalixto.android.mathsolar.core.NetworkConnection
import com.listocalixto.android.mathsolar.data.source.ApplicationDatabase
import com.listocalixto.android.mathsolar.data.source.article.remote.ArticleWebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
    fun provideArticleDao(db: ApplicationDatabase) = db.articleDao

    @Singleton
    @Provides
    fun providePVProjectDao(db: ApplicationDatabase) = db.pvProjectDao

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Singleton
    @Provides
    fun providesApplication(@ApplicationContext app: Context): BaseApplication =
        app as BaseApplication

    @Singleton
    @Provides
    fun provideNetworkConnection(@ApplicationContext context: Context): LiveData<Boolean> =
        NetworkConnection(context)

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(FREE_NEWS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiServices(retrofit: Retrofit): ArticleWebService {
        return retrofit.create(ArticleWebService::class.java)
    }

}