package com.example.task.di

import android.content.Context
import com.example.task.repository.ItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAppRepository(
        @ApplicationContext appContext: Context,
    ) = ItemRepository(context = appContext)

}