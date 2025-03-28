package com.noteapp.presentation.ui.manage.add

import android.view.View
import androidx.fragment.app.viewModels
import com.noteapp.R
import com.noteapp.presentation.ui.manage.base.BaseManageNoteFragment
import com.noteapp.presentation.ui.manage.base.BaseManageNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFragment: BaseManageNoteFragment() {
    override val viewModel: AddViewModel by viewModels()

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        binding.manageTaskTopText.text = getString(R.string.add_task)
        binding.submitTaskText.text = getString(R.string.add)
    }
}