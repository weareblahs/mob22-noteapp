package com.noteapp.presentation.ui.manage.edit

import android.text.Editable
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
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
        viewModel.getNote(noteId) // calls viewModel to get the existing note data
        binding.manageNoteTopText.text = getString(R.string.edit_note)
        binding.btnSubmitNote.text = getString(R.string.edit)
        binding.btnSubmitNote.setOnClickListener {
            viewModel.submitNote(
                Note(
                    id = noteId,
                    title = binding.etNoteTitle.text.toString(),
                    desc = binding.etNoteDesc.text.toString(),
                    color = color
                )
            )
        }

//            color bindings
        binding.colorBox1.setOnClickListener {
            changeColor("#088A1F")
        }
        binding.colorBox2.setOnClickListener {
            changeColor("#00BCD4")
        }
        binding.colorBox3.setOnClickListener {
            changeColor("#DA0C00")
        }
        binding.colorBox4.setOnClickListener {
            changeColor("#673AB7")
        }
        binding.colorBox5.setOnClickListener {
            changeColor("#FFC107")
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.isUpdated.collect{
//                when firestore gets the note, the following will update the note title and the note description
                binding.etNoteTitle.text = Editable.Factory.getInstance().newEditable(viewModel.existingNote.value.title)
                binding.etNoteDesc.text = Editable.Factory.getInstance().newEditable(viewModel.existingNote.value.desc)
                changeColor(viewModel.existingNote.value.color)
            }
        }

        lifecycleScope.launch {
            viewModel.dataPending.collect{
                binding.loading.isVisible = viewModel.dataPending.value // handles loading state
            }
        }
    }


}