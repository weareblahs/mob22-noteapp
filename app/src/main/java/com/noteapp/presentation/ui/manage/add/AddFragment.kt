package com.noteapp.presentation.ui.manage.add

import android.view.View
import androidx.fragment.app.viewModels
import com.noteapp.R
import com.noteapp.data.model.Note
import com.noteapp.presentation.ui.manage.base.BaseManageNoteFragment
import com.noteapp.presentation.ui.manage.base.BaseManageNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFragment: BaseManageNoteFragment() {
    override val viewModel: AddViewModel by viewModels()
    var color = "#FFFFFF"
    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
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
//        NOTE ABOUT COLORS
//        binding.colorBox1 = green (#088A1F)
//        binding.colorBox2 = cyan (#00BCD4)
//        binding.colorBox3 = red (#DA0C00)
//        binding.colorBox4 = purple (#673AB7)
//        binding.colorBox5 = yellow ($FFC107)
//        the home fragment takes hex code as string (example: "#088A1F") and displays it
//        at the background of the note item itself - which according to the model, the
//        color is stored as a hex code. A color variable has been created so it can be
//        changed when a user taps on the color (example:
    //        binding.colorBox1.setOnClickListener {
    //            color = "#088A1F"
    //        }
//        )

//        note that home fragment has the code to handle conversion to integer

//        when submit, the color is brought along with the title and description, so ensure
//        that the Note model looks like this when submitting from this class:
//            Note(
//                title = binding.etNoteTitle.text.toString(),
//                desc = binding.etNoteDesc.text.toString(),
//                color = color
//            )
    }
}