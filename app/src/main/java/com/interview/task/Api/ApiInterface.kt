package com.interview.task.Api

import com.interview.task.Model.PostModel
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @GET("photos")
    fun getPosts():  Call<MutableList<PostModel>?>


}