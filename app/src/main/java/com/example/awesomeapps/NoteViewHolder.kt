package com.example.awesomeapps

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomeapps.databinding.NoteCellBinding
import java.lang.Math.min

class NoteViewHolder (
    private val context: Context,
    private val binding: NoteCellBinding,
    private val clickListener: NoteClickListener
): RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bindNote(note: Note) {
        binding.text.text = note.text.substring(0, min(note.text.length, 200))
        binding.tag.text = note.tag
        val formatter = NoteDateFormatter(note.created)
        binding.createdDate.text = formatter.formattedDate
        binding.createdTime.text = formatter.formattedTime

        binding.root.setOnClickListener {
            clickListener.openNote(note)
        }
    }
}
