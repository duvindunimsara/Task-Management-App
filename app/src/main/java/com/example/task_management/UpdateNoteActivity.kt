package com.example.task_management

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.task_management.db.DBOpenHelper
import com.example.task_management.utils.COLUMN_NAME_DATE
import com.example.task_management.utils.COLUMN_NAME_DESCRIPTION
import com.example.task_management.utils.COLUMN_NAME_PRIORITY
import com.example.task_management.utils.COLUMN_NAME_TITLE
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class UpdateNoteActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var etUpdatedTitle: TextInputLayout
    private lateinit var etUpdatedDescription: TextInputLayout
    private lateinit var etUpdatedPriority: TextInputLayout
    private lateinit var etUpdatedDate: TextInputLayout
    private lateinit var fabUpdate: FloatingActionButton
    private val dbOpenHelper = DBOpenHelper(this)
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        etUpdatedTitle = findViewById(R.id.et_updated_title)
        etUpdatedDescription = findViewById(R.id.et_updated_description)
        etUpdatedPriority = findViewById(R.id.et_updated_priority)
        etUpdatedDate = findViewById(R.id.et_updated_date)
        fabUpdate = findViewById(R.id.fab_update)

        etUpdatedDate.editText?.setOnClickListener {
            showDatePicker()
        }

        // Retrieve data from intent
        val id = intent.getIntExtra(BaseColumns._ID, 0).toString()
        val titleOld = intent.getStringExtra(COLUMN_NAME_TITLE)
        val descriptionOld = intent.getStringExtra(COLUMN_NAME_DESCRIPTION)
        val priorityOld = intent.getIntExtra(COLUMN_NAME_PRIORITY, 0)
        val dateOld = intent.getStringExtra(COLUMN_NAME_DATE)

        // Set old data to respective fields
        etUpdatedTitle.editText?.setText(titleOld)
        etUpdatedDescription.editText?.setText(descriptionOld)
        etUpdatedPriority.editText?.setText(priorityOld.toString())
        etUpdatedDate.editText?.setText(dateOld)

        fabUpdate.setOnClickListener {
            updateData(id)
        }
    }

    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, day).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = "$year-${month + 1}-$dayOfMonth" // Month starts from 0
        etUpdatedDate.editText?.setText(selectedDate)
    }

    private fun updateData(id: String) {
        // Reset errors
        etUpdatedTitle.error = null
        etUpdatedDescription.error = null
        etUpdatedPriority.error = null
        etUpdatedDate.error = null

        // Retrieve updated data from EditText fields
        val title = etUpdatedTitle.editText?.text.toString()
        val description = etUpdatedDescription.editText?.text.toString()
        val priorityText = etUpdatedPriority.editText?.text.toString()
        val date = etUpdatedDate.editText?.text.toString()

        // Validate data
        if (title.isEmpty()) {
            etUpdatedTitle.error = "Please enter your Title"
            etUpdatedTitle.requestFocus()
            return
        }

        if (description.isEmpty()) {
            etUpdatedDescription.error = "Please enter your Description"
            etUpdatedDescription.requestFocus()
            return
        }

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

        if (date.isEmpty()) {
            etUpdatedDate.error = "Please enter a Date"
            etUpdatedDate.requestFocus()
            return
        }

        // Additional date validation can be added here if needed

        // Update note in the database
        dbOpenHelper.updateNote(id, title, description, priority, date)
        Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()

        // Navigate back to MainActivity
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentToMainActivity)
        finish()
    }



}
