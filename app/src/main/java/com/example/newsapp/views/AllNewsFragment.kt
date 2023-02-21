package com.example.newsapp.views

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

        binding.newsList.adapter = NewsListAdapter(NewListListener {news ->
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
                dialog.dismiss()
            }
            // below line is use to set cancelable to avoid
            // closing of dialog box when clicking on the screen.
            // dialog.setCancelable(false)

            // val btnRecommend = view.findViewById<Button>(R.id.recommended)

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