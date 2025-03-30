package com.noteapp.presentation.ui.manage.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.noteapp.databinding.FragmentManageNoteBinding
import com.noteapp.presentation.ui.base.BaseFragment
import kotlinx.coroutines.launch

abstract class BaseManageNoteFragment: BaseFragment() {
    lateinit var binding: FragmentManageNoteBinding
    abstract override val viewModel: BaseManageNoteViewModel
    var color = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch  {
            viewModel.finish.collect{
                findNavController().popBackStack()
                Snackbar.make(requireView(), "Note modified", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    fun changeColor(colorNo: String) {
        color = colorNo // sets color variable to value passed
        // the following conditions are hardcoded, as the background color values are same for this app
        // there is a graphic that overlays on the color box when clicked. this adjusts its visibility
        binding.colorBox1Selected.isVisible = (colorNo == "#088A1F")
        binding.colorBox2Selected.isVisible = (colorNo == "#00BCD4")
        binding.colorBox3Selected.isVisible = (colorNo == "#DA0C00")
        binding.colorBox4Selected.isVisible = (colorNo == "#673AB7")
        binding.colorBox5Selected.isVisible = (colorNo == "#FFC107")
    }
}