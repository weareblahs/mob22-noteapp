package com.noteapp.presentation.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noteapp.data.model.Note
import com.noteapp.databinding.ItemNoteBinding
import androidx.core.graphics.toColorInt

class NoteAdapter(private var notes: List<Note>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteBinding.inflate(inflater, parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.cvNote.setCardBackgroundColor(note.color.toColorInt()) // parses color to integer so it can set the background color of the card, which is set according to
            binding.title.text = note.title // assigns note title
            binding.desc.text = note.desc // assigns note description
//            TODO: view single note on tap, which can be done with a listener
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val note = notes[position]
        val viewHolder = holder as NoteViewHolder
        viewHolder.bind(note)
    }

    fun setNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }

}