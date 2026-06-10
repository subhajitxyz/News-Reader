package com.example.newsapp.data.models

data class NewsListResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)