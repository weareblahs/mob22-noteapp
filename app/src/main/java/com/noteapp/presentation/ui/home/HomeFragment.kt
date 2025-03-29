package com.noteapp.presentation.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.noteapp.R
import com.noteapp.data.model.Note
import com.noteapp.databinding.FragmentHomeBinding
import com.noteapp.presentation.ui.adapter.NoteAdapter
import com.noteapp.presentation.ui.base.BaseFragment
import com.noteapp.presentation.ui.login.LoginFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    override val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: NoteAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        Glide.with(binding.profilePicture).load(viewModel.getProfileUrl())
            .into(binding.profilePicture) // loads google profile photo into top-left of app
        binding.profilePicture.setOnClickListener {
            viewModel.logOut(requireContext()) // not implemented: popbackstack when logout. check homeviewmodel
            lifecycleScope.launch {
                viewModel.success.collect{
                    findNavController().navigate(HomeFragmentDirections.toLoginFragment())
                }
            }
        }

        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            binding.tvMyNotes.isVisible = !hasFocus
        }

        binding.btnAddNote.setOnClickListener {
            val dir = HomeFragmentDirections.actionHomeFragmentToAddFragment()
            findNavController().navigate(dir)
        }
    }

    private fun setupAdapter() {
        adapter = NoteAdapter(emptyList())
//        TODO: listener code
        binding.rvNote.adapter = adapter
        binding.rvNote.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        setupAdapter()
        lifecycleScope.launch {
            viewModel.notes.collect {
                adapter.setNotes(it)
                binding.noNotes.isVisible = it.isEmpty()
            }
        }



        Log.d("debugging", viewModel.notes.value.isEmpty().toString())
    }
}