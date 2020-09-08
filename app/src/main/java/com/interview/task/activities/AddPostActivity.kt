package com.interview.task.activities

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.interview.task.LocalDb.DbOperation_Post
import com.interview.task.Model.PostModel
import com.interview.task.R
import com.interview.task.Utile
import com.interview.task.classes.Constants
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.dialog_addpost.postImage
import java.io.Serializable


class AddPostActivity : ActivityBase() {
    var db: DbOperation_Post? = null
    private val SELECT_PHOTO = 7777

    var postTitle: String? = null
    var postPhoto: String? = null
    var editPost:Boolean=false
    var postId:Int = 0
    private var postType: Int? = 0
    private var chooseImage: Boolean = false


    private lateinit var postImageBlob: ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        db = DbOperation_Post(getActiviy())
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)


        val intent = intent
        if (intent != null) {
            editPost=intent.getBooleanExtra(Constants.EDIT_POST,false)
            if(editPost)
            {

                val i = getIntent()
                val post = i.getSerializableExtra(Constants.POST_OBJECT) as PostModel
                postTitle =post.title
                postPhoto = post.thumbnailUrl
                postId=post.id
                postType=post.type
                editButton.visibility = View.VISIBLE
                saveBtn.visibility = View.GONE
                toolbar.title= getString(R.string.edit_post)
                postTitleText.setText(postTitle)

                if(postType==1){
                    Glide.with(this)
                        .asBitmap()
                        .load(postImage)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(postImage);
                }
                else{
                    postImageBlob= post.thumbnailImage
                    postImage.setImageBitmap(Utile.getImage(postImageBlob))

                }


            }

        }

        postImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, SELECT_PHOTO)

        }

        saveBtn.setOnClickListener {

            if (postTitleText.text.isNullOrEmpty()) {
                postTitleText.error = getString(R.string.invalid_post)
                postTitleText.requestFocus()
            }
            else  if(!chooseImage!!){
                Toast.makeText(getActiviy(), getString(R.string.choose_photo_using), Toast.LENGTH_SHORT).show()

            }

            else {

                val post = PostModel()
                post.title = postTitleText.text.toString()
                post.thumbnailUrl= null
                post.type=2
                post.thumbnailImage=convertBitmapToByte()

                val added: Boolean = db!!.insert(post)
                if (added) {
                    Toast.makeText(getActiviy(), getString(R.string.add_success), Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK, intent)
                    finish()

                } else {
                    Toast.makeText(getActiviy(), getString(R.string.add_failed), Toast.LENGTH_SHORT).show()
                }
            }

        }

        editButton.setOnClickListener {

            if (postTitleText.text.isNullOrEmpty()) {
                postTitleText.error = getString(R.string.invalid_post)
                postTitleText.requestFocus()
            } else {
                val post = PostModel()
                post.title = postTitleText.text.toString()
                post.id=postId

                if(postType==1){
                    post.thumbnailUrl= postPhoto
                    post.type=1
                    post.thumbnailImage=null

                }
                else{
                    val post = PostModel()
                    post.thumbnailImage= convertBitmapToByte()
                    post.type=2
                    post.thumbnailUrl=null
                }

                val added: Boolean = db!!.Update(post)
                if (added) {
                    Toast.makeText(getActiviy(), getString(R.string.edit_success), Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK, intent)
                    finish()

                } else {
                    Toast.makeText(getActiviy(), getString(R.string.edit_failed), Toast.LENGTH_SHORT).show()
                }

            }

        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            val pickImage: Uri? = data.data
            postImage.setImageURI(pickImage)
            chooseImage=true

        }

    }

    fun convertBitmapToByte(): ByteArray? {
        val bitmap = (postImage.getDrawable() as BitmapDrawable).bitmap
        return Utile.getbyte(bitmap)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



}