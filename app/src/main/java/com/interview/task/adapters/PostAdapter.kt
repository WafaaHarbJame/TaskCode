package com.interview.task.adapters


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.interview.task.Model.PostModel
import com.interview.task.R
import com.interview.task.activities.PostDetailsActivity
import com.interview.task.classes.Constants


class PostAdapter(postlist: ArrayList<PostModel>, activity: Activity) :
        RecyclerView.Adapter<PostAdapter.PostHolder>() {
        var postList: ArrayList<PostModel> = postlist
        var activity: Activity = activity
    override fun onCreateViewHolder(
            viewGroup: ViewGroup,
            i: Int
        ): PostHolder {
            val root: View = LayoutInflater.from(activity).inflate(R.layout.row_post_item, null, false)
            return PostHolder(root)
        }

        override fun onBindViewHolder(
            postHolder: PostHolder,
            i: Int)
        {

            postHolder.title.text = postList[i].title

        }

        override fun getItemCount(): Int {
            return postList.size
        }

        inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var title: TextView
            var  cardV:CardView

            init {
                title = itemView.findViewById(R.id.postTitleTv)
                cardV=itemView.findViewById(R.id.cardV)

                cardV.setOnClickListener {
                    val position=adapterPosition;
                    val details = Intent(activity, PostDetailsActivity::class.java)
                    details.putExtra(Constants.POST_IMAGE, postList[position].thumbnailUrl)
                    details.putExtra(Constants.POST_TITLE, postList[position].title)
                    activity.startActivity(details)
                }
            }


        }


    fun setPostListItems(postList:ArrayList<PostModel>){
        this.postList = postList;
        notifyDataSetChanged()
    }


}

