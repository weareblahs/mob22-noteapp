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

    private val allNotes = MutableStateFlow<List<Note>>(emptyList()) // Stores all notes
    val notes = MutableStateFlow<List<Note>>(emptyList()) // Stores filtered notes

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val _empty = MutableStateFlow<Boolean>(true)
    val empty = _empty.asStateFlow()

    val _dataPending = MutableStateFlow<Boolean>(true)
    val dataPending = _dataPending.asStateFlow()

    private val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive = _isSearchActive.asStateFlow()


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
                    allNotes.value = items  // Store all notes
                    notes.value = items // Initially show all notes
                    _empty.update { items.isEmpty() }
                    _dataPending.update { false }
                }
            }
        }
    }

    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
    }

    fun searchNotes(query: String) {
        _searchQuery.value = query  // Update the search query
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery.collect { query ->
                notes.value = if (query.isEmpty()) {
                    allNotes.value  // Show all notes when query is empty
                } else {
                    allNotes.value.filter {
                        it.title.contains(query, ignoreCase = true) ||
                                it.desc.contains(query, ignoreCase = true)
                    }
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
                _success.emit(Unit)
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
