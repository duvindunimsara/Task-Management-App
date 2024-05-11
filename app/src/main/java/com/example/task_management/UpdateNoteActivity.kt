package com.example.taskmanagementapp

import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.task_management.MainActivity
import com.example.task_management.R
import com.example.taskmanagementapp.db.DBOpenHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.example.taskmanagementapp.utils.COLUMN_NAME_DATE
import com.example.taskmanagementapp.utils.COLUMN_NAME_DESCRIPTION
import com.example.taskmanagementapp.utils.COLUMN_NAME_PRIORITY
import com.example.taskmanagementapp.utils.COLUMN_NAME_TITLE

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var etUpdatedTitle: TextInputLayout
    private lateinit var etUpdatedDescription: TextInputLayout
    private lateinit var etUpdatedPriority: TextInputLayout
    private lateinit var etUpdatedDate: TextInputLayout // New field for date
    private lateinit var fabUpdate: FloatingActionButton
    private val dbOpenHelper = DBOpenHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        etUpdatedTitle = findViewById(R.id.et_updated_title)
        etUpdatedDescription = findViewById(R.id.et_updated_description)
        etUpdatedPriority = findViewById(R.id.et_updated_priority)
        etUpdatedDate = findViewById(R.id.et_updated_date) // Initialize date field
        fabUpdate = findViewById(R.id.fab_update)

        val titleOld = intent.getStringExtra(COLUMN_NAME_TITLE)
        val descriptionOld = intent.getStringExtra(COLUMN_NAME_DESCRIPTION)
        val priorityOld = intent.getIntExtra(COLUMN_NAME_PRIORITY, 0)
        val dateOld = intent.getStringExtra(COLUMN_NAME_DATE) // Get date value

        if (!titleOld.isNullOrBlank()) {
            etUpdatedTitle.editText?.setText(titleOld)
            etUpdatedDescription.editText?.setText(descriptionOld)
            etUpdatedPriority.editText?.setText(priorityOld.toString())
            etUpdatedDate.editText?.setText(dateOld) // Set date value

            Log.d("com.example.taskmanagementapp.UpdateNoteActivity", titleOld.toString())
            Log.d("com.example.taskmanagementapp.UpdateNoteActivity", descriptionOld.toString())

        } else {
            Log.d("com.example.taskmanagementapp.UpdateNoteActivity", "value was null")
            Toast.makeText(this, "Value was null", Toast.LENGTH_SHORT).show()
        }

        fabUpdate.setOnClickListener {
            updateData()
        }
    }

    private fun updateData() {
        val id = intent.getIntExtra(BaseColumns._ID, 0).toString()

        if (etUpdatedTitle.editText?.text.toString().isEmpty()) {
            etUpdatedTitle.error = "Please enter your Title"
            etUpdatedTitle.requestFocus()
            return
        }

        if (etUpdatedDescription.editText?.text.toString().isEmpty()) {
            etUpdatedDescription.error = "Please enter your Description"
            etUpdatedDescription.requestFocus()
            return
        }

        val priorityText = etUpdatedPriority.editText?.text.toString()
        if (priorityText.isEmpty()) {
            etUpdatedPriority.error = "Please enter priority"
            etUpdatedPriority.requestFocus()
            return
        }

        val priority = priorityText.toIntOrNull()
        if (priority == null || priority !in 1..3) {
            etUpdatedPriority.error = "Priority must be a number between 1 and 3"
            etUpdatedPriority.requestFocus()
            return
        }

        val date = etUpdatedDate.editText?.text.toString()

        if (notEmpty()) {
            dbOpenHelper.updateNote(
                id,
                etUpdatedTitle.editText?.text.toString(),
                etUpdatedDescription.editText?.text.toString(),
                priority,
                date
            )
            Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
            val intentToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentToMainActivity)
            finish()
        }
    }

    private fun notEmpty(): Boolean {
        return (etUpdatedTitle.editText?.text.toString().isNotEmpty()
                && etUpdatedDescription.editText?.text.toString().isNotEmpty()
                && etUpdatedPriority.editText?.text.toString().isNotEmpty()
                && etUpdatedDate.editText?.text.toString().isNotEmpty())
    }
}