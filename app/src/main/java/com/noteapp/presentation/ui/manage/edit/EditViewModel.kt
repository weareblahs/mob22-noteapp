package com.noteapp.presentation.ui.manage.edit

import androidx.lifecycle.viewModelScope
import com.noteapp.data.model.Note
import com.noteapp.data.repo.NotesRepo
import com.noteapp.presentation.ui.manage.base.BaseManageNoteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(private val repo: NotesRepo) : BaseManageNoteViewModel() {
    override fun submitNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                repo.editNote(note)
                _finish.emit(Unit)
            }
        }
    }
}