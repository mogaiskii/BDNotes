package com.example.awesomeapps

import android.content.Intent
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
import com.example.awesomeapps.databinding.ActivityMainBinding

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

        binding.actionSave.setOnClickListener { saveNote() }

        binding.floatingActionButton.setOnClickListener {
            saveNote()
        }

        binding.editTextTextMultiLine2.requestFocus()
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_list -> navigateToList()
            R.id.action_save -> saveNote()
            R.id.action_delete -> deleteNote()
            else -> super.onOptionsItemSelected(item)
        }
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