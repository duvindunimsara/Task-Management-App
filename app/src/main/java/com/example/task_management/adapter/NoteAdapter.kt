package com.example.task_management.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.task_management.R
import com.example.taskmanagementapp.model.NoteModel


import com.example.taskmanagementapp.utils.DialogBox
import com.google.android.material.button.MaterialButton

class NoteAdapter(
    private val context: Context,
    private var dataSet: List<NoteModel>
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val originalDataSet: List<NoteModel> = dataSet.toList()

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textTitle: TextView = view.findViewById(R.id.text_title)
        val textDescription: TextView = view.findViewById(R.id.text_description)
        val textPriority: TextView = view.findViewById(R.id.text_priority)
        val textDate: TextView = view.findViewById(R.id.text_date) // Add TextView for date
        val btnEdit: MaterialButton = view.findViewById(R.id.btn_edit)
        val btnDelete: MaterialButton = view.findViewById(R.id.btn_delete)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_recycler_single_item, parent, false)
        return NoteViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val dialog = DialogBox()
        val item = dataSet[position]

        holder.textTitle.text = item.title
        holder.textDescription.text = item.description
        holder.textPriority.text = "Priority: ${item.priority}"
        holder.textDate.text = "Date: ${item.date}" // Bind date to TextView

        holder.btnEdit.setOnClickListener {
            dialog.editDialog(context, item)
        }

        holder.btnDelete.setOnClickListener {
            dialog.deleteDialog(context, item)
        }
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }

    // Function to update the dataset with filtered data
    fun updateList(filteredDataSet: List<NoteModel>) {
        dataSet = filteredDataSet
        notifyDataSetChanged()
    }

    // Function to reset dataset to original data
    fun resetList() {
        dataSet = originalDataSet
        notifyDataSetChanged()
    }
}