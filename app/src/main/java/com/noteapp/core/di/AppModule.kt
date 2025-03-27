package com.noteapp.core.di

import com.noteapp.core.service.AuthService
import com.noteapp.core.service.AuthServiceImpl
import com.noteapp.data.repo.NotesRepo
import com.noteapp.data.repo.NotesRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
//    @Provides
//    @Singleton
//    fun provideTasksRepo(
//        authService: AuthService
//    ): NotesRepo{
//        return NotesRepoImpl(
//            authService = authService
//        )
//    }

    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return AuthServiceImpl()
    }
}