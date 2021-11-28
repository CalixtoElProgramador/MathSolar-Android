package com.listocalixto.android.mathsolar.ui.main.home.article_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentArticleDetailsBinding
import com.listocalixto.android.mathsolar.presentation.main.home.HomeViewModel
import com.listocalixto.android.mathsolar.presentation.main.home.article_details.ArticleDetailsViewModel
import com.listocalixto.android.mathsolar.utils.setFunctionOnClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailsFragment : Fragment(R.layout.fragment_article_details) {

    private val viewModel by activityViewModels<ArticleDetailsViewModel>()
    private val articlesViewModel by activityViewModels<HomeViewModel>()
    private val args: ArticleDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentArticleDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFab()
        binding = FragmentArticleDetailsBinding.bind(view).apply {
            articleDetailsViewModel = viewModel
            homeViewModel = articlesViewModel
        }
        binding.lifecycleOwner = this.viewLifecycleOwner
        viewModel.start(args.articleId)

        binding.toolbarArticleDetails.setNavigationOnClickListener { activity?.onBackPressed() }

    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab_main)?.apply {
            setFunctionOnClick(viewModel)
            updateBookmarkIcon()
        }
    }

    private fun FloatingActionButton.updateBookmarkIcon() {
        viewModel.bookmark.observe(viewLifecycleOwner, {
            if (it) {
                setImageResource(R.drawable.ic_bookmark)
            } else {
                setImageResource(R.drawable.ic_bookmark_border)
            }
        })
    }
}