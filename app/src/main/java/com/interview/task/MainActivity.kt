package com.interview.task

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.interview.task.Api.ApiClient.getClient
import com.interview.task.Api.ApiInterface
import com.interview.task.LocalDb.DbOperation_Post
import com.interview.task.Model.PostModel
import com.interview.task.activities.ActivityBase
import com.interview.task.activities.AddPostActivity
import com.interview.task.adapters.PostAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : ActivityBase() {
    private lateinit var recyclerPost: RecyclerView
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var addPost: FloatingActionButton

    private  lateinit var postAdapter: PostAdapter
    var postList: ArrayList<PostModel> = ArrayList()
    var db: DbOperation_Post? = null


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerPost = findViewById(R.id.recyclerPost)
        swipeContainer = findViewById(R.id.swipe_container)
        addPost = findViewById(R.id.addPost)

        postAdapter = PostAdapter(postList,this@MainActivity)

        val llm = LinearLayoutManager(getActiviy())
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerPost.layoutManager = llm

        recyclerPost.adapter = postAdapter

        checkInternet()

        db = DbOperation_Post(this)

        swipeContainer.setOnRefreshListener {
            getPosts();
        }


        addPost.setOnClickListener {
            val details = Intent(getActiviy(), AddPostActivity::class.java)
            startActivity(details)

        }




    }


    fun getPosts() {
        swipeContainer.isRefreshing = true
        val apiService = getClient()!!.create(ApiInterface::class.java)
        val apiInterface = apiService.getPosts()
        apiInterface.enqueue( object : Callback<List<PostModel>> {
            override fun onResponse(
                call: Call<List<PostModel>>?,
                response: Response<List<PostModel>>?
            ) {

                if (response?.body() != null){
                    swipeContainer.isRefreshing=false

                    postAdapter.setPostListItems(response.body() as ArrayList<PostModel>)

                }


            }

            override fun onFailure(call: Call<List<PostModel>>?, t: Throwable?) {

            }
        })
    }


    private fun checkInternet() {
        val conMgr =
           getActiviy().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = conMgr.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            getPosts()
        } else {
            db!!.getAllPosts();

        }
    }






}