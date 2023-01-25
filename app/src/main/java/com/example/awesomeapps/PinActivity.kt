package com.example.awesomeapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import com.example.awesomeapps.databinding.ActivityPinBinding
import com.google.android.material.snackbar.Snackbar

class PinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPinBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val setting = intent.getBooleanExtra("setting", false)


        binding.editTextNumberPassword.doAfterTextChanged {
            Log.d("Content", it.toString())
            if (it.toString().isNotEmpty() and (it.toString().length == 4)) {
                if (setting) savePin()
                else passPin()
            }
        }
        binding.editTextNumberPassword.requestFocus()
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    }

    private fun savePin() {
        val result = (application as NoteApplication).savePin(
            this,
            binding.editTextNumberPassword.text.toString()
        )
        if (result) navigateToList()
    }

    private fun navigateToList(): Boolean {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
        finish()
        return true
    }

    private fun passPin() {
        val result = (application as NoteApplication).passPin(
            this,
            binding.editTextNumberPassword.text.toString()
        )
        if (result) {
            navigateToList()
        } else {
            Snackbar.make(
                window.decorView.rootView,
                "Wrong code",
                Snackbar.LENGTH_LONG
            )
                .setAction("Action", null).show()
        }
    }
}