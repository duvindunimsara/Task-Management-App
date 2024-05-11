package com.example.taskmanagementapp.model

data class NoteModel(
    val id: Int,
    val title: String,
    val description: String,
    val priority: Int,
    val date: String
)