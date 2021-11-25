package com.listocalixto.android.mathsolar.ui.main.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentHomeBinding
import com.listocalixto.android.mathsolar.presentation.main.home.HomeViewModel
import com.listocalixto.android.mathsolar.ui.main.home.adapter.HomeAdapter
import com.listocalixto.android.mathsolar.ui.main.projects.ProjectsFragment
import com.listocalixto.android.mathsolar.ui.main.projects.adapter.ProjectsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel by activityViewModels<HomeViewModel>()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var listAdapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view).also {
            it.lifecycleOwner = this.viewLifecycleOwner
            it.homeViewModel = viewModel
            setupListAdapter(it)
        }

        viewModel.dataLoading.observe(viewLifecycleOwner, {
            if (it) {
                binding.listArticles.showShimmer()
            } else {
                binding.listArticles.hideShimmer()
            }
        })

        val bottomAppBar = activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)
        binding.listArticles.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    bottomAppBar?.performHide()
                }
                if (dy < 0) {
                    bottomAppBar?.performShow()
                }
            }
        })

    }

    private fun setupListAdapter(binding: FragmentHomeBinding) {
        val viewModel = binding.homeViewModel
        viewModel?.let {
            listAdapter = HomeAdapter(it)
            binding.listArticles.adapter = listAdapter
        } ?: Log.d(
            ProjectsFragment.TAG,
            "setupListAdapter: ViewModel not initialized when attempting to set up adapter."
        )
    }
}