package com.example.newsapp.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.model.NewsArticle
import androidx.recyclerview.widget.ListAdapter
import com.example.newsapp.databinding.LinearViewItemBinding


class NewsListAdapter (val clickListener: NewListListener) :
        ListAdapter<NewsArticle, NewsListAdapter.NewListViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<NewsArticle>() {

        override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
            return oldItem.content == newItem.content && oldItem.description == newItem.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NewListViewHolder(
            LinearViewItemBinding.inflate(layoutInflater)
        )
    }

    override fun onBindViewHolder(holder: NewListViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news, clickListener)
    }

    class NewListViewHolder(
        var binding: LinearViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            news: NewsArticle,
            clickListener: NewListListener) {
            binding.news = news
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

}


class NewListListener(val clickListener: (news: NewsArticle) -> Unit) {
    fun onClick(news: NewsArticle) = clickListener(news)
}