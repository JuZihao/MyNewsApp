package com.example.newsapp.views

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.newsapp.databinding.NewsDetailBinding

class NewsDetailFragment: Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: NewsDetailBinding

    private val readMore = "Read More"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewsDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.backArrow.setOnClickListener{
            findNavController().navigateUp()
        }
        setClickableLink()

        binding.shareBtn.setOnClickListener{
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.type = "text/plain"
            sendIntent.putExtra("Share this:", viewModel.getNewsUrl())
            val chooser = Intent.createChooser(sendIntent,"Share using")

            startActivity(chooser)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setClickableLink() {
        try {
            val newText = StringBuilder().append(viewModel.news.value?.content).append(readMore).toString()
            val ss = SpannableString(newText)

            val clickableSpan = object : android.text.style.ClickableSpan() {
                override fun onClick(widget: View) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.getNewsUrl()))
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = Color.BLUE
                    ds.isUnderlineText = true
                }
            }
            ss.setSpan(clickableSpan, newText.length - readMore.length, newText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.content.text = ss
            binding.content.movementMethod = LinkMovementMethod.getInstance()
        } catch(e : Exception) {
            e.printStackTrace()
        }
    }

}