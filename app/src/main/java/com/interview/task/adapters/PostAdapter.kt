package com.interview.task.adapters


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.interview.task.LocalDb.DbOperation_Post
import com.interview.task.Model.PostModel
import com.interview.task.R
import com.interview.task.activities.AddPostActivity
import com.interview.task.activities.PostDetailsActivity
import com.interview.task.classes.Constants


class PostAdapter( var activity: Activity,var postList: MutableList<PostModel>?) :
        RecyclerView.Adapter<PostAdapter.PostHolder>() {
    var db: DbOperation_Post? = DbOperation_Post(activity)
    private val editCode = 100


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

            postHolder.title.text = postList!![i].title

        }

        override fun getItemCount(): Int {
            return postList!!.size
        }

        inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var title: TextView = itemView.findViewById(R.id.postTitleTv)
            private var  cardView:CardView = itemView.findViewById(R.id.cardV)
            private var  editBut:ImageView = itemView.findViewById(R.id.editBtn)
            private var  deleteBut:ImageView = itemView.findViewById(R.id.deleteBtn)

            init {

                cardView.setOnClickListener {
                    val position=adapterPosition;
                    val details = Intent(activity, PostDetailsActivity::class.java)
                    val post= postList!![position];
                    details.putExtra(Constants.POST_OBJECT,post);
                    activity.startActivity(details)

                }

                editBut.setOnClickListener {
                    val position=adapterPosition;
                    val post= postList!![position];
                    val add = Intent(activity, AddPostActivity::class.java)
                    add.putExtra(Constants.POST_OBJECT,post);
                    add.putExtra(Constants.EDIT_POST,true);
                    activity.startActivityForResult(add,editCode)

                }

                deleteBut.setOnClickListener {
                    val position=adapterPosition;
                    if( db?.delete( postList!![position].id)!! >0){
                        Toast.makeText(activity, activity.getString(R.string.deleted_sucess), Toast.LENGTH_SHORT).show()
                        postList?.removeAt(position);
                        notifyItemChanged(position)
                        notifyDataSetChanged();
                        notifyItemRemoved(position);

                    }




                }
            }


        }


    fun setPostListItems(postList:MutableList<PostModel>){
        this.postList = postList;
        notifyDataSetChanged()
    }





}

