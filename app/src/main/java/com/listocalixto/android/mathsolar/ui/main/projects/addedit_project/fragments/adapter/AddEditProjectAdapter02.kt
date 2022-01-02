package com.listocalixto.android.mathsolar.ui.main.projects.addedit_project.fragments.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.ItemListConsumptionBinding
import com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project.AddEditProjectViewModel

class AddEditProjectAdapter02(
    private val viewModel: AddEditProjectViewModel,
    private val context: Context,
    private val owner: LifecycleOwner
) : ListAdapter<Double, AddEditProjectAdapter02.ViewHolder>(AddEditProject02DiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        var isUpdateState = false
        holder.binding.run {
            inputPaymentItem.doAfterTextChanged { inputLayoutPaymentItem.isErrorEnabled = false }
            if (position != 0) {
                inputLayoutPaymentItem.isHelperTextEnabled = false
                inputLayoutPaymentItem.helperText = null
                configureTheViews(this, isUpdateState, 0.4f)
                inputLayoutPaymentItem.hint = context.getString(R.string.hint_text_input_consumption, position.toString())
                icActionButtonItem.setOnClickListener {
                    if (isUpdateState) {
                        val paymentUpdated = inputPaymentItem.text.toString()
                        val error = isAnErrorOrNull(paymentUpdated)
                        error?.let {
                            inputLayoutPaymentItem.error = context.getString(it)
                        } ?: run {
                            isUpdateState = false
                            viewModel.onUpdatePayment(position, paymentUpdated)
                            configureTheViews(this, isUpdateState, 0.4f)
                            inputLayoutPaymentItem.endIconMode = TextInputLayout.END_ICON_NONE
                            inputLayoutPaymentItem.endIconDrawable = null
                            notifyItemChanged(position)
                        }
                        return@setOnClickListener
                    }
                    isUpdateState = true
                    configureTheViews(this, isUpdateState, 1.0f)
                    inputLayoutPaymentItem.endIconMode = TextInputLayout.END_ICON_CUSTOM
                    inputLayoutPaymentItem.endIconDrawable =
                        ContextCompat.getDrawable(context, R.drawable.ic_delete)
                    inputLayoutPaymentItem.setEndIconOnClickListener {
                        viewModel.onDeleteConsumption(position)
                        viewModel.setCurrentFragment(R.id.addEditProjectFragment02)
                        val dataSize = this@AddEditProjectAdapter02.itemCount
                        notifyItemRangeChanged(position, dataSize)
                        notifyItemChanged(0)

                    }
                }
            } else {
                val dataSize = this@AddEditProjectAdapter02.itemCount
                viewModel.periodConsumptionType.observe(owner, {
                    it?.let {
                        inputLayoutPaymentItem.isHelperTextEnabled = true
                        val listSizeString = (dataSize - 1).toString()
                        val minMonthString = it.minMonths.toString()
                        inputLayoutPaymentItem.helperText = context.getString(
                            R.string.helper_text_consumption,
                            listSizeString,
                            minMonthString
                        )

                    }
                })
                icActionButtonItem.setImageResource(R.drawable.ic_add)
                inputLayoutPaymentItem.hint = context.getString(R.string.quantity)
                inputPaymentItem.isEnabled = true
                inputLayoutPaymentItem.alpha = 1.0f
                inputLayoutPaymentItem.endIconMode = TextInputLayout.END_ICON_NONE
                inputLayoutPaymentItem.endIconDrawable = null
                icActionButtonItem.setOnClickListener {
                    val newPayment = inputPaymentItem.text.toString()
                    val error = isAnErrorOrNull(newPayment)
                    error?.let {
                        inputLayoutPaymentItem.error = context.getString(it)
                    } ?: run {
                        viewModel.onAddPayment(inputPaymentItem.text.toString())
                        viewModel.setCurrentFragment(R.id.addEditProjectFragment02)
                        notifyItemRangeChanged(position, dataSize)
                    }
                }
            }
        }

        holder.bind(viewModel, item)
    }

    private fun configureTheViews(
        binding: ItemListConsumptionBinding,
        isUpdateState: Boolean,
        alpha: Float
    ) {
        binding.run {
            inputPaymentItem.isEnabled = isUpdateState
            inputLayoutPaymentItem.alpha = alpha
            if (isUpdateState) {
                icActionButtonItem.setImageResource(R.drawable.ic_update)
                inputPaymentItem.requestFocus()
            } else {
                icActionButtonItem.setImageResource(R.drawable.ic_edit)
                inputLayoutPaymentItem.endIconMode = TextInputLayout.END_ICON_NONE
                inputLayoutPaymentItem.endIconDrawable = null
            }

        }
    }

    private fun isAnErrorOrNull(newPayment: String): Int? {
        return when {
            newPayment.isEmpty() -> {
                R.string.err_field_empty
            }
            newPayment.toDoubleOrNull() == null -> {
                R.string.err_quantity_not_valid
            }
            newPayment.toDouble() <= 0.0 -> {
                R.string.err_payment_equals_or_minor_to_zero
            }
            else -> {
                null
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemListConsumptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: AddEditProjectViewModel, item: Double?) {
            binding.run {
                addEditProjectViewModel = viewModel
                consumption = item
                executePendingBindings()
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemListConsumptionBinding.inflate(layoutInflater, parent, false)

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