package com.example.newsapp.data.api

import com.example.newsapp.data.models.NewsListResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "bda8816de8eb45dfa25628e8e6fa7285"
interface NewsApiService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsListResponse

    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String = "us",
        @Query("sortBy") sortBy: String = "popularity",
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsListResponse
}