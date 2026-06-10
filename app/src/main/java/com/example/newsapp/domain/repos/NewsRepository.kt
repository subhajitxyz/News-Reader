package com.example.newsapp.domain.repos

import android.util.Log
import com.example.newsapp.data.api.NewsApiService
import com.example.newsapp.data.models.NewsListResponse
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApiService: NewsApiService
) {
    suspend fun getTopHeadLines(): NewsListResponse {
       return newsApiService.getTopHeadlines()
    }

    suspend fun getSearchedNews(query: String): NewsListResponse {
        return newsApiService.getNews(query = query)
    }

}