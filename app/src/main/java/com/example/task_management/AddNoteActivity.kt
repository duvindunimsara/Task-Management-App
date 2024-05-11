package com.example.taskmanagementapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.task_management.MainActivity
import com.example.task_management.R
import com.example.task_management.db.DBOpenHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout

class AddNoteActivity : AppCompatActivity() {

    private lateinit var etTitle: TextInputLayout
    private lateinit var etDescription: TextInputLayout
    private lateinit var etPriority: TextInputLayout // Added reference to priority TextInputLayout
    private lateinit var fabSend: FloatingActionButton
    private val dbOpenHelper = DBOpenHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        etTitle = findViewById(R.id.et_title)
        etDescription = findViewById(R.id.et_description)
        etPriority = findViewById(R.id.et_priority) // Initialize priority TextInputLayout
        fabSend = findViewById(R.id.fab_send)

        fabSend.setOnClickListener {
            fabSendData()
        }
    }

    private fun fabSendData() {
        val title = etTitle.editText?.text.toString()
        val description = etDescription.editText?.text.toString()
        val priority = etPriority.editText?.text.toString() // Get priority input

        if (title.isEmpty()) {
            etTitle.error = "Please enter your Note"
            etTitle.requestFocus()
            return
        }

        if (description.isEmpty()) {
            etDescription.error = "Please enter your Description"
            etDescription.requestFocus()
            return
        }

        if (priority.isEmpty()) {
            etPriority.error = "Please enter priority"
            etPriority.requestFocus()
            return
        }
        val priorityValue = priority.toIntOrNull()
        if (priorityValue == null || priorityValue < 1 || priorityValue > 3) {
            etPriority.error = "Priority must be a number between 1 and 3"
            etPriority.requestFocus()
            return
        }

        dbOpenHelper.addNote(title, description, priority.toInt())
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentToMainActivity)
        finish()
    }
}
