package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSeeAllBinding
import com.example.newsapp.ui.*
import com.google.android.material.bottomsheet.BottomSheetDialog

class AllNewsFragment: Fragment(), View.OnClickListener {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding : FragmentSeeAllBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeeAllBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.newsList.adapter = NewsListAdapter(NewListListener { news ->
            viewModel.onNewsClicked(news)
            findNavController()
                .navigate(R.id.action_allNewsFragment_to_newsDetailFragment2)})

        binding.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank()) {
                    return false
                } else {
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

        // Filter Button
        binding.filter.setOnClickListener{

            // on below line we are creating a new bottom sheet dialog.
            val dialog = BottomSheetDialog(it.context)
            var filterType: SortByTypes = SortByTypes.DEFAULT

            // on below line we are inflating a layout file which we have created.
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

            // on below line we are creating a variable for our button
            // which we are using to dismiss our dialog.
            val btnClose = view.findViewById<Button>(R.id.save)

            // on below line we are adding on click listener
            // for our dismissing the dialog button.
            btnClose.setOnClickListener {
                // on below line we are calling a dismiss
                // method to close our dialog.
                viewModel.applyFilter(filterType)
                binding.filter.isSelected = true
                dialog.dismiss()
            }
            // below line is use to set cancelable to avoid
            // closing of dialog box when clicking on the screen.
            // dialog.setCancelable(false)

            dialog.setOnCancelListener {
                binding.filter.isSelected = false
            }

            val btnReset = view.findViewById<Button>(R.id.reset)
            val btnRecommend = view.findViewById<Button>(R.id.recommended)
            val btnLatest = view.findViewById<Button>(R.id.latest)
            val btnViews = view.findViewById<Button>(R.id.most_viewed)

            btnReset.setOnClickListener{
                filterType = SortByTypes.DEFAULT
                btnRecommend.isSelected = false
                btnLatest.isSelected = false
                btnViews.isSelected = false
            }

            btnRecommend.setOnClickListener{
                filterType = SortByTypes.RECOMMENDED
                btnRecommend.isSelected = true
                btnLatest.isSelected = false
                btnViews.isSelected = false
            }

            btnLatest.setOnClickListener{
                filterType = SortByTypes.LATEST
                btnRecommend.isSelected = false
                btnLatest.isSelected = true
                btnViews.isSelected = false
            }

            btnViews.setOnClickListener{
                filterType = SortByTypes.VIEW
                btnRecommend.isSelected = false
                btnLatest.isSelected = false
                btnViews.isSelected = true
            }

            // on below line we are setting
            // content view to our view.
            dialog.setContentView(view)

            // on below line we are calling
            // a show method to display a dialog.
            dialog.show()
        }

        // Category Buttons
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
            R.id.business -> {
                viewModel.changeNewsCategory(NewsCategories.BUSINESS)
                clearCategorySelected()
                binding.business.isSelected = true
            }
            R.id.entertainment -> {
                viewModel.changeNewsCategory(NewsCategories.ENTERTAINMENT)
                clearCategorySelected()
                binding.entertainment.isSelected = true
            }
            R.id.general -> {
                viewModel.changeNewsCategory(NewsCategories.GENERAL)
                clearCategorySelected()
                binding.general.isSelected = true
            }
            R.id.science -> {
                viewModel.changeNewsCategory(NewsCategories.SCIENCE)
                clearCategorySelected()
                binding.science.isSelected = true
            }
            R.id.health -> {
                viewModel.changeNewsCategory(NewsCategories.HEALTH)
                clearCategorySelected()
                binding.health.isSelected = true
            }
            R.id.technology -> {
                viewModel.changeNewsCategory(NewsCategories.TECHNOLOGY)
                clearCategorySelected()
                binding.technology.isSelected = true
            }
            else-> viewModel.changeNewsCategory(NewsCategories.LATEST)
        }
    }
    private fun clearCategorySelected() {
        binding.business.isSelected = false
        binding.entertainment.isSelected = false
        binding.general.isSelected = false
        binding.science.isSelected = false
        binding.health.isSelected = false
        binding.technology.isSelected = false
    }

}