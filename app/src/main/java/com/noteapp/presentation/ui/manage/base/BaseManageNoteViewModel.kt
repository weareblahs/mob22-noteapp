package com.noteapp.presentation.ui.manage.base

import com.noteapp.data.model.Note
import com.noteapp.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseManageNoteViewModel: BaseViewModel() {
    protected val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()
    abstract fun submitNote(note: Note)
}