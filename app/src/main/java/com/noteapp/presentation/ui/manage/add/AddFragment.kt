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
}