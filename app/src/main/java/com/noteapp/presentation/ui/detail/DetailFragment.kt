package com.noteapp.presentation.ui.detail

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.noteapp.R
import com.noteapp.databinding.FragmentDetailBinding
import com.noteapp.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment() {
    override val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_detail, container, false)
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        viewModel.getNote(args.NoteId)

        binding.btnDeleteNote.setOnClickListener {
            // Show confirmation dialog to delete note
            viewModel.deleteNote(requireContext(), viewModel.note.value)
        }

        binding.btnEditNote.setOnClickListener {
            val dir = DetailFragmentDirections.actionDetailFragmentToEditFragment(viewModel.note.value.id!!)
            findNavController().navigate(dir)
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.note.collect{
//                when firestore gets the note, the following will update the note title and the note description
                var color = "#FFFFFF"
                if(viewModel.note.value.color != "") {
                    color = viewModel.note.value.color
                }

                binding.noteTitle.text = viewModel.note.value.title
                binding.noteDescription.text = viewModel.note.value.desc
                binding.noteDisplay.setBackgroundColor(color.toColorInt())
//                changeColor(viewModel.existingNote.value.color)
            }
        }
        lifecycleScope.launch {
            viewModel.dataPending.collect {
                binding.loading.isVisible = viewModel.dataPending.value // handles loading state
            }
        }
        lifecycleScope.launch {
            viewModel.noteDeleted.collect {
                if(viewModel.noteDeleted.value) {
//                    BUG FIX: after note is edited, if a user wants to delete this note, popBackStack() will be
//                             going back to the login layout instead of the home layout as expected. this is a
//                             hardcoded way to do this
                    val dir = DetailFragmentDirections.actionDetailFragmentToHomeFragment()
                    findNavController().navigate(dir)
                    Snackbar.make(requireView(), "Note deleted", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}