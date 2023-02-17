package com.example.newsapp.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.HorizontalViewItemBinding
import com.example.newsapp.model.NewsArticle

class NewsCardAdapter(val clickListener: NewCardsListener) :
    ListAdapter<NewsArticle, NewsCardAdapter.NewCardsViewHolder>(NewsListAdapter) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsCardAdapter.NewCardsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NewCardsViewHolder(
            HorizontalViewItemBinding.inflate(layoutInflater)
        )
    }

    override fun onBindViewHolder(holder: NewsCardAdapter.NewCardsViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news, clickListener)
    }

    class NewCardsViewHolder(
        var binding: HorizontalViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            news: NewsArticle,
            clickListener: NewCardsListener) {
            binding.news = news
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }
}

class NewCardsListener(val clickListener: (news: NewsArticle) -> Unit) {
    fun onClick(news: NewsArticle) = clickListener(news)
}