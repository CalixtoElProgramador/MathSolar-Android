package com.listocalixto.android.mathsolar.ui.main

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.math.MathUtils
import com.google.android.material.navigation.NavigationView
import com.google.android.material.transition.MaterialFadeThrough
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.BottomNavDrawerMainLayoutHeaderBinding
import com.listocalixto.android.mathsolar.databinding.ParentFragmentMainBinding
import com.listocalixto.android.mathsolar.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainParentFragment : Fragment(R.layout.parent_fragment_main) {

    private val viewModel by activityViewModels<MainViewModel>()

    private lateinit var binding: ParentFragmentMainBinding
    private lateinit var headerBottomNavBinding: BottomNavDrawerMainLayoutHeaderBinding

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<NavigationView>

    val currentNavigationFragment: Fragment?
        get() = childFragmentManager.findFragmentById(R.id.nav_host_main)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding(view)
        setupLayoutHeaderBinding()

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavDrawerMain.setupWithNavController(navController)
        setupDestinationChangeListener(navController)

        viewModel.currentFragment.observe(viewLifecycleOwner, {
            currentNavigationFragment?.let {
                it.exitTransition = MaterialFadeThrough().apply {
                    duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                }
            }
        })

        setupBottomSheetBehavior()
        binding.bottomAppBar.setNavigationOnClickListener { showBottomNavDrawer() }
        viewModel.bottomNavExpandedState.observe(viewLifecycleOwner, {
            if (it) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        })

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    viewModel.isBottomNavExpanded(false)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val baseColor = Color.BLACK
                // 60% opacity
                val baseAlpha =
                    ResourcesCompat.getFloat(resources, R.dimen.material_emphasis_medium)
                // Map slideOffset from [-1.0, 1.0] to [0.0, 1.0]
                val offset = (slideOffset - (-1f)) / (1f - (-1f)) * (1f - 0f) + 0f
                val alpha = MathUtils.lerp(0f, 255f, offset * baseAlpha).toInt()
                val color = Color.argb(alpha, baseColor.red, baseColor.green, baseColor.blue)
                viewModel.setScrimBackground(color)
            }
        })
    }


    private fun setupLayoutHeaderBinding() {
        headerBottomNavBinding = BottomNavDrawerMainLayoutHeaderBinding.bind(
            binding.bottomNavDrawerMain.getHeaderView(0)
        ).apply {
            lifecycleOwner = this@MainParentFragment.viewLifecycleOwner
            mainViewModel = viewModel
        }
    }

    private fun setupBinding(view: View) {
        binding = ParentFragmentMainBinding.bind(view).apply {
            lifecycleOwner = this@MainParentFragment.viewLifecycleOwner
            mainViewModel = viewModel
        }
    }

    private fun setupDestinationChangeListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.articlesFragment -> {
                    viewModel.setCurrentFragment(R.id.articlesFragment)
                }
                R.id.articleDetailsFragment -> {
                    viewModel.setCurrentFragment(R.id.articleDetailsFragment)
                }
                R.id.projectsFragment -> {
                    viewModel.setCurrentFragment(R.id.projectsFragment)
                }
                R.id.addEditProjectFragment00 -> {
                    viewModel.setCurrentFragment(R.id.addEditProjectFragment00)
                }
                R.id.searchProjectFragment -> {
                    viewModel.setCurrentFragment(R.id.searchProjectFragment)
                }
            }
        }
    }

    private fun setupBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomNavDrawerMain)
    }

    private fun showBottomNavDrawer() {
        viewModel.isBottomNavExpanded(true)
    }

    companion object {
        const val TAG = "MainParentFragment"
    }

}