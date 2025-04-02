package com.noteapp.presentation.ui.manage.add

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.noteapp.R
import com.noteapp.data.model.Note
import com.noteapp.presentation.ui.manage.base.BaseManageNoteFragment
import com.noteapp.presentation.ui.manage.base.BaseManageNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

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
            viewModel.submitNote(
                Note(
                    title = binding.etNoteTitle.text.toString(),
                    desc = binding.etNoteDesc.text.toString(),
                    color = color
                )
            )
        }
    }
}