package com.noteapp.presentation.ui.manage.edit

import androidx.lifecycle.viewModelScope
import com.noteapp.data.model.Note
import com.noteapp.data.repo.NotesRepo
import com.noteapp.presentation.ui.manage.base.BaseManageNoteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(private val repo: NotesRepo) : BaseManageNoteViewModel() {
    val _editNote = MutableStateFlow(EditNote())
    val editNote = _editNote.asStateFlow()

    fun submitNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
//                blank and character checks for note title and note description (optional)
                if(note.title.isEmpty()) throw Exception("Note title must not be empty") else {
                        _editNote.update {it.copy(isPending = true)} // loading view is implemented in this case so that when changing data in Firestore, no last minute edits are allowed
                        repo.editNote(note)
                }
                _editNote.update {it.copy(isFinished = true)}
            }
        }
    }

    fun getNote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                val note = repo.getSingleNote(id)
                _editNote.update {it.copy(
                    existingNote = note!!,
                    isPending = false
                )}
            }
        }
    }




    fun handleIntent(intent: NotesIntent) {
        when(intent) {
            is NotesIntent.GetNote -> getNote(intent.noteId)
            is NotesIntent.SubmitNote -> submitNote(intent.note)
        }
    }

}

sealed class NotesIntent {
    data class GetNote(val noteId: String): NotesIntent()
    data class SubmitNote(val note: Note): NotesIntent()
}

data class EditNote(
    val existingNote: Note = Note(), // changes note data to existing note data if it exists
    val isPending: Boolean = true, // indicates if the data is still loading
    val isFinished: Boolean = false // indicated if the note is updated
)