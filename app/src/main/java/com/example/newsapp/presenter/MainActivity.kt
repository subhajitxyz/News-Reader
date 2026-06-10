package com.example.newsapp.presenter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.data.models.NewsListResponse
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.domain.adapters.NewsListAdapter
import com.example.newsapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

const val URL_INTENT_KEY = "url_intent_key"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val newsListAdapter = NewsListAdapter(mutableListOf()) { url ->
        startActivity(Intent(this, WebViewActivity::class.java ).apply {
            putExtra(URL_INTENT_KEY, url)
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mainBinding.recyclerView.adapter = newsListAdapter

        mainBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let {
                    viewModel.searchQuery.value = p0
                }
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let {
                    viewModel.searchQuery.value = p0
                }
                return true
            }
        })


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newsListUiState.collect { it ->
                    showUI(it)
                }
            }
        }
    }


    private fun showUI(state: UiState<NewsListResponse>) {
        when(state) {
            UiState.Loading -> {
                mainBinding.recyclerView.visibility = View.GONE
                mainBinding.progressCircular.visibility = View.VISIBLE
                mainBinding.errorTextview.visibility = View.GONE
            }
            is UiState.Success<NewsListResponse> -> {
                mainBinding.recyclerView.visibility = View.VISIBLE
                mainBinding.progressCircular.visibility = View.GONE
                mainBinding.errorTextview.visibility = View.GONE

                state.data.articles?.let { it ->
                    newsListAdapter.submitList(it)
                }
            }
            is UiState.Error -> {
                mainBinding.recyclerView.visibility = View.GONE
                mainBinding.progressCircular.visibility = View.GONE
                mainBinding.errorTextview.visibility = View.VISIBLE
                mainBinding.errorTextview.text = state.e
            }
        }
    }
}