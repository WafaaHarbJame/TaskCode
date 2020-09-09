package com.interview.task.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.interview.task.Model.PostModel
import com.interview.task.R
import com.interview.task.Utile
import com.interview.task.classes.Constants
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.activity_post_deails.*
import kotlinx.android.synthetic.main.row_post_item.postTitleTv


class PostDetailsActivity : AppCompatActivity() {
    private var postTitle: String? = null
    private var postImage: String? = null
    private var postType: Int? = 0

    private lateinit var postImageBlob: ByteArray


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_deails)

        setSupportActionBar(toolbar_details)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        if (intent != null) {
            val i = getIntent()
            val post = i.getSerializableExtra(Constants.POST_OBJECT) as PostModel
            postTitle = post.title
            postType = post.type

            if (postType == 1) {
                postImage = post.thumbnailUrl
                Glide.with(this)
                    .asBitmap()
                    .load(postImage)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(postIv)

            } else  if (postType == 2) {
                postImageBlob =post.thumbnailImage
                    postIv.setImageBitmap(Utile.getImage(postImageBlob))

            }

            postTitleTv.text = postTitle

        }


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}