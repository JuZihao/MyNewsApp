package com.example.newsapp

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.newsapp.model.NewsArticle
import com.example.newsapp.ui.NewsApiStatus
import com.example.newsapp.ui.NewsCardAdapter
import com.example.newsapp.ui.NewsListAdapter

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        if (imgUri == null) {
            imgView.load(R.drawable.ic_broken_image)
        } else {
            imgView.load(imgUri) {
                this.placeholder(R.drawable.loading_img)
            }
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,
                     data: List<NewsArticle>?) {
    val adapter = recyclerView.adapter as NewsListAdapter
    adapter.submitList(data)
}

@BindingAdapter("horizontalnews")
fun bindHorizontalRecyclerView(recyclerView: RecyclerView,
                     data: List<NewsArticle>?) {
    val adapter = recyclerView.adapter as NewsCardAdapter
    adapter.submitList(data)
}

@BindingAdapter("apiStatus")
fun bindStatus(statusImageView: ImageView, status: NewsApiStatus) {
    when(status) {
        NewsApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        NewsApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        NewsApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
    }
}