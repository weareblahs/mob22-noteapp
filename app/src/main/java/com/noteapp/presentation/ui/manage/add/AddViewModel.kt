package com.noteapp.presentation.ui.manage.add

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.noteapp.data.model.Note
import com.noteapp.data.repo.NotesRepo
import com.noteapp.presentation.ui.manage.base.BaseManageNoteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(private val repo: NotesRepo): BaseManageNoteViewModel() {


    override fun submitNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
//                blank checks for note title
                if(note.title.isEmpty()) throw Exception("Note title must not be empty") else repo.addNote(note)
                _finish.emit(Unit)
            }
        }
    }
}