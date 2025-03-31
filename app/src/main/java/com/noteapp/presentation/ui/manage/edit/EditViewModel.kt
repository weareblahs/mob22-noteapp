package com.noteapp.presentation.ui.manage.edit

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.noteapp.data.model.Note
import com.noteapp.data.repo.NotesRepo
import com.noteapp.presentation.ui.manage.base.BaseManageNoteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(private val repo: NotesRepo) : BaseManageNoteViewModel() {
    val _existingNote = MutableStateFlow<Note>(Note())
    val existingNote = _existingNote.asStateFlow()
    val _dataPending = MutableStateFlow<Boolean>(true)
    val dataPending = _dataPending.asStateFlow()

    val _isUpdated = MutableSharedFlow<Unit>()
    val isUpdated = _isUpdated.asSharedFlow()
    fun getNote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                val note = repo.getSingleNote(id)
                _existingNote.update { note!! }
                _isUpdated.emit(Unit)
                _dataPending.update { false }
            }
        }
    }

    override fun submitNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                _dataPending.update { true } // loading view is implemented in this case so that when changing data in Firestore, no last minute edits are allowed
                repo.editNote(note)
                _finish.emit(Unit)
            }
        }
    }
}