package com.listocalixto.android.mathsolar.bindings

import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.data.model.PVProject
import com.listocalixto.android.mathsolar.ui.main.projects.adapter.ProjectsAdapter

/**
 * [BindingAdapter]s for the [PVProject]s list.
 */
@BindingAdapter("app:projectItems")
fun RecyclerView.setItems(items: List<PVProject>?) {
    items?.let {
        (adapter as ProjectsAdapter).submitList(items)
    }
}

@BindingAdapter("app:applyFavoriteColor")
fun ImageButton.isFavorite(item: PVProject?) {
    item?.let {
        if (item.isFavorite) {
            setColorFilter(ContextCompat.getColor(context, R.color.red))
        }
    }
}

@BindingAdapter("app:loadImage")
fun ImageView.setProjectImage(url: String) {
    load(url) {
        crossfade(600)
        error(R.drawable.ic_error_placeholder)
    }
}