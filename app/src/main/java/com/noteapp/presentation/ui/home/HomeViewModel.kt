package com.noteapp.presentation.ui.home

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.noteapp.core.service.AuthService
import com.noteapp.data.repo.NotesRepo
import com.noteapp.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.noteapp.core.utils.DialogUtils
import com.noteapp.data.model.Note
import com.noteapp.data.repo.NotesRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authService: AuthService,
    val repo: NotesRepo
) : BaseViewModel() {
//
//    private val allNotes = MutableStateFlow<List<Note>>(emptyList()) // Stores all notes
//    val notes = MutableStateFlow<List<Note>>(emptyList()) // Stores filtered notes
//
//    private val _searchQuery = MutableStateFlow("")
//    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
//
//    val _empty = MutableStateFlow<Boolean>(true)
//    val empty = _empty.asStateFlow()
//
//    val _dataPending = MutableStateFlow<Boolean>(true)
//    val dataPending = _dataPending.asStateFlow()
//
//    private val _success = MutableSharedFlow<Unit>()
//    val success = _success.asSharedFlow()
//
//    private val _isSearchActive = MutableStateFlow(false)
//    val isSearchActive = _isSearchActive.asStateFlow()
//
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
                }
            }
        }
    }


    fun searchNotes(query: String) {
        _home.update { it.copy(searchQuery = query) }// Update the search query
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            var base = home.value
            _home.collect {
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

    fun logOut(context: Context) {
        DialogUtils.showConfirmationDialog(
            context = context,
            title = "Log out",
            message = "Are you sure you want to log out?",
            positiveText = "Log out",
            negativeText = "Back"
        ) {
            viewModelScope.launch {
                authService.logout()
                _home.update {it.copy(logoutSuccess = true)}
            }
        }
    }

    fun deleteNote(context: Context, note: Note) {
        DialogUtils.showConfirmationDialog(
            context = context,
            title = "Delete Note",
            message = "Are you sure you want to delete this note?",
            positiveText = "Delete",
            negativeText = "Cancel"
        ) {
            viewModelScope.launch {
                repo.deleteNote(note)
                getNotes() // Refresh notes after deletion
            }
        }
    }
}

data class Home(
//    stores all notes
    val allNotes: List<Note> = emptyList(),
//    stores
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
