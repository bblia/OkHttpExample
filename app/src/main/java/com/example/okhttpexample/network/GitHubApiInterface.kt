package com.example.okhttpexample.network

import com.example.okhttpexample.models.Repository
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.ArrayList

interface GitHubApiInterface {
    @GET("/users/{user}/repos")
    fun getRepository(@Path("user") userName: String?): Call<ArrayList<Repository>>
}
