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
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        viewModel.getNote(args.NoteId)

        binding.btnDeleteNote.setOnClickListener {
            fun deleteNoteIntent() {
                viewModel.handleIntent(NotesIntent.DeleteNote(viewModel.singleNote.value.note))
            }
            // Show confirmation dialog to delete note
            showDialog(
                getString(R.string.delete_note),
                getString(R.string.note_delete_confirmation),
                getString(R.string.delete),
                ::deleteNoteIntent,
                false // since the MutableStateFlow is updated and the snackbar is handled by setupViewModelObserver, the snackbar option in this dialog is set to false
            )
        }

        binding.btnEditNote.setOnClickListener {
            val dir = DetailFragmentDirections.actionDetailFragmentToEditFragment(viewModel.singleNote.value.note.id!!)
            findNavController().navigate(dir)
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.singleNote.collect { base ->
//              when firestore gets the note, the following will update the note title and the note description
                binding.loading.isVisible = base.dataPending // handles loading state
                binding.noteTitle.text = base.note.title // binds title
                binding.noteDescription.text = base.note.desc // binds description
                binding.noteDisplay.setBackgroundColor(base.note.color) // assigns color

                if(base.noteDeleted) {
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