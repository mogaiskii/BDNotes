package com.example.awesomeapps

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomeapps.databinding.NoteCellBinding
import java.lang.Math.min
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class NoteViewHolder (
    private val context: Context,
    private val binding: NoteCellBinding,
    private val clickListener: NoteClickListener
): RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bindNote(note: Note) {
        binding.text.text = note.text.substring(0, min(note.text.length, 200))
        binding.tag.text = note.tag
        val dateFormatter = DateTimeFormatter.ofPattern("dd LLL yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val localDate = note.created.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        binding.createdDate.text = localDate.format(dateFormatter)
        binding.createdTime.text = localDate.format(timeFormatter)

        binding.root.setOnClickListener {
            clickListener.openNote(note)
        }
    }
}
