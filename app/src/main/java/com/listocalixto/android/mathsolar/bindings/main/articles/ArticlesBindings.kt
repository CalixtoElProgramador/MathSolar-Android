package com.listocalixto.android.mathsolar.bindings.main.articles

import android.icu.text.SimpleDateFormat
import android.util.Log
import java.util.Calendar
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.color.MaterialColors
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.Constants.MAXIMUM_TOP_APP_BAR_VERTICAL_OFF_SET
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.presentation.main.articles.ArticlesViewModel
import com.listocalixto.android.mathsolar.ui.main.articles.adapter.HomeAdapter
import com.listocalixto.android.mathsolar.utils.ArticleFilterType
import com.listocalixto.android.mathsolar.utils.ArticleTopic
import com.listocalixto.android.mathsolar.utils.fadeIn
import com.todkars.shimmer.ShimmerRecyclerView
import java.util.*

@BindingAdapter("app:articleItems")
fun ShimmerRecyclerView.setArticleItems(items: List<Article>?) {
    items?.let {
        (actualAdapter as HomeAdapter).submitList(it)
    }
}

@BindingAdapter(
    "loadArticleImage"
)
fun ImageView.setArticleImageDetail(articleUrl: String?) {
    articleUrl?.let {
        Glide.with(context)
            .load(it)
            .placeholder(R.drawable.ic_error_placeholder)
            .error(R.drawable.ic_error_placeholder)
            .into(this)
    } ?: run {
        Glide.with(context).clear(this)
        this.setImageResource(R.drawable.ic_error_placeholder)
    }


}

@BindingAdapter(
    "applyBookmark"
)
fun ImageButton.isBookmark(item: Article?) {
    item?.let {
        if (it.bookmark) {
            setImageResource(R.drawable.ic_bookmark)
            setColorFilter(MaterialColors.getColor(this, R.attr.colorSecondaryVariant))
        } else {
            setImageResource(R.drawable.ic_bookmark_border)
            setColorFilter(MaterialColors.getColor(this, R.attr.colorOnSurface))
        }
    }
}

@BindingAdapter("app:loadingItems")
fun ShimmerRecyclerView.onLoadingStatus(state: Boolean?) {
    state?.let {
        if (it) {
            this.showShimmer()
        } else {
            this.hideShimmer()
        }
    }
}

@BindingAdapter("app:viewed")
fun View.isViewed(boolean: Boolean) {
    if (boolean) {
        when (this) {
            is TextView -> {
                setTextColor(MaterialColors.getColor(this, R.attr.colorOnSurfaceDisabled))
            }
            is MaterialCardView -> {
                setCardBackgroundColor(MaterialColors.getColor(this, R.attr.background))
                radius = 24.0f
                strokeWidth = 2
                strokeColor = MaterialColors.getColor(this, R.attr.colorSurface)
            }
        }
    } else {
        when {
            id == R.id.articleSourceItem && this is TextView -> {
                setTextColor(MaterialColors.getColor(this, R.attr.colorPrimary))
            }
            id == R.id.articleTitleItem && this is TextView -> {
                setTextColor(MaterialColors.getColor(this, R.attr.textColorPrimary))
            }
            id == R.id.articlePublishDateItem && this is TextView -> {
                setTextColor(MaterialColors.getColor(this, R.attr.textColorSecondary))
            }
            id == R.id.cardView_articleItem && this is MaterialCardView -> {
                setCardBackgroundColor(MaterialColors.getColor(this, R.attr.colorSurface))
                strokeWidth = 0
                radius = 24.0f
            }
        }
    }
}

@BindingAdapter("app:clearCheckInBookmarkOrHistory")
fun ChipGroup.clearCheckInBookmarkOrHistory(isBookmarkOrHistory: Boolean) {
    if (isBookmarkOrHistory) {
        isSelectionRequired = false
        clearCheck()
    } else {
        isSelectionRequired = true
    }
}

@BindingAdapter("app:publishDate", "app:isViewed")
fun LinearLayout.showOrHideNewLabel(date: String, isViewed: Boolean) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val today = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    val dateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    val publishDate = sdf.parse(date)
    dateCalendar.time = publishDate
    dateCalendar.set(
        dateCalendar.get(Calendar.YEAR),
        dateCalendar.get(Calendar.MONTH),
        dateCalendar.get(Calendar.DAY_OF_MONTH) + 1
    )

    today.set(
        today.get(Calendar.YEAR),
        today.get(Calendar.MONTH),
        today.get(Calendar.DAY_OF_MONTH),
        0,
        0,
        0
    )

    val TAG = "ArticlesBinding"
    Log.d(
        TAG,
        "today.timeInMilis = ${today.timeInMillis}, dateCalendar.timeInMillis = ${dateCalendar.timeInMillis}"
    )

    if (today.timeInMillis <= dateCalendar.timeInMillis && !isViewed) {
        fadeIn()
    } else {
        visibility = View.GONE
    }
}

@BindingAdapter("app:onSelected")
fun Chip.onSelected(viewModel: ArticlesViewModel) {
    setOnClickListener {
        viewModel.setFiltering(ArticleFilterType.ALL_ARTICLES)
        viewModel.onChipChecked()
        when (id) {
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
            R.id.chip_sustainability -> {
                viewModel.changeTopic(ArticleTopic.SUSTAINABILITY)
            }
        }
    }
}

@BindingAdapter(
    "offSetChangedListener"
)
fun AppBarLayout.offSetChangedListener(viewModel: ArticlesViewModel) {
    var toggle = true
    addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
        if (verticalOffset < MAXIMUM_TOP_APP_BAR_VERTICAL_OFF_SET) {
            if (!toggle) {
                viewModel.setExpandedAppBarState(toggle)
                toggle = true
            }
        } else {
            if (toggle) {
                viewModel.setExpandedAppBarState(toggle)
                toggle = false
            }
        }
    })
}
