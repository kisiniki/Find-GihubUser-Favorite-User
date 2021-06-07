package com.example.helpme.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.helpme.database.DatabaseContract.UserColumns.Companion.AVATAR_URL
import com.example.helpme.database.DatabaseContract.UserColumns.Companion.FAVORITE
import com.example.helpme.database.DatabaseContract.UserColumns.Companion.LOGIN
import com.example.helpme.database.DatabaseContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "githubuser"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                "($LOGIN TEXT PRIMARY KEY NOT NULL," +
                " $AVATAR_URL TEXT NOT NULL,"+
                " $FAVORITE TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}