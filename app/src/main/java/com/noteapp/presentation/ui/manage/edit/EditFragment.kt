package com.noteapp.presentation.ui.manage.edit

import android.view.View
import androidx.fragment.app.viewModels
import com.noteapp.R
import com.noteapp.presentation.ui.manage.base.BaseManageNoteFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditFragment: BaseManageNoteFragment() {
    override val viewModel: EditViewModel by viewModels()
    var color = ""
    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        binding.manageNoteTopText.text = getString(R.string.edit_note)
        binding.btnSubmitNote.text = getString(R.string.edit)

//        binding.etNoteTitle.text = ""
//        binding.etNoteDesc.text = ""

        binding.colorBox1.setOnClickListener {
            color = "#088A1F"
        }
        binding.colorBox2.setOnClickListener {
            color = "#00BCD4"
        }
        binding.colorBox3.setOnClickListener {
            color = "#DA0C00"
        }
        binding.colorBox4.setOnClickListener {
            color = "#673AB7"
        }
        binding.colorBox5.setOnClickListener {
            color = "#FFC107"
        }
    }
}