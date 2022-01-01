package com.listocalixto.android.mathsolar.ui.main.projects.addedit_project.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.listocalixto.android.mathsolar.data.model.Payment
import com.listocalixto.android.mathsolar.databinding.ItemListElectricPaymentBinding
import com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project.AddEditProjectViewModel

class AddEditProjectAdapter02(private val viewModel: AddEditProjectViewModel) :
    ListAdapter<Double, AddEditProjectAdapter02.ViewHolder>(AddEditProject02DiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ItemListElectricPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: AddEditProjectViewModel, item: Double) {
            binding.addEditProjectViewModel = viewModel
            binding.payment = item

            binding.icAddPaymentItem.setOnClickListener {
                viewModel.onAddPayment(binding.inputPaymentItem.text.toString())
                binding.inputPaymentItem.setText("")
            }

            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListElectricPaymentBinding.inflate(layoutInflater, parent, false)

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
class AddEditProject02DiffCallback : DiffUtil.ItemCallback<Double>() {
    override fun areItemsTheSame(oldItem: Double, newItem: Double): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Double, newItem: Double): Boolean {
        return false
    }
}