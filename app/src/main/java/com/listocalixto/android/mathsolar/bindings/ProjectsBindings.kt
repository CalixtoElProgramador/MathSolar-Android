package com.listocalixto.android.mathsolar.bindings

import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.data.model.PVProject
import com.listocalixto.android.mathsolar.ui.main.projects.adapter.ProjectsAdapter

/**
 * [BindingAdapter]s for the [Task]s list.
 */
@BindingAdapter("app:items")
fun RecyclerView.setItems(items: List<PVProject>?) {
    items?.let {
        (adapter as ProjectsAdapter).submitList(items)
    }
}

@BindingAdapter("app:applyFavoriteColor")
fun ImageButton.isFavorite(item: PVProject?) {
    item?.let {
        if (it.isFavorite) {
            setColorFilter(ContextCompat.getColor(context, R.color.red))
        }
    }
}