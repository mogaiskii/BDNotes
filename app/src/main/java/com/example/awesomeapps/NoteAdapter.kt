package com.example.awesomeapps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomeapps.databinding.NoteCellBinding

class NoteAdapter(
    private val notes: List<Note>,
    private val clickListener: NoteClickListener
): RecyclerView.Adapter<NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = NoteCellBinding.inflate(from, parent, false)
        return NoteViewHolder(parent.context, binding, clickListener)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bindNote(notes[position])
    }

}