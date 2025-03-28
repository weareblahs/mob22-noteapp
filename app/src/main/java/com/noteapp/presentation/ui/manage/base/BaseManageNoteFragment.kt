package com.noteapp.presentation.ui.manage.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.noteapp.databinding.FragmentManageNoteBinding
import com.noteapp.presentation.ui.base.BaseFragment
import kotlinx.coroutines.launch

abstract class BaseManageNoteFragment: BaseFragment() {
    lateinit var binding: FragmentManageNoteBinding
    abstract override val viewModel: BaseManageNoteViewModel
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
            }
        }
    }
}