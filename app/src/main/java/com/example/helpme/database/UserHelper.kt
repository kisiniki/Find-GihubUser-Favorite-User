package com.example.helpme.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.helpme.database.DatabaseContract.UserColumns.Companion.LOGIN
import com.example.helpme.database.DatabaseContract.UserColumns.Companion.TABLE_NAME
import java.sql.SQLException

class UserHelper(context: Context) {

    private lateinit var database: SQLiteDatabase

    companion object {
        private lateinit var dataBaseHelper: DatabaseHelper
        private const val DATABASE_TABLE = TABLE_NAME

        private var INSTANCE: UserHelper? = null
        fun getInstance(context: Context): UserHelper = INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }
    }
    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor{
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$LOGIN ASC",
                null
        )
    }

    fun querByIdl(query: String): Cursor{
        return database.query(
                DATABASE_TABLE,
                null,
                "$LOGIN = ?",
                arrayOf(query),
                null,
                null,
                null,
                null,
        )
    }
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE,null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$LOGIN = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$LOGIN = '$id'", null)
    }

}