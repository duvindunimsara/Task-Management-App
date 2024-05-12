package com.example.task_management

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.task_management.MainActivity
import com.example.task_management.R
import com.example.task_management.db.DBOpenHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    private lateinit var etTitle: TextInputLayout
    private lateinit var etDescription: TextInputLayout
    private lateinit var etPriority: TextInputLayout
    private lateinit var etDate: TextInputLayout
    private lateinit var fabSend: FloatingActionButton
    private val dbOpenHelper = DBOpenHelper(this)
    private var selectedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        etTitle = findViewById(R.id.et_title)
        etDescription = findViewById(R.id.et_description)
        etPriority = findViewById(R.id.et_priority)
        etDate = findViewById(R.id.et_date)
        fabSend = findViewById(R.id.fab_send)

        // Set up click listener for date input field
        etDate.editText?.setOnClickListener {
            showDatePicker()
        }

        fabSend.setOnClickListener {
            fabSendData()
        }
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, day ->
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, day)

                // Format the selected date
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                // Set the formatted date to the date TextInputEditText
                etDate.editText?.setText(formattedDate)
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }

    private fun fabSendData() {
        val title = etTitle.editText?.text.toString()
        val description = etDescription.editText?.text.toString()
        val priority = etPriority.editText?.text.toString()

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

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate.time)

        dbOpenHelper.addNote(title, description, priorityValue, formattedDate)
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
        val intentToMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentToMainActivity)
        finish()
    }
}
