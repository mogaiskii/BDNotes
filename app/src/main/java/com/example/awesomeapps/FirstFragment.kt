package com.example.awesomeapps

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.awesomeapps.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var note: Note? = null
    private val noteViewModel: NoteViewModel by viewModels {
        NoteModelFactory((activity?.application as NoteApplication).repository)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        binding.editTextTextMultiLine2.requestFocus()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        val noteId = activity?.intent?.getStringExtra("noteId")
        if (noteId != null) {
            subscribeToNote(noteId)
        }
        return binding.root
    }

    private fun subscribeToNote(noteId: String) {
        val self = this
        noteViewModel.noteById(noteId).observe(this) {
            note = it
            Log.d("NOTE", it.id.toString())
            Log.d("NOTE", it.text)
            if (note != null) {
                self.note = note!!
                binding.editTextTextMultiLine2.setText(note!!.text)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
            saveAction()
        }
    }

    public fun saveAction() {
        val text = binding.editTextTextMultiLine2.text.toString()
        if (text.isEmpty()) return

        if (note != null) {
            note!!.text = text
            noteViewModel.updateNote(note!!)
        } else {
            this.note = Note(text=text)
            noteViewModel.addNote(note!!)
        }
        findNavController().navigate(
            R.id.action_FirstFragment_to_SecondFragment,
            bundleOf("noteId" to note!!.id.toString())
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}