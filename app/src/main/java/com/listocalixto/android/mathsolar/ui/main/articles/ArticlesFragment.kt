package com.listocalixto.android.mathsolar.ui.main.articles

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentArticlesBinding
import com.listocalixto.android.mathsolar.presentation.main.articles.ArticlesViewModel
import com.listocalixto.android.mathsolar.ui.main.articles.adapter.HomeAdapter
import com.listocalixto.android.mathsolar.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticlesFragment : Fragment(R.layout.fragment_articles) {

    private val viewModel by activityViewModels<ArticlesViewModel>()

    private lateinit var binding: FragmentArticlesBinding
    private lateinit var listAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }
        binding = FragmentArticlesBinding.bind(view).apply {
            articlesViewModel = viewModel
            setupListAdapter(this)
        }

        binding.lifecycleOwner = this.viewLifecycleOwner

        activity?.findViewById<BottomAppBar>(R.id.bottomAppBar).also {
            binding.listArticles.hideOrShowBottomAppBarOnRecyclerScrolled(it, viewModel)
            it?.onMenuItemSelected(viewModel)
        }

        viewModel.userHasInternet.observe(viewLifecycleOwner, {
            if (it) {
                if (viewModel.getReferenceInternet()) {
                    viewModel.showSnackbarMessage(R.string.internet_connection_restored)
                    viewModel.updateReferenceInternet(false)
                }
            } else {
                if (!viewModel.getReferenceInternet()) {
                    viewModel.showSnackbarMessage(R.string.no_internet_connection)
                    viewModel.updateReferenceInternet(true)
                }
            }
        })

        setupSnackbar()
        setupNavigation()

    }

    private fun setupNavigation() {
        viewModel.apply {
            openArticleEvent.observe(viewLifecycleOwner, EventObserver {
                navigateToArticleDetailsFragment(it.cardView, it.itemId)
            })
        }
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(
            this.viewLifecycleOwner,
            viewModel.snackbarText,
            Snackbar.LENGTH_INDEFINITE,
            binding.guideline
        )
    }

    private fun navigateToArticleDetailsFragment(cardView: View, articleId: String) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()

        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_medium_02).toLong()
        }
        val articleCardDetailTransitionName = getString(R.string.article_card_detail_transition_name)
        val extras = FragmentNavigatorExtras(cardView to articleCardDetailTransitionName)
        val action = ArticlesFragmentDirections.actionHomeFragmentToArticleDetailsFragment(articleId)
        findNavController().navigate(action, extras)
    }

    private fun setupListAdapter(binding: FragmentArticlesBinding) {
        val viewModel = binding.articlesViewModel
        viewModel?.let {
            listAdapter = HomeAdapter(it)
            binding.listArticles.adapter = listAdapter
        } ?: Log.d(
            TAG,
            "setupListAdapter: ViewModel not initialized when attempting to set up adapter."
        )
    }

    companion object {
        private const val TAG = "ArticlesFragment"
    }

}