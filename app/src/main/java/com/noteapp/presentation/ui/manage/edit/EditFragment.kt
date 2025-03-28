package com.noteapp.presentation.ui.manage.edit

import android.view.View
import androidx.fragment.app.viewModels
import com.noteapp.R
import com.noteapp.presentation.ui.manage.base.BaseManageNoteFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditFragment: BaseManageNoteFragment() {
    override val viewModel: EditViewModel by viewModels()

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        binding.manageTaskTopText.text = getString(R.string.edit_task)
        binding.submitTaskText.text = getString(R.string.edit)
    }
}