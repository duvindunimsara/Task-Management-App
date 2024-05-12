package com.example.task_management




import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task_management.adapter.NoteAdapter
import com.example.task_management.db.DBOpenHelper
import com.example.task_management.model.NoteModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var fabCreate: FloatingActionButton
    private lateinit var searchEditText: EditText // Add EditText reference for search

    private lateinit var myDataset: MutableList<NoteModel>
    private val dbOpenHelper = DBOpenHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainRecyclerView = findViewById(R.id.main_recycler_view)
        fabCreate = findViewById(R.id.fab_create)
        searchEditText = findViewById(R.id.searchEditText) // Initialize search EditText

        myDataset = dbOpenHelper.readNotes().sortedBy { it.priority.toDouble() }.toMutableList()




        mainRecyclerView.layoutManager = LinearLayoutManager(this)
        mainRecyclerView.adapter = NoteAdapter(this, myDataset)

        fabCreate.setOnClickListener {
            val intentToAddNoteActivity = Intent(this, AddNoteActivity::class.java)
            startActivity(intentToAddNoteActivity)
            finish()
        }

        // Search functionality
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Filter dataset based on search text
                val searchText = s.toString().toLowerCase(Locale.getDefault())
                val filteredList = if (searchText.isNotEmpty()) {
                    myDataset.filter { it.title.toLowerCase(Locale.getDefault()).contains(searchText) }
                } else {
                    myDataset
                }
                (mainRecyclerView.adapter as NoteAdapter).updateList(filteredList)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}