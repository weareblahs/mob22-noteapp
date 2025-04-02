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
    val _singleNote = MutableStateFlow(SingleNote())
    val singleNote = _singleNote.asStateFlow()
    fun getNote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                val note = repo.getSingleNote(id)
                _singleNote.update {
                    it.copy (
                        note = note!!,
                        dataPending = false
                    )
                } // dataPending handles loading state, which will control the "loading" view on the layout. the loading view only contains a spinner
            }
        }
    }
    fun handleIntent(intent: NotesIntent) {
        when(intent) {
            is NotesIntent.DeleteNote -> deleteNote(intent.context, intent.note)
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
                _singleNote.update {
                    it.copy(dataPending = true)
                }
                repo.deleteNote(note)
                _singleNote.update {
                    it.copy(noteDeleted = true, dataPending = false)
                }

            }
        }
    }
}

sealed class NotesIntent {
    data class DeleteNote(val context: Context, var note: Note): NotesIntent()
}

data class SingleNote (
    val dataPending: Boolean = true,
    val note: Note = Note(),
    val noteDeleted: Boolean = false
)