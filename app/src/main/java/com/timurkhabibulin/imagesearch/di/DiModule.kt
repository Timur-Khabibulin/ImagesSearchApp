package com.timurkhabibulin.imagesearch.di

import com.timurkhabibulin.imagesearch.data.AuthorizationInterceptor
import com.timurkhabibulin.imagesearch.data.ImagesSearchApi
import com.timurkhabibulin.imagesearch.data.RepositoryImpl
import com.timurkhabibulin.imagesearch.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://google.serper.dev"

@Module
@InstallIn(SingletonComponent::class)
interface DiModule {
    @Binds
    fun provideRepository(impl: RepositoryImpl): Repository

    companion object {

        @Provides
        @Singleton
        fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @Singleton
        fun provideImagesSearchApi(retrofit: Retrofit): ImagesSearchApi {
            return retrofit.create(ImagesSearchApi::class.java)
        }

        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .build()
        }

        @Singleton
        @Provides
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient().newBuilder()
                .addInterceptor(AuthorizationInterceptor())
                .build()
        }
    }
}
