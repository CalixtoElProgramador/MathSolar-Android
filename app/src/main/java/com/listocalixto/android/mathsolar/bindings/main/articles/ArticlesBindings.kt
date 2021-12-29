package com.listocalixto.android.mathsolar.bindings.main.articles

import android.icu.text.SimpleDateFormat
import android.view.View
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.load
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.color.MaterialColors
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.ui.main.articles.adapter.HomeAdapter
import com.todkars.shimmer.ShimmerRecyclerView
import java.util.*

@BindingAdapter("app:articleItems")
fun ShimmerRecyclerView.setArticleItems(items: List<Article>?) {
    items?.let {
        (actualAdapter as HomeAdapter).submitList(it)
    }
}

@BindingAdapter("app:loadArticleImage")
fun ImageView.setArticleImage(url: String?) {
    url?.let {
        load(it) {
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
    }
}

@BindingAdapter("app:applyBookmark")
fun ImageView.isBookmark(item: Article?) {
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
                setTextColor(MaterialColors.getColor(this, R.attr.colorPrimaryVariant))
            }
            id == R.id.articleTitleItem && this is TextView -> {
                setTextColor(MaterialColors.getColor(this, R.attr.textColorPrimary))
            }
            id == R.id.articlePublishDateItem && this is TextView -> {
                setTextColor(MaterialColors.getColor(this, R.attr.textColorSecondary))
            }
            id == R.id.card_view_article && this is MaterialCardView -> {
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
    val todayString = sdf.format(today.time)
    val publishDate = sdf.parse(date)
    val publishDateString = sdf.format(publishDate)

    visibility = if (todayString == publishDateString && !isViewed) {
        View.VISIBLE
    } else {
        View.GONE
    }

}
