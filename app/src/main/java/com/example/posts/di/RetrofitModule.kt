package com.example.posts.di

import com.example.posts.retrofit.IRetrofit
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule
{
    private val BASE_URL: String = "https://jsonplaceholder.typicode.com/"

    @Singleton
    @Provides
    fun providesGson() : Gson
    {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun providesRetrofit(gson: Gson) : Retrofit.Builder
    {
        return  Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))

    }

    @Singleton
    @Provides
    fun providesRetrofitInterface(retrofit: Retrofit.Builder) : IRetrofit
    {
        return retrofit.build().create(IRetrofit::class.java)
    }
}