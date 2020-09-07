package com.interview.task.LocalDb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.interview.task.Model.PostModel
import java.sql.SQLException


class DbOperation_Post(context: Context?) {
    var context: Context?
    var db: SQLiteDatabase

    @Throws(SQLException::class)
    fun insert(post: PostModel): Boolean {
        val cv = ContentValues()
        cv.put("title", post.title)
        cv.put("id", post.id)
        cv.put("thumbnailUrl", post.thumbnailUrl)
        val inserted = db.insert(Table, null, cv)
        return if (inserted > 0) {
            true
        } else {
            false
        }
    }



    @Throws(SQLException::class)
    fun Update(post: PostModel): Boolean {
        val cv = ContentValues()
        cv.put("title", post.title)
        cv.put("id", post.id)
        cv.put("thumbnailUrl", post.thumbnailUrl)
        val updated = db.update(
            Table,
            cv,
            "ID=?",
            arrayOf(post.id.toString() + "")
        ).toLong()
        return if (updated > 0) {
            true
        } else {
            false
        }
    }




    fun getImage(id: Int): ByteArray {
        val cr: Cursor = db.rawQuery(
            "select * from $Table where ID = $id",
            null
        )
        val result: ByteArray
        cr.moveToFirst()
        result = cr.getBlob(cr.getColumnIndex("IMAGE_SRC"))
        cr.close()
        return result
    }




    fun delete(id: Int): Int {
        val db_delete = DbOperation_Post(context)
        return db.delete(
            Table,
            "ID =? ",
            arrayOf(Integer.toString(id))
        )
    }



    companion object {
        private const val DATABASENAME = "postDb.db"
        const val Table = "POSTS"
    }

    init {
        val dbase =
            CreateOPenHelperSQL(context, DATABASENAME, null, 1)
        this.context = context
        db = dbase.writableDatabase
    }


    fun getAllPosts(): List<PostModel>? {
        val cursor = db.rawQuery("select * from POSTS", null)
        cursor.moveToFirst()
        val list: MutableList<PostModel> = ArrayList<PostModel>()
        while (!cursor.isAfterLast) {
            val postModel = PostModel()
            postModel.setId(cursor.getInt(0))
            postModel.title[1]
            postModel.thumbnailUrl[2]
            list.add(postModel)
            cursor.moveToNext()
        }
        cursor.close()
        return list
    }




}



