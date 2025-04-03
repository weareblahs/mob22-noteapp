package com.noteapp.presentation.ui.home

import android.net.Uri
import android.util.Log
import com.noteapp.core.service.AuthService
import com.noteapp.data.repo.NotesRepo
import com.noteapp.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.noteapp.data.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authService: AuthService,
    val repo: NotesRepo
) : BaseViewModel() {
    private val _home = MutableStateFlow(Home())
    val home = _home.asStateFlow()

    init {
        getNotes()
        observeSearchQuery()
    }

    fun getProfileUrl(): Uri? {
        return authService.getLoggedInUser()?.photoUrl
    }

    fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                repo.getNotes().collect { items ->
                    _home.update {
                        it.copy (
                            allNotes = items,  // Store all notes
                            notes = items,  // Initially show all notes
                            isEmpty = items.isEmpty(),
                            dataPending = false
                        )
                    }
                    Log.d("debugging", home.value.notes.toString())
                }
            }
        }
    }


    fun searchNotes(query: String) {
        _home.update { it.copy(searchQuery = query) }// Update the search query
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _home.collect { base ->
                val query = base.searchQuery
                if (query.isEmpty()) {
                    _home.update {it.copy(notes = base.allNotes)}  // Show all notes when query is empty
                } else {
                    val data = base.allNotes.filter {
                        it.title.contains(query, ignoreCase = true) ||
                                it.desc.contains(query, ignoreCase = true)
                    }
                    _home.update{it.copy(notes = data)}
                }
            }
        }
    }

    fun addDummyNote() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addNote(
                Note(
                    title = "Note title ${(1..1000).random()}",
                    desc = "Note description ${(1..1000).random()}"
                )
            )
        }
    }

    fun logOut() {
        viewModelScope.launch {
            authService.logout()
            _home.update {it.copy(logoutSuccess = true)}
        }
    }
    fun handleIntent(intent: NotesIntent) {
        when(intent) {
            is NotesIntent.SearchNote -> searchNotes(intent.query)
            is NotesIntent.DeleteNote -> deleteNote(intent.note)
            is NotesIntent.Logout -> logOut()
        }
    }
    private fun deleteNote(note: Note) {
        viewModelScope.launch {
            repo.deleteNote(note) // deletes note
            getNotes() // Refresh notes after deletion
        }
    }


}
sealed class NotesIntent {
    data class SearchNote(var query: String): NotesIntent()
    data class DeleteNote(var note: Note): NotesIntent()
    class Logout(): NotesIntent()
}
data class Home(
//    stores all notes
    val allNotes: List<Note> = emptyList(),
//    stores a backup copy of all notes. in case the search query is blank, it will be restored
    val notes: List<Note> = emptyList(),
//    string to store search query
    val searchQuery: String = "",
//    a boolean state that it is empty. true as default
    val isEmpty: Boolean = true,
//    indicates visibility of loading view. true as default
    val dataPending: Boolean = true,
//    LOG OUT: check if log out is success. if not, throw error exception. false as default
    val logoutSuccess: Boolean = false
)
