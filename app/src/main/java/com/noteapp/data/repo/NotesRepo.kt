package com.noteapp.data.repo

import com.noteapp.data.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepo {
    suspend fun getNotes(): Flow<List<Note>>
    suspend fun getNotesByQuery(query: String): Flow<List<Note>>
    suspend fun getSingleNote(id: String): Note?
    suspend fun addNote(note: Note)
    suspend fun editNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun test(): String // test if it successfully connect s to the implementation
}