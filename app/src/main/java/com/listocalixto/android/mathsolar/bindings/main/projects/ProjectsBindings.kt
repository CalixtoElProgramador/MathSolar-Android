package com.listocalixto.android.mathsolar.bindings.main.projects

import android.widget.ImageButton
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.color.MaterialColors
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
            setColorFilter(MaterialColors.getColor(this, R.attr.colorError))
        }
    }
}

@BindingAdapter(
    "loadProjectImageItem"
)
fun ImageView.setProjectImage(imageUrl: String?) {
    imageUrl?.let {
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