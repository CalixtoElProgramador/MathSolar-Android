package com.listocalixto.android.mathsolar.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.core.view.get
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.color.MaterialColors
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.listocalixto.android.mathsolar.utils.SnackbarType.DEFAULT
import com.listocalixto.android.mathsolar.utils.SnackbarType.GO_TO_SETTINGS
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.listocalixto.android.mathsolar.BuildConfig
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.data.model.PVProject
import com.listocalixto.android.mathsolar.data.model.PVProjectRemote
import com.listocalixto.android.mathsolar.presentation.main.articles.ArticlesViewModel
import com.listocalixto.android.mathsolar.presentation.main.articles.article_details.ArticleDetailsViewModel
import com.listocalixto.android.mathsolar.presentation.main.projects.ProjectsViewModel

fun PVProject.asRemoteModel(downloadUrl: String, userUid: String): PVProjectRemote =
    PVProjectRemote(
        name = this.name,
        location = this.location,
        createdAt = this.createdAt,
        description = this.description,
        imageUrl = downloadUrl,
        type = this.type.ordinal,
        deleted = this.isDeleted,
        favorite = this.isFavorite,
        ambTemperature = this.ambTemperature,
        batteryCapacity = this.batteryCapacity,
        batteryDepthDischarge = this.batteryDepthDischarge,
        batteryVoltage = this.batteryVoltage,
        daysAutonomy = this.daysAutonomy,
        inverterEfficiency = this.inverterEfficiency,
        loadMax = this.loadMax,
        loadMonth = this.loadMonth,
        latitude = this.latitude,
        longitude = this.longitude,
        moduleCurrent = this.moduleCurrent,
        modulePower = this.modulePower,
        moduleVoltage = this.moduleVoltage,
        sunHours = this.sunHours,
        uid = this.uid,
        userUid = userUid
    )

fun PVProjectRemote.asLocalModel(): PVProject =
    PVProject(
        this.name,
        this.location,
        this.createdAt,
        this.description,
        this.imageUrl,
        type = PVProjectType.values()[this.type],
        this.deleted,
        this.favorite,
        this.ambTemperature,
        this.batteryCapacity,
        this.batteryDepthDischarge,
        this.batteryVoltage,
        this.daysAutonomy,
        this.inverterEfficiency,
        this.loadMax,
        this.loadMonth,
        this.latitude,
        this.longitude,
        this.moduleCurrent,
        this.modulePower,
        this.moduleVoltage,
        this.sunHours,
        this.uid
    )

fun Article.toDatabaseModel(topic: ArticleTopic): Article = Article(
    cleanUrl = this.cleanUrl,
    id = this.id,
    link = this.link,
    media = this.media,
    publishedDate = this.publishedDate,
    summary = this.summary,
    title = this.title,
    bookmark = this.bookmark,
    viewed = this.viewed,
    topic
)

fun List<PVProjectRemote>.asLocalModel(): List<PVProject> {
    val resultList = mutableListOf<PVProject>()
    this.forEach { pvProjectRemote ->
        resultList.add(pvProjectRemote.asLocalModel())
    }
    return resultList
}

/*
fun List<PVProject>.asRemoteModel(downloadUrl: String, userUid: String): List<PVProjectRemote> {
    val resultList = mutableListOf<PVProjectRemote>()
    this.forEach { pvProject ->
        resultList.add(pvProject.asRemoteModel(downloadUrl, userUid))
    }
    return resultList
}
*/

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
@SuppressLint("WrongConstant")
fun View.showSnackbar(
    snackbarText: String,
    timeLength: Int,
    anchorView: View,
    isError: Boolean = false,
    goSettings: Boolean = false
) {
    val snackbar = Snackbar.make(this, snackbarText, timeLength)

    snackbar.run {
        if (timeLength == -2 /* LENGTH_INDEFINITE */) {
            setAction(R.string.ok) { this.dismiss() }
        }

        if (isError) {
            setBackgroundTint(MaterialColors.getColor(this@showSnackbar, R.attr.colorError))
            setTextColor(MaterialColors.getColor(this@showSnackbar, R.attr.colorOnError))
            setActionTextColor(MaterialColors.getColor(this@showSnackbar, R.attr.colorOnError))
        }

        if (goSettings) {
            setAction(R.string.settings) {
                context.startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }
            duration = 0 /* LENGTH_LONG */
        }

        setAnchorView(anchorView)
        show()
    }


}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<SnackbarMessage>>,
    timeLength: Int,
    anchorView: View
) {
    snackbarEvent.observe(lifecycleOwner, { event ->
        event.getContentIfNotHandled()?.let {
            when (it.type) {
                DEFAULT -> {
                    if (it.isError) {
                        showSnackbar(context.getString(it.message), timeLength, anchorView, true)
                    } else {
                        showSnackbar(context.getString(it.message), timeLength, anchorView)
                    }

                }
                GO_TO_SETTINGS -> {
                    if (it.isError) {
                        showSnackbar(
                            context.getString(it.message), timeLength, anchorView,
                            isError = true,
                            goSettings = true
                        )
                    } else {
                        showSnackbar(
                            context.getString(it.message),
                            timeLength,
                            anchorView,
                            isError = false,
                            goSettings = true
                        )
                    }
                }
            }


        }
    })
}

fun TextInputLayout.isEditTextEmpty() =
    editText?.text.toString().isEmpty()

fun TextInputLayout.enableError(message: Int) {
    isErrorEnabled = true
    error = context.getString(message)
}

fun BottomAppBar.onMenuItemSelected(viewModel: ViewModel?) {
    viewModel?.let {
        (it as? ArticlesViewModel)?.let { articlesViewModel ->
            this.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.bookmark -> {
                        articlesViewModel.setFiltering(ArticleFilterType.BOOKMARK)
                        true
                    }
                    R.id.history -> {
                        articlesViewModel.setFiltering(ArticleFilterType.HISTORY)
                        true
                    }
                    else -> {
                        false
                    }

                }
            }
        }
    }
}


fun RecyclerView.hideOrShowBottomAppBarOnRecyclerScrolled(bottomAppBar: BottomAppBar?) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

fun FloatingActionButton.setFunctionOnClick(viewModel: ViewModel?) {
    viewModel?.let { vm ->
        when (vm) {
            is ArticleDetailsViewModel -> {
                setOnClickListener { vm.setBookmark() }
            }
            is ProjectsViewModel -> {
                setOnClickListener { vm.onAddNewProject() }
            }
        }
    }
}

// animate changing the view visibility
fun View.fadeIn() {
    this.visibility = View.VISIBLE
    this.alpha = 0f
    this.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeIn.alpha = 1f
        }
    })
}

// animate changing the view visibility
fun View.fadeOut() {
    this.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeOut.alpha = 1f
            this@fadeOut.visibility = View.GONE
        }
    })
}