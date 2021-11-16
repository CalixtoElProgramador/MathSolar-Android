package com.listocalixto.android.mathsolar.ui.main.projects.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.listocalixto.android.mathsolar.data.model.PVProject
import com.listocalixto.android.mathsolar.databinding.ItemListProjectBinding
import com.listocalixto.android.mathsolar.presentation.projects.ProjectsViewModel

class ProjectsAdapter(private val viewModel: ProjectsViewModel) :
    ListAdapter<PVProject, ProjectsAdapter.ViewHolder>(TaskDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ItemListProjectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ProjectsViewModel, item: PVProject) {

            binding.viewmodel = viewModel
            binding.project = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListProjectBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class TaskDiffCallback : DiffUtil.ItemCallback<PVProject>() {
    override fun areItemsTheSame(oldItem: PVProject, newItem: PVProject): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: PVProject, newItem: PVProject): Boolean {
        return oldItem == newItem
    }
}