package com.example.newsapp.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentMainBinding

class MainFragment : Fragment(), View.OnClickListener{

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.newsList.adapter = NewsListAdapter(NewListListener { news ->
            viewModel.onNewsClicked(news)
            findNavController()
                .navigate(R.id.action_mainFragment_to_newsDetailFragment2)})
        binding.horizontalList.adapter = NewsCardAdapter(NewCardsListener { news ->
            viewModel.onNewsClicked(news)
            findNavController()
                .navigate(R.id.action_mainFragment_to_newsDetailFragment2)})

        binding.seeAll.setOnClickListener{
            viewModel.changeNewsCategory(NewsCategories.LATEST)
            findNavController()
                .navigate(R.id.action_mainFragment_to_allNewsFragment)
        }

        binding.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank()) {
                    return false
                } else {
                    findNavController()
                        .navigate(R.id.action_mainFragment_to_allNewsFragment)
                    viewModel.getQueryNews(query)
                    binding.searchBar.setQuery("", false)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        }
        )

        binding.business.setOnClickListener(this)
        binding.entertainment.setOnClickListener(this)
        binding.general.setOnClickListener(this)
        binding.science.setOnClickListener(this)
        binding.health.setOnClickListener(this)
        binding.technology.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.business -> viewModel.changeNewsCategory(NewsCategories.BUSINESS)
            R.id.entertainment -> viewModel.changeNewsCategory(NewsCategories.ENTERTAINMENT)
            R.id.general -> viewModel.changeNewsCategory(NewsCategories.GENERAL)
            R.id.science -> viewModel.changeNewsCategory(NewsCategories.SCIENCE)
            R.id.health -> viewModel.changeNewsCategory(NewsCategories.HEALTH)
            R.id.technology -> viewModel.changeNewsCategory(NewsCategories.TECHNOLOGY)
            else-> viewModel.changeNewsCategory(NewsCategories.LATEST)
        }
    }
}