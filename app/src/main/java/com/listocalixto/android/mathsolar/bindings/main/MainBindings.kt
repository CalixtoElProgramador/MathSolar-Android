package com.listocalixto.android.mathsolar.bindings.main

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.data.model.User
import com.listocalixto.android.mathsolar.presentation.main.home.HomeViewModel
import com.listocalixto.android.mathsolar.utils.ArticleFilterType

@BindingAdapter("app:isBottomNavExpanded", "app:currentFragment")
fun View.isBottomNavExpanded(isOpenBottomNav: Boolean, currentFragment: Int) {
    when (currentFragment) {
        R.id.homeFragment -> {
            when (this) {
                is BottomAppBar -> {
                    replaceMenu(R.menu.bottom_app_bar_home)
                    if (isOpenBottomNav) performHide() else performShow()
                }
                is FloatingActionButton -> {
                    hide()
                }
            }
        }
        R.id.projectsFragment -> {
            when (this) {
                is BottomAppBar -> {
                    fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                    replaceMenu(R.menu.bottom_app_bar_projects)
                    if (isOpenBottomNav) performHide() else performShow()
                }
                is FloatingActionButton -> {
                    setImageResource(R.drawable.ic_add)
                    if (isOpenBottomNav) hide() else show()
                }
            }
        }
        R.id.articleDetailsFragment -> {
            when (this) {
                is BottomAppBar -> {
                    fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                    replaceMenu(R.menu.bottom_app_bar_article_details)
                    if (isOpenBottomNav) performHide() else performShow()
                }
                is FloatingActionButton -> {
                    if (isOpenBottomNav) hide() else show()
                }
            }
        }
    }
}

@BindingAdapter("app:backgroundColor")
fun FrameLayout.backgroundColor(color: Int?) {
    color?.let {
        this.setBackgroundColor(it)
    }
}

@BindingAdapter("app:userData")
fun View.setUserData(user: User?) {
    user?.let {
        val userName = "${it.name} ${it.lastname}"
        when {
            this is TextView && id == R.id.userName -> {
                text = userName
            }
            this is TextView && id == R.id.userEmail -> {
                text = it.email
            }
            this is ImageView -> {
                load(it.profilePictureUrl) {
                    crossfade(600)
                    error(R.drawable.ic_error)
                    placeholder(R.drawable.ic_error_placeholder)
                }
            }
            else -> {
            }
        }
    }
}
