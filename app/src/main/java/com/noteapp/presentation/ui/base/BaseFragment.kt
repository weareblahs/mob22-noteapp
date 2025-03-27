package com.noteapp.presentation.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {
    protected abstract val viewModel: BaseViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onFragmentResult()
        setupUiComponents(view)
        setupViewModelObserver()
    }

    protected open fun onFragmentResult() {

    }

    protected open fun setupViewModelObserver() {

    }

    protected open fun setupUiComponents(view: View){

    }
}