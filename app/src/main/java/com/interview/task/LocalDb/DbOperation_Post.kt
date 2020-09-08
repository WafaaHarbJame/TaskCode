package com.interview.task.LocalDb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.interview.task.Model.PostModel
import java.sql.SQLException


class DbOperation_Post(context: Context?) {
    var context: Context?
    var db: SQLiteDatabase
    val Table = "POSTS"
    private val DATABASENAME = "MyTask.db"

    @Throws(SQLException::class)
    fun insert(post: PostModel): Boolean {
        val cv = ContentValues()
        cv.put("title", post.title)
        cv.put("thumbnailUrl", post.thumbnailUrl)
        cv.put("thumbnailImage",post.thumbnailImage)
        cv.put("type",post.type)

        val inserted = db.insert(Table, null, cv)
        return inserted > 0
    }

    @Throws(SQLException::class)
    fun Update(post: PostModel): Boolean {
        val cv = ContentValues()
        cv.put("title", post.title)
        cv.put("id", post.id)
        cv.put("thumbnailUrl", post.thumbnailUrl)
        cv.put("thumbnailImage",post.thumbnailImage)
        cv.put("type",post.type)
        val updated = db.update(
            Table,
            cv,
            "ID=?",
            arrayOf(post.id.toString() + "")
        ).toLong()
        return updated > 0
    }




    fun delete(id: Int): Int {
        return db.delete(
            Table,
            "id =? ",
            arrayOf(id.toString())
        )
    }




    init {
        val dbase =
            CreateOPenHelperSQL(context, DATABASENAME, null, 2)
        this.context = context
        db = dbase.writableDatabase
    }


    fun getAllPosts(): MutableList<PostModel>? {
        val cursor = db.rawQuery("select * from POSTS", null)
        cursor.moveToFirst()
        val list: MutableList<PostModel> = mutableListOf()
        while (!cursor.isAfterLast) {
            val postModel = PostModel()
            postModel.id = cursor.getInt(0)
            postModel.title=cursor.getString(1)
            postModel.thumbnailUrl=cursor.getString(2)
            postModel.thumbnailImage=cursor.getBlob(3)
            postModel.type=cursor.getInt(4)
            list.add(postModel)
            cursor.moveToNext()
        }
        cursor.close()
        return list
    }




}



