package com.noteapp.data.repo

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.noteapp.core.service.AuthService
import com.noteapp.data.model.Note
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotesRepoImpl @Inject constructor (private val authService: AuthService): NotesRepo {
    val db = Firebase.firestore
    private fun getCollectionRef() : CollectionReference {
        val uid = authService.getUid() ?: throw Exception("User not found")
        return db.collection("users/$uid/notes")
    }

    override suspend fun getNotes() = callbackFlow{
        val listener = getCollectionRef().addSnapshotListener {
                value, error ->
            if(error != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }
            val tasks = mutableListOf<Note>()
            value?.documents?.forEach{doc ->
                val task = doc.toObject(Note::class.java) // nested object
                if(task != null) {
                    tasks.add(task.copy(id = doc.id))
                }
            }
            Log.d("debugging", authService.getUid().toString())
            Log.d("debugging", tasks.toString()) // test if note works for this case
            trySend(tasks)
        }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getNotesByQuery(query: String): Flow<List<Note>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSingleNote(id: String): Flow<Note> {
        TODO("Not yet implemented")
    }

    override suspend fun addNote(note: Note) {
        val docRef = getCollectionRef().document()
        docRef.set(note.copy(id = docRef.id)).await()
    }

    override suspend fun editNote(note: Note) {
        getCollectionRef().document(note.id!!).set(note).await()
    }

    override suspend fun deleteNote(note: Note) {
        getCollectionRef().document(note.id!!).delete().await()
    }

    override suspend fun test(): String {
        return "Successfully linked to NotesRepoImpl"
    }
}