package com.example.awesomeapps

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.awesomeapps.databinding.ActivityMainBinding
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var note: Note? = null
    private val noteViewModel: NoteViewModel by viewModels {
        NoteModelFactory((application as NoteApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.navigationIcon = ContextCompat.getDrawable(
            this, android.R.drawable.ic_input_add
        )
        binding.toolbar.navigationIcon?.setTint(Color.WHITE)
        binding.toolbar.setNavigationOnClickListener { saveNote() }

        binding.floatingActionButton.setOnClickListener {
            saveNote()
        }

        binding.editTextTextMultiLine2.requestFocus()
        window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
        val noteId = intent?.getStringExtra("noteId")
        if (noteId != null) {
            subscribeToNote(noteId)
        }
    }

    private fun subscribeToNote(noteId: String) {
        val self = this
        noteViewModel.noteById(noteId).observe(this) {
            note = it
            if (note != null) {
                Log.d("NOTE", it.id.toString())
                Log.d("NOTE", it.text)
                self.note = note!!
                binding.editTextTextMultiLine2.setText(note!!.text)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        if (note == null) {
            binding.toolbar.menu.findItem(R.id.empty)?.isVisible = false
            binding.toolbar.menu.findItem(R.id.action_delete)?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_list -> navigateToList()
            R.id.action_save -> saveNote()
            R.id.action_copy -> copyNote()
            R.id.action_delete -> deleteNote()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun copyNote(): Boolean {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val noteText = binding.editTextTextMultiLine2.text.toString()
        val dateFormatter = if (note != null) {
            NoteDateFormatter(note!!.created)
        } else {
            NoteDateFormatter(Date())
        }
        val text = dateFormatter.formatDateTimeWithText(noteText)
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
        return true
    }

    private fun deleteNote(): Boolean {
        if (note != null) {
            noteViewModel.deleteNote(note!!)
        }
        return navigateToList()
    }

    private fun saveNote(): Boolean {
        val text = binding.editTextTextMultiLine2.text.toString()
        if (text.isEmpty()) return true

        if (note != null) {
            note!!.text = text
            noteViewModel.updateNote(note!!)
        } else {
            this.note = Note(text=text)
            noteViewModel.addNote(note!!)
        }
        return navigateToList()
    }

    private fun navigateToList(deleteTop: Boolean = false): Boolean {
        val intent = Intent(this, ListActivity::class.java)
        if (deleteTop) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
        finish()
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}