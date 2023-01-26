package com.example.awesomeapps

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.util.Date

class NotesExporter(
    private val notes: List<Note>
) {
    private val notesData: String
        get() {
            val transform: (Note) -> String = {
                NoteDateFormatter(it.created).formatDateTimeWithText(it.text)
            }
            val updated = notes.map(transform)
            return updated.joinToString("\n\n\n*-----------*\n\n\n")
        }

    fun exportNotes(context: Context) {
        val name = "bdnotes_dump" + Date().toString() + ".txt"
        val dirPath = context.filesDir.absolutePath

        val file = File(dirPath + File.separator + name)
        if (!file.exists()) {
            file.createNewFile()
        }
        if (file.exists()) {
            file.writeText(notesData)
            val uri = FileProvider.getUriForFile(
                context, BuildConfig.APPLICATION_ID + ".provider", file
            )
            val intent = Intent(Intent.ACTION_SEND)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } else {
            throw java.lang.IllegalStateException("Cannot create file for exporting")
        }
    }
}