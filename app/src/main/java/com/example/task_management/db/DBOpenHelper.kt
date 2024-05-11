package com.example.task_management.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.task_management.model.NoteModel
import com.example.task_management.utils.COLUMN_NAME_DESCRIPTION
import com.example.task_management.utils.COLUMN_NAME_TITLE
import com.example.task_management.utils.TABLE_NAME
import com.example.task_management.utils.COLUMN_NAME_PRIORITY


private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$COLUMN_NAME_TITLE TEXT," +
            "$COLUMN_NAME_DESCRIPTION TEXT," +
            "$COLUMN_NAME_PRIORITY INTEGER)"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

class DBOpenHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "NoteApp.db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun addNote(title: String, description: String, priority: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME_TITLE, title)
            put(COLUMN_NAME_DESCRIPTION, description)
            put(COLUMN_NAME_PRIORITY, priority)
        }
        db?.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun readNotes(): MutableList<NoteModel> {
        val db = this.readableDatabase
        val cursorNotes: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val notesList: MutableList<NoteModel> = mutableListOf()

        if (cursorNotes.moveToFirst()) {
            do {
                notesList.add(
                    NoteModel(
                        cursorNotes.getInt(0),
                        cursorNotes.getString(1),
                        cursorNotes.getString(2),
                        cursorNotes.getInt(3)
                    )
                )
            } while (cursorNotes.moveToNext())
        }
        cursorNotes.close()
        return notesList
    }

    fun updateNote(id: String, title: String, description: String, priority: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME_TITLE, title)
            put(COLUMN_NAME_DESCRIPTION, description)
            put(COLUMN_NAME_PRIORITY, priority)
        }
        try {
            db?.update(TABLE_NAME, values, "_id = ?", arrayOf(id))
            db.close()
        } catch (e: Exception) {
            Log.d("DBOpenHelper", e.message.toString())
        }
    }

    fun deleteNote(id: String) {
        val db = this.writableDatabase
        try {
            db?.delete(TABLE_NAME, "_id = ?", arrayOf(id))
            db.close()
        } catch (e: Exception) {
            Log.d("DBOpenHelper", e.message.toString())
        }
    }
}
