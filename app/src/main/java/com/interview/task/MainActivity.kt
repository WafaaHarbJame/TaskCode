package com.interview.task

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interview.task.Api.ApiClient.getClient
import com.interview.task.Api.ApiInterface
import com.interview.task.LocalDb.DbOperation_Post
import com.interview.task.Model.PostModel
import com.interview.task.Utils.SharedPManger
import com.interview.task.activities.ActivityBase
import com.interview.task.activities.AddPostActivity
import com.interview.task.adapters.PostAdapter
import com.interview.task.classes.Constants
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : ActivityBase() {

    private lateinit var postAdapter: PostAdapter
    public var postList: MutableList<PostModel>? = mutableListOf()
    var db: DbOperation_Post? = null
    private var sharedPManger: SharedPManger? = null
    private val editCode = 100
    private val addCode = 200
    var isLoading = false

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerPost.layoutManager = LinearLayoutManager(getActiviy())
        postAdapter = PostAdapter(this, recyclerPost, postList)
        recyclerPost.adapter = postAdapter
        sharedPManger = SharedPManger(getActiviy())


        db = DbOperation_Post(this)

        swipeContainer.setOnRefreshListener {
            checkInternet()
        }

        checkInternet()

        addPost.setOnClickListener {
            val add = Intent(getActiviy(), AddPostActivity::class.java)
            add.putExtra(Constants.EDIT_POST, false)
            startActivityForResult(add, addCode)
        }
    }

    private fun getPosts() {
        swipeContainer.isRefreshing = true
        val apiService = getClient()!!.create(ApiInterface::class.java)
        val apiInterface = apiService.getPosts()
        apiInterface.enqueue(object : Callback<MutableList<PostModel>?> {
            override fun onResponse(
                call: Call<MutableList<PostModel>?>?,
                response: Response<MutableList<PostModel>?>?
            ) {

                swipeContainer.isRefreshing = false
                if (response?.isSuccessful == true) {
                    postList = response.body()

                    if (postList != null) {
                        postAdapter.setPostListItems(postList!!)
                    }
                    MyTask().execute()

                }

            }

            override fun onFailure(call: Call<MutableList<PostModel>?>?, t: Throwable?) {

            }
        })
    }

    private fun checkInternet() {
        val conMgr =
            getActiviy().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = conMgr.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {

            val firstRun = sharedPManger?.getDataBool(Constants.FIRST_RUN)

            if (firstRun == false) {
                sharedPManger!!.SetData(Constants.FIRST_RUN, true)
                getPosts()

            } else {
                readPostLocal()

            }


        } else {

            readPostLocal()

        }
    }

    private fun checkFirstRun() {
        val firstRun = sharedPManger?.getDataBool(Constants.FIRST_RUN)
        if (firstRun == false) {
            sharedPManger!!.SetData(Constants.FIRST_RUN, true)
        }
    }

    private fun readPostLocal() {

        postList = db!!.getAllPosts()

        swipeContainer.isRefreshing = false
        if (postList!!.isNotEmpty()) {
            postAdapter.setPostListItems(postList!!)
            postList?.sortByDescending { it.id }

        } else {
            getPosts()
        }

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == editCode && resultCode == Activity.RESULT_OK) {
            readPostLocal()
        }
        if (requestCode == addCode && resultCode == Activity.RESULT_OK) {
            readPostLocal()
        }
    }


    inner class MyTask() : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean? {
            // ...
            return saveDataLocal()
        }

        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if (result == true) {
                Log.d("add posts", "Added" + getString(R.string.add_success))
            } else {
                Log.d("add posts", "not Added" + getString(R.string.add_failed))
            }

        }
    }


    fun saveDataLocal(): Boolean {
        var isAllSuccess = false
        if (postList!!.size > 0) {
            for (i in postList!!.indices) {

                val post = PostModel()
                post.title = postList!![i].title
                post.id = postList!![i].id
                post.thumbnailUrl = postList!![i].thumbnailUrl
                post.thumbnailImage = null
                post.type = 1

                isAllSuccess = db!!.insert(post)
                if (!isAllSuccess)
                    break

            }

        }

        return isAllSuccess
    }



}