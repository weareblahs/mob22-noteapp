package com.noteapp.presentation.ui.manage.add

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.noteapp.R
import com.noteapp.data.model.Note
import com.noteapp.presentation.ui.manage.base.BaseManageNoteFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddFragment: BaseManageNoteFragment() {
    override val viewModel: AddViewModel by viewModels()
    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
//      as this is the fragment for add, it hides the "loading" spinner by default
        binding.loading.isVisible = false
        binding.manageNoteTopText.text = getString(R.string.add_note)
        binding.btnSubmitNote.text = getString(R.string.add)
        binding.btnSubmitNote.setOnClickListener {
            viewModel.handleIntent(
                NotesIntent.SubmitNote(
                    Note(
                        title = binding.etNoteTitle.text.toString(),
                        desc = binding.etNoteDesc.text.toString(),
                        color = color
                    )
                )
            )
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.addNote.collect {
                val base = viewModel.addNote.value
                if(base.isFinished) {
                    findNavController().popBackStack()
                    Snackbar.make(requireView(), "Note added", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}