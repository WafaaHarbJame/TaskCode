package com.interview.task.LocalDb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory

import android.database.sqlite.SQLiteOpenHelper
import androidx.annotation.Nullable


class CreateOPenHelperSQL(
    @Nullable context: Context?,
    @Nullable name: String?,
    @Nullable factory: CursorFactory?,
    version: Int
) :
    SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table POSTS(id INTEGER  PRIMARY KEY AUTOINCREMENT,title TEXT,thumbnailUrl TEXT ,thumbnailImage BLOB,type INTEGER)")
           }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int) {
        db.execSQL("drop table if exists POSTS")
        onCreate(db)
    }
}
