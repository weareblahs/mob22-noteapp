package com.noteapp.presentation.ui.manage.base

import androidx.lifecycle.viewModelScope
import com.noteapp.data.model.Note
import com.noteapp.presentation.ui.base.BaseViewModel
import com.noteapp.presentation.ui.manage.edit.NotesIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseManageNoteViewModel: BaseViewModel() {
    abstract fun handleIntent(intent: NotesIntent)
}


data class EditNote(
    val existingNote: Note = Note(),
    val isPending: Boolean = true,
    val isFinished: Boolean = false
)