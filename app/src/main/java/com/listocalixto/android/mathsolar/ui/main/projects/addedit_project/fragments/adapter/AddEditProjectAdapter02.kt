package com.listocalixto.android.mathsolar.ui.main.projects.addedit_project.fragments.adapter

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.ItemListElectricPaymentBinding
import com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project.AddEditProjectViewModel

class AddEditProjectAdapter02(private val viewModel: AddEditProjectViewModel) :
    ListAdapter<Double, AddEditProjectAdapter02.ViewHolder>(AddEditProject02DiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.run {
            if (position != 0) {
                var isInUpdateMode = false
                inputLayoutPaymentItem.hint = "Payment $position"
                inputPaymentItem.isEnabled = false
                inputLayoutPaymentItem.alpha = 0.4f
                icActionButtonItem.setImageResource(R.drawable.ic_edit)
                icActionButtonItem.setOnClickListener {
                    if (isInUpdateMode) {
                        val paymentUpdated = inputPaymentItem.text.toString()
                        if (isValidateInput(paymentUpdated)) {
                            viewModel.onUpdatePayment(position, paymentUpdated)
                            inputPaymentItem.isEnabled = false
                            icActionButtonItem.setImageResource(R.drawable.ic_edit)
                            inputLayoutPaymentItem.alpha = 0.4f
                            isInUpdateMode = false
                        } else {
                            viewModel.showSnackbarErrorMessage()
                        }
                        return@setOnClickListener
                    }

                    inputPaymentItem.isEnabled = true
                    inputPaymentItem.isFocusableInTouchMode = true
                    inputPaymentItem.requestFocus()
                    icActionButtonItem.setImageResource(R.drawable.ic_update)
                    isInUpdateMode = true
                    inputLayoutPaymentItem.alpha = 1.0f

                }

            } else {
                inputPaymentItem.setText("")
                icActionButtonItem.setImageResource(R.drawable.ic_add)
                icActionButtonItem.setOnClickListener {
                    val newPayment = inputPaymentItem.text.toString()
                    if (isValidateInput(newPayment)) {
                        viewModel.onAddPayment(inputPaymentItem.text.toString())
                        inputPaymentItem.setText("")
                    } else {
                        viewModel.showSnackbarErrorMessage()
                    }
                }
            }
        }

        holder.bind(viewModel, item)

    }

    private fun isValidateInput(newPayment: String): Boolean {
        return !(newPayment.isEmpty() || newPayment.toDouble() == 0.0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemListElectricPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: AddEditProjectViewModel, item: Double) {
            binding.run {
                addEditProjectViewModel = viewModel
                payment = item
                executePendingBindings()
            }

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