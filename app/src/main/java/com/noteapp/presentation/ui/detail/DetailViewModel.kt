package com.noteapp.presentation.ui.detail

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.noteapp.core.utils.DialogUtils
import com.noteapp.data.model.Note
import com.noteapp.data.repo.NotesRepo
import com.noteapp.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repo: NotesRepo) : BaseViewModel() {
    val _dataPending = MutableStateFlow(true)
    val dataPending = _dataPending.asStateFlow()
    val _note = MutableStateFlow<Note>(Note())
    val note = _note.asStateFlow()
    val _noteDeleted = MutableStateFlow(false)
    val noteDeleted = _noteDeleted.asStateFlow()

    fun getNote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                val note = repo.getSingleNote(id)
                _note.update { note!! }
                _dataPending.update { false } // handles loading state, which will control the "loading" view on the layout. the loading view only contains a spinner
            }
        }
    }
    fun deleteNote(context: Context, note: Note){
        DialogUtils.showConfirmationDialog(
            context = context,
            title = "Delete Note",
            message = "Are you sure you want to delete this note?",
            positiveText = "Delete",
            negativeText = "Cancel"
        ) {
            viewModelScope.launch {
                repo.deleteNote(note)
                _dataPending.update { true }
                _noteDeleted.update { true }
            }
        }
    }
}