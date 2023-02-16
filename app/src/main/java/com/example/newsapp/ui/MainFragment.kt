package com.example.newsapp.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.newsList.adapter = NewsListAdapter()
        binding.horizontalList.adapter = NewsListAdapter()
//            NewsListAdapter(NewListListener {news ->
//            viewModel.onNewsClicked(news)
//            findNavController()
//                .navigate(R.id.action_mainFragment_to_placeholder)})
        return binding.root
    }

}