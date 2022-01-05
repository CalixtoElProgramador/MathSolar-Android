package com.listocalixto.android.mathsolar.ui.main.articles.details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.color.MaterialColors
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.transition.MaterialContainerTransform
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentArticleDetailsBinding
import com.listocalixto.android.mathsolar.presentation.main.articles.ArticlesViewModel
import com.listocalixto.android.mathsolar.presentation.main.articles.article_details.ArticleDetailsViewModel
import com.listocalixto.android.mathsolar.utils.EventObserver
import com.listocalixto.android.mathsolar.utils.setFunctionOnClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailsFragment : Fragment(R.layout.fragment_article_details) {

    private val viewModel by activityViewModels<ArticleDetailsViewModel>()
    private val articlesViewModel by activityViewModels<ArticlesViewModel>()
    private val args: ArticleDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentArticleDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform(requireContext(), true).apply {
            drawingViewId = R.id.nav_host_main
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
            fadeProgressThresholds = MaterialContainerTransform.ProgressThresholds(0f, 1f)
            interpolator = FastOutSlowInInterpolator()
            setAllContainerColors(
                MaterialColors.getColor(
                    requireContext(),
                    R.attr.colorSurface,
                    Color.TRANSPARENT
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }
        setupFab()
        binding = FragmentArticleDetailsBinding.bind(view)

        binding.run {
            lifecycleOwner = this@ArticleDetailsFragment.viewLifecycleOwner
            articleDetailsViewModel = viewModel
            articlesViewModel = this@ArticleDetailsFragment.articlesViewModel
        }
        viewModel.start(args.articleId)

        val bottomAppbar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
        binding.nestedScrollArticleDetails.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            val dy = scrollY - oldScrollY
            if (dy > 0) {
                bottomAppbar?.performHide()
            }
            if (dy < 0) {
                bottomAppbar?.performShow()
            }
        }

        viewModel.backEvent.observe(viewLifecycleOwner, EventObserver {
            activity?.onBackPressed()
        })

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