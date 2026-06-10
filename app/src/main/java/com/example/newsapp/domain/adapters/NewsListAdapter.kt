package com.example.newsapp.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.data.models.Article
import com.example.newsapp.databinding.NewsItemLayoutBinding


class NewsListAdapter(
    var list: MutableList<Article>,
    val itemClick: (String) -> Unit
): RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsViewHolder {
        val binding = NewsItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NewsViewHolder,
        position: Int
    ) {
        val article = list[position]
        holder.itemBinding.tvTitle.text =
            article.title ?: "No Title"

        holder.itemBinding.tvDescription.text =
            article.description ?: "No Description"

        holder.itemBinding.tvAuthor.text =
            article.author ?: "Unknown Author"

        Glide.with(holder.itemBinding.root)
            .load(article.urlToImage)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.itemBinding.ivThumbnail)

        holder.itemBinding.root.setOnClickListener {
            article.url?.let { it ->
                itemClick(it)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList(list: List<Article>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(val itemBinding: NewsItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}
