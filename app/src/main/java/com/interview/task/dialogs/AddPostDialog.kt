package com.interview.task.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.interview.task.Api.DataFetcherCallBack
import com.interview.task.LocalDb.DbOperation_Post
import com.interview.task.Model.PostModel
import com.interview.task.R
import com.interview.task.Utile


class AddPostDialog(
    var activity: Activity?,
    var dataFetcherCallBack: DataFetcherCallBack?
) :
    Dialog(activity!!) {
    private val postTitle: EditText
    private var postImage: ImageView
    private  var  savBtn:Button
    var db: DbOperation_Post? = null
    private val SELECT_PHOTO = 7777


    private val addPostDialog: AddPostDialog
        get() = this

    init {
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_addpost)
        postTitle = findViewById(R.id.postTitleET)
        postImage = findViewById(R.id.postImage)
        savBtn = findViewById(R.id.savBtn)

        addPostDialog.getWindow()?.setGravity(Gravity.BOTTOM)
        addPostDialog.getWindow()?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        addPostDialog.setCancelable(true)

        db = DbOperation_Post(activity)

        savBtn.setOnClickListener {

            if (postTitle.text.toString() == "") {
                postTitle?.error = "Enter Post title "

                postTitle.requestFocus()
            }
            else {


                val post = PostModel()
                post.title=postTitle.text.toString()
                val added: Boolean = db!!.insert(post)
                if (added) {
                    Toast.makeText(activity," Added Success",Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(activity,"Failed to add ",Toast.LENGTH_SHORT).show();


                }
                dismiss()
                dataFetcherCallBack?.Result(
                    addPostDialog,
                    "AddPostDialog",
                    true
                )
            }



        }
        try {
            if (activity != null && !activity!!.isFinishing) show()
        } catch (e: Exception) {
            dismiss()
        }

        postImage.setOnClickListener {

        }

    }

    fun convertBitmapToByte(): ByteArray? {
        val bitmap = (postImage.getDrawable() as BitmapDrawable).bitmap
        return Utile.getbyte(bitmap)
    }
}