package com.example.paging3.retrofit

import com.example.paging3.model.QuateList
import retrofit2.http.GET
import retrofit2.http.Query

interface QuateAPI {

    @GET("/quotes")
    suspend fun getQuates(@Query("page") Page:Int): QuateList
}