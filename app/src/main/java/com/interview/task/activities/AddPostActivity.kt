package com.interview.task.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import com.interview.task.LocalDb.DbOperation_Post
import com.interview.task.Model.PostModel
import com.interview.task.R
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.dialog_addpost.*
import kotlinx.android.synthetic.main.dialog_addpost.postImage
import kotlinx.android.synthetic.main.dialog_addpost.postTitleET

class AddPostActivity : ActivityBase() {
//    private lateinit var CloseBut: ImageView
//    private lateinit var PostTitleET: EditText
//    private lateinit var PostImage: ImageView
//    private lateinit var SavBtn: Button
    var db: DbOperation_Post? = null
    private val SELECT_PHOTO = 7777

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
//
//        PostTitleET = findViewById(R.id.postTitleET)
//        PostImage = findViewById(R.id.postImage)
//        SavBtn = findViewById(R.id.savBtn)
//
        db = DbOperation_Post(getActiviy())

        postImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, SELECT_PHOTO)

        }





        saveBtn.setOnClickListener {

            if (postTitleET.text.isNullOrEmpty()) {
                postTitleET.error = "Enter Post title"
                postTitleET.requestFocus()
            } else {

                val post = PostModel()
                post.title = postTitleET.text.toString()
//                post.thumbnailUrl
                val added: Boolean = db!!.insert(post)
                if (added) {
                    Toast.makeText(getActiviy(), getString(R.string.add_sucess), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(getActiviy(), "Failed to Add", Toast.LENGTH_SHORT).show()
                }


            }


        }


    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            val pick_image: Uri? = data.data
            postImage.setImageURI(pick_image)
        }
    }
}