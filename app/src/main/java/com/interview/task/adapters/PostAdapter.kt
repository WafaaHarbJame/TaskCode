package com.interview.task.adapters


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.interview.task.LocalDb.DbOperation_Post
import com.interview.task.Model.PostModel
import com.interview.task.R
import com.interview.task.activities.AddPostActivity
import com.interview.task.activities.PostDetailsActivity
import com.interview.task.classes.Constants
import com.interview.task.classes.OnLoadMoreListener


class PostAdapter( var activity: Activity, val rv: RecyclerView, var postList: MutableList<PostModel>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    var db: DbOperation_Post? = DbOperation_Post(activity)
    private val editCode = 100
    val VIEW_TYPE_ITEM = 1
    val VIEW_TYPE_LOADING = 0
    private var isLoading = false
    var mOnLoadMoreListener: OnLoadMoreListener? = null


    override fun onCreateViewHolder(
            viewGroup: ViewGroup,
            viewType: Int):
            RecyclerView.ViewHolder {

        if (viewType == VIEW_TYPE_ITEM) {
            val view: View = LayoutInflater.from(activity).inflate(R.layout.row_post_item, null, false)
            return PostHolder(view)

        } else if (viewType == VIEW_TYPE_LOADING) {
            val view: View =
                LayoutInflater.from(activity).inflate(R.layout.row_loading, viewGroup, false)
            return LoadingViewHolder(view)
        }
            return PostHolder(null)



        }



        override fun getItemCount(): Int {
            return postList!!.size
        }

        inner class PostHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
            var title: TextView = itemView!!.findViewById(R.id.postTitleTv)
            private var  cardView:CardView = itemView!!.findViewById(R.id.cardV)
            private var  editBut:ImageView = itemView!!.findViewById(R.id.editBtn)
            private var  deleteBut:ImageView = itemView!!.findViewById(R.id.deleteBtn)

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


    internal inner class LoadingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar1)

    }
    fun setPostListItems(postList:MutableList<PostModel>){
        this.postList = postList;
        notifyDataSetChanged()
    }


    fun setLoaded() {
        isLoading = false
    }

    private fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener?) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PostHolder) {
            holder.title.text = postList!![position].title
        }

        else if (holder is LoadingViewHolder) {
            holder.progressBar.isIndeterminate = true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (postList!![position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }



}

