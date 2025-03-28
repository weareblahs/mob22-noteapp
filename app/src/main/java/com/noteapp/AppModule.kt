package com.noteapp

import com.noteapp.core.service.AuthService
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
    @Provides
    @Singleton
    fun provideNoteRepo(authService: AuthService): NotesRepo {
        return NotesRepoImpl(authService = authService)
    }
}