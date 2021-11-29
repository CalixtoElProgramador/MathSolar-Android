package com.listocalixto.android.mathsolar.ui.main.articles

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentArticlesBinding
import com.listocalixto.android.mathsolar.presentation.main.articles.ArticlesViewModel
import com.listocalixto.android.mathsolar.ui.main.articles.adapter.HomeAdapter
import com.listocalixto.android.mathsolar.ui.main.projects.ProjectsFragment
import com.listocalixto.android.mathsolar.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticlesFragment : Fragment(R.layout.fragment_articles) {

    private val viewModel by activityViewModels<ArticlesViewModel>()

    private lateinit var binding: FragmentArticlesBinding
    private lateinit var listAdapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticlesBinding.bind(view).apply {
            articlesViewModel = viewModel
            setupListAdapter(this)
        }
        binding.lifecycleOwner = this.viewLifecycleOwner

        activity?.findViewById<BottomAppBar>(R.id.bottomAppBar).also {
            binding.listArticles.hideOrShowBottomAppBarOnRecyclerScrolled(it)
            it?.onMenuItemSelected(viewModel)
        }

        viewModel.setLifecycleOwnerToObserveNetworkConnection(viewLifecycleOwner)

        binding.chipGroupArticleTopics.setOnCheckedChangeListener { group, checkedId ->
            viewModel.setFiltering(ArticleFilterType.ALL_ARTICLES)
            when (checkedId) {
                R.id.chip_solar_power -> {
                    viewModel.changeTopic(ArticleTopic.SOLAR_POWER)
                }
                R.id.chip_solar_panels -> {
                    viewModel.changeTopic(ArticleTopic.SOLAR_PANELS)
                }
                R.id.chip_thermal_systems -> {
                    viewModel.changeTopic(ArticleTopic.THERMAL_SYSTEMS)
                }
                R.id.chip_climate_change -> {
                    viewModel.changeTopic(ArticleTopic.CLIMATE_CHANGE)
                }
                R.id.chip_environment -> {
                    viewModel.changeTopic(ArticleTopic.ENVIRONMENT)
                }
                else -> {
                    viewModel.changeTopic(ArticleTopic.SUSTAINABILITY)
                }
            }
        }

        setupNavigation()

    }

    private fun setupNavigation() {
        viewModel.apply {
            openArticleEvent.observe(viewLifecycleOwner, EventObserver {
                openArticleDetailsFragment(it)
            })
        }
    }

    private fun openArticleDetailsFragment(articleId: String) {
        val action = ArticlesFragmentDirections.actionHomeFragmentToArticleDetailsFragment(articleId)
        findNavController().navigate(action)
    }

    private fun setupListAdapter(binding: FragmentArticlesBinding) {
        val viewModel = binding.articlesViewModel
        viewModel?.let {
            listAdapter = HomeAdapter(it)
            binding.listArticles.adapter = listAdapter
        } ?: Log.d(
            ProjectsFragment.TAG,
            "setupListAdapter: ViewModel not initialized when attempting to set up adapter."
        )
    }
}