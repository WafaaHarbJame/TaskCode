package com.interview.task.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.interview.task.R
import com.interview.task.classes.Constants
import kotlinx.android.synthetic.main.row_post_item.*


class PostDetailsActivity : AppCompatActivity() {
    private lateinit var PostTitleTv: TextView
    private lateinit var PostIv: ImageView
    var POST_TITLE: String? = null
    var POST_IMAGE: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_deails)

        PostTitleTv = findViewById(R.id.postTitleTv)
        PostIv = findViewById(R.id.postIv)

        val intent = intent
        if (intent != null) {
            POST_TITLE = intent.getStringExtra(Constants.POST_TITLE)
            POST_IMAGE = intent.getStringExtra(Constants.POST_IMAGE)

        }
        Glide
            .with(this)
            .asBitmap()
            .load(POST_IMAGE)
            .placeholder(R.drawable.ic_launcher_background)
            .into(PostIv);
        PostTitleTv.text = POST_TITLE

    }
}