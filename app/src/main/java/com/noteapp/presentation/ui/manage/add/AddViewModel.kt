package com.noteapp.presentation.ui.manage.add

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
class AddViewModel @Inject constructor(private val repo: NotesRepo): BaseManageNoteViewModel() {
    val _addNote = MutableStateFlow(AddNote())
    val addNote = _addNote.asStateFlow()
    fun submitNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
//                blank and character checks for note title and note description (optional)
                if(note.title.isEmpty()) throw Exception("Note title must not be empty") else {
                    if(note.title.length > 20) {
                        throw Exception("Note title must not be longer than 20 characters. The title has ${note.title.length} characters.")
                    } else {
                        if(note.desc.length > 120) {
                            throw Exception("Note description must not be longer than 120 characters. The description has ${note.desc.length} characters.")
                        }
                        repo.addNote(note)
                    }
                }
                _addNote.update { it.copy(isFinished = true) }
            }
        }
    }

    fun handleIntent(intent: NotesIntent) {
        when(intent) {
            is NotesIntent.SubmitNote -> submitNote(intent.note)
        }
    }
}
sealed class NotesIntent {
    data class SubmitNote(val note: Note): NotesIntent()
}
data class AddNote (
    val isFinished: Boolean = false
)

