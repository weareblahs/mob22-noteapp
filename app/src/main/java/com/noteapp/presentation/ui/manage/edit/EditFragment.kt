package com.noteapp.presentation.ui.manage.edit

import android.text.Editable
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.noteapp.R
import com.noteapp.data.model.Note
import com.noteapp.presentation.ui.manage.base.BaseManageNoteFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditFragment: BaseManageNoteFragment() {
    override val viewModel: EditViewModel by viewModels()
    private val args: EditFragmentArgs by navArgs()


    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        val noteId = args.id
        viewModel.handleIntent(NotesIntent.GetNote(noteId)) // calls viewModel to get the existing note data
        binding.manageNoteTopText.text = getString(R.string.edit_note)
        binding.btnSubmitNote.text = getString(R.string.edit)
        binding.btnSubmitNote.setOnClickListener {
            viewModel.handleIntent(
                NotesIntent.SubmitNote(
                    Note(
                        id = noteId,
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
            viewModel.editNote.collect {
//                when firestore gets the note, the following will update the note title and the note description
                val base = viewModel.editNote.value
                binding.loading.isVisible = base.isPending
//                this changes the "pending" state
                if (!base.isPending) {
                    binding.etNoteTitle.text =
                        Editable.Factory.getInstance().newEditable(base.existingNote.title)
                    binding.etNoteDesc.text =
                        Editable.Factory.getInstance().newEditable(base.existingNote.desc)
                    changeColor(base.existingNote.color)
                }
                if(base.isFinished) {
                    findNavController().popBackStack()
                    Snackbar.make(requireView(), "Note edited", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}