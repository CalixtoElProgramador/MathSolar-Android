package com.listocalixto.android.mathsolar.bindings.main.articles

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.data.model.Article
import com.listocalixto.android.mathsolar.ui.main.articles.adapter.HomeAdapter
import com.todkars.shimmer.ShimmerRecyclerView

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
        }
        else {
            setImageResource(R.drawable.ic_bookmark_border)
            setColorFilter(MaterialColors.getColor(this, R.attr.colorOnSurface))
        }
    }
}

@BindingAdapter("app:loadingItems")
fun ShimmerRecyclerView.onLoadingStatus(state: Boolean?) {
    state?.let {
        if (it) { this.showShimmer() } else { this.hideShimmer() }
    }
}

@BindingAdapter("app:viewed")
fun TextView.isViewed(boolean: Boolean) {
    if (boolean) { setTextColor(MaterialColors.getColor(this, R.attr.colorOnSurfaceDisabled)) }
    else {
        when(this.id) {
            R.id.articleSourceItem -> {
                setTextColor(MaterialColors.getColor(this, R.attr.colorPrimary))
            }
            R.id.articleTitleItem -> {
                setTextColor(MaterialColors.getColor(this, R.attr.textColorPrimary))
            }
            R.id.articlePublishDateItem -> {
                setTextColor(MaterialColors.getColor(this, R.attr.textColorSecondary))
            }
        }
    }
}
