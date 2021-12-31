package com.listocalixto.android.mathsolar.bindings.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.data.model.User

@BindingAdapter("app:isBottomNavExpanded", "app:currentFragment")
fun View.isBottomNavExpanded(isOpenBottomNav: Boolean, currentFragment: Int) {
    when (currentFragment) {
        R.id.articlesFragment -> {
            when (this) {
                is BottomAppBar -> {
                    setNavigationIcon(R.drawable.ic_menu)
                    replaceMenu(R.menu.bottom_app_bar_home)
                    if (isOpenBottomNav) hideWithAnimatorListenerAdapter() else performShow()
                }
                is FloatingActionButton -> {
                    hideWithAnimatorListenerAdapter()
                }
            }
        }
        R.id.projectsFragment -> {
            when (this) {
                is BottomAppBar -> {
                    visibility = View.VISIBLE
                    setNavigationIcon(R.drawable.ic_menu)
                    fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                    replaceMenu(R.menu.bottom_app_bar_projects)
                    if (isOpenBottomNav) hideWithAnimatorListenerAdapter() else performShow()
                }
                is FloatingActionButton -> {
                    setImageResource(R.drawable.ic_add)
                    if (isOpenBottomNav) hideWithAnimatorListenerAdapter() else show()
                }
            }
        }
        R.id.articleDetailsFragment -> {
            when (this) {
                is BottomAppBar -> {
                    navigationIcon = null
                    fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                    replaceMenu(R.menu.bottom_app_bar_article_details)
                    if (isOpenBottomNav) hideWithAnimatorListenerAdapter() else performShow()
                }
                is FloatingActionButton -> {
                    if (isOpenBottomNav) hideWithAnimatorListenerAdapter() else show()
                }
            }
        }
        R.id.addEditProjectFragment00 -> {
            when (this) {
                is BottomAppBar -> { hideWithAnimatorListenerAdapter() }
                is FloatingActionButton -> { hideWithAnimatorListenerAdapter() }
            }

        }
        R.id.searchProjectFragment -> {
            when (this) {
                is BottomAppBar -> { hideWithAnimatorListenerAdapter() }
                is FloatingActionButton -> { hideWithAnimatorListenerAdapter() }
            }
        }
    }
}

private fun FloatingActionButton.hideWithAnimatorListenerAdapter() {
    animate().setListener(object : AnimatorListenerAdapter() {
        var isCanceled = false
        override fun onAnimationEnd(animation: Animator?) {
            if (isCanceled) return
            visibility = View.INVISIBLE
        }

        override fun onAnimationCancel(animation: Animator?) {
            isCanceled = true
        }
    })
    hide()
}

private fun BottomAppBar.hideWithAnimatorListenerAdapter() {
    performHide()
    animate().setListener(object : AnimatorListenerAdapter() {
        var isCanceled = false
        override fun onAnimationEnd(animation: Animator?) {
            if (isCanceled) return
            visibility = View.GONE
        }

        override fun onAnimationCancel(animation: Animator?) {
            isCanceled = true
        }
    })
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
