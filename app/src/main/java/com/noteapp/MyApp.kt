package com.noteapp

import android.app.Application
import android.util.Log
import com.noteapp.core.service.AuthServiceImpl
import com.noteapp.data.repo.NotesRepo
import com.noteapp.data.repo.NotesRepoImpl
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp: Application() {
    @Inject
    override fun onCreate() {
        super.onCreate()
    }
}