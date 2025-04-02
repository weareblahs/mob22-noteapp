package com.noteapp.presentation.ui.manage.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.noteapp.R
import com.noteapp.databinding.FragmentManageNoteBinding
import com.noteapp.presentation.ui.base.BaseFragment
import kotlinx.coroutines.launch

abstract class BaseManageNoteFragment: BaseFragment() {
    lateinit var binding: FragmentManageNoteBinding
    abstract override val viewModel: BaseManageNoteViewModel
    var color = -1



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
//        set color for color box
        val color1 = ContextCompat.getColor(requireContext(), R.color.green)
        val color2 = ContextCompat.getColor(requireContext(), R.color.cyan)
        val color3 = ContextCompat.getColor(requireContext(), R.color.red)
        val color4 = ContextCompat.getColor(requireContext(), R.color.purple)
        val color5 = ContextCompat.getColor(requireContext(), R.color.yellow)
        binding.colorBox1.setBackgroundColor(color1)
        binding.colorBox2.setBackgroundColor(color2)
        binding.colorBox3.setBackgroundColor(color3)
        binding.colorBox4.setBackgroundColor(color4)
        binding.colorBox5.setBackgroundColor(color5)

        binding.colorBox1.setOnClickListener {
            changeColor(color1)
        }
        binding.colorBox2.setOnClickListener {
            changeColor(color2)
        }
        binding.colorBox3.setOnClickListener {
            changeColor(color3)
        }
        binding.colorBox4.setOnClickListener {
            changeColor(color4)
        }
        binding.colorBox5.setOnClickListener {
            changeColor(color5)
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
    }

    fun changeColor(colorResource: Int) {
        // sets color variable to value passed
        // the following conditions are hardcoded, as the background color values are same for this app
        // there is a graphic that overlays on the color box when clicked. this adjusts its visibility
        // note that this is duplicated due to resource linking reasons
        val color1 = ContextCompat.getColor(requireContext(), R.color.green)
        val color2 = ContextCompat.getColor(requireContext(), R.color.cyan)
        val color3 = ContextCompat.getColor(requireContext(), R.color.red)
        val color4 = ContextCompat.getColor(requireContext(), R.color.purple)
        val color5 = ContextCompat.getColor(requireContext(), R.color.yellow)
        color = colorResource
        binding.colorBox1Selected.isVisible = (colorResource == color1)
        binding.colorBox2Selected.isVisible = (colorResource == color2)
        binding.colorBox3Selected.isVisible = (colorResource == color3)
        binding.colorBox4Selected.isVisible = (colorResource == color4)
        binding.colorBox5Selected.isVisible = (colorResource == color5)
    }
}