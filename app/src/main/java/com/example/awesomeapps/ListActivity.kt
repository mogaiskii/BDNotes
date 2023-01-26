package com.example.awesomeapps

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.awesomeapps.databinding.ActivityListBinding

class ListActivity : AppCompatActivity(), NoteClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityListBinding

    private val noteViewModel: NoteViewModel by viewModels {
        NoteModelFactory((application as NoteApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAuth()

        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            navigateToMain()
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
        }

        setRecyclerView()
    }

    private fun checkAuth() {
        if (!(application as NoteApplication).isAuthorized(this)) {
            navigateToPin()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_list, menu)
        if (!(application as NoteApplication).isPasswordSet(this)) {
            binding.toolbar.menu.findItem(R.id.action_remove_pin)?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> navigateToPin(true)
            R.id.action_remove_pin -> removePin()
            R.id.action_export_notes -> exportNotes()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun exportNotes(): Boolean {
        return try {
            NotesExporter(noteViewModel.notes.value!!).exportNotes(applicationContext)
            true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Snackbar.make(window.decorView.rootView, "Error happened :(", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
            false
        }
    }

    private fun removePin(): Boolean {
        (application as NoteApplication).removePin(this)
        return true
    }

    private fun navigateToPin(setting: Boolean = false): Boolean {
        val intent = Intent(this, PinActivity::class.java)
        if (setting) intent.putExtra("setting", true)
        startActivity(intent)
        finish()
        return true
    }

    private fun navigateToMain(): Boolean {
        startActivity(Intent(this, MainActivity::class.java))
        return true
    }

    private fun setRecyclerView() {
        val currentActivity = this
        noteViewModel.notes.observe(this) {
            binding.notesListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = NoteAdapter(it, currentActivity)
            }
        }
    }

    override fun openNote(note: Note) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("noteId", note.id.toString())
        startActivity(intent)
    }

}