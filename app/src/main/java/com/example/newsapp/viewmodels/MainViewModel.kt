package com.example.newsapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.models.NewsListResponse
import com.example.newsapp.domain.repos.NewsRepository
import com.example.newsapp.presenter.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepo: NewsRepository
): ViewModel() {
    val searchQuery = MutableStateFlow("")
    private val _newsListUiState = MutableStateFlow<UiState<NewsListResponse>>(UiState.Loading)
    val newsListUiState: StateFlow<UiState<NewsListResponse>> = _newsListUiState

    init {

        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if(query.isEmpty()) {
                        loadNews()
                    } else {
                        searchNews(query)
                    }
                }
        }
    }

    private fun searchNews(query: String) {
        _newsListUiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val news = withContext(Dispatchers.IO) {
                    newsRepo.getSearchedNews(query)
                }
                _newsListUiState.value = UiState.Success(news)
            } catch (e: Exception) {
                _newsListUiState.value = UiState.Error(e.message.toString())
            }

        }
    }

    private fun loadNews() {
        _newsListUiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val news = withContext(Dispatchers.IO) {
                    newsRepo.getTopHeadLines()
                }
                _newsListUiState.value = UiState.Success(news)
            } catch (e: Exception) {
                Log.d("subha", e.toString())
                _newsListUiState.value = UiState.Error(e.message?: "Something Error.")
            }
        }
    }

}