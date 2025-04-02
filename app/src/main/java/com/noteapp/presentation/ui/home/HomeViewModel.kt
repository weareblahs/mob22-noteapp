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
    val notes = MutableStateFlow<List<Note>>(emptyList())
    val _empty = MutableStateFlow<Boolean>(true)
    val empty = _empty.asStateFlow()
    val _dataPending = MutableStateFlow<Boolean>(true)
    val dataPending = _dataPending.asStateFlow()
    private val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()
    init {
        getNotes()
        Log.d("debugging", empty.toString())
    }

    fun getProfileUrl(): Uri? {
        return authService.getLoggedInUser()?.photoUrl
    }

    fun getNotes() : Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                repo.getNotes().collect {items ->
                    notes.update {items}
                    _empty.update {false}
                    _dataPending.update {false} // handles loading state, which will control the "loading" view on the layout. the loading view only contains a spinner
                }
            }
        }
        return true
    }


    fun addDummyNote() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addNote(Note(
                title = "Note title ${(1..1000).random()}",
                desc = "Note description ${(1..1000).random()}"
            ))
        }
    }

    fun logOut(context: Context) {
////        pops out an alert dialog after tapping on the "log out" button
//        val alertDialog = AlertDialog.Builder(context)
//        alertDialog.apply {
//            //setIcon(R.drawable.ic_hello)
//            setTitle("Log out")
//            setMessage("Are you sure you want to log out from this app?")
//            setPositiveButton("Back") { dialog, id ->
//                dialog.dismiss()
//            }
//            setNegativeButton("Log out") { dialog, dismiss ->
////                authService.logout() // logs out from auth service BUT does not go back. will have a implementation in the future
//                viewModelScope.launch {
//                    errorHandler {
//                        authService.logout()
//                    }
//                    _success.emit(Unit)
//                }
//            }
//        }.create().show()
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

    fun deleteNote(context: Context,note: Note){
        DialogUtils.showConfirmationDialog(
            context = context,
            title = "Delete Note",
            message = "Are you sure you want to delete this note?",
            positiveText = "Delete",
            negativeText = "Cancel"
        ) {
            viewModelScope.launch {
                repo.deleteNote(note)
            }
        }
    }


}