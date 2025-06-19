package com.example.appfinanceiro.ui.expense

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.appfinanceiro.data.model.Expense
import com.example.appfinanceiro.databinding.ItemExpenseBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class ExpenseAdapter(
    private val onItemClick: (Expense) -> Unit,
    private val onPaidChanged: (Expense, Boolean) -> Unit,
    private val navController: NavController
) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExpenseViewHolder(
        private val binding: ItemExpenseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(expense: Expense) {
            binding.apply {
                switchPaid.setOnCheckedChangeListener(null)
                switchPaid.isChecked = expense.isPaid
                switchPaid.setOnCheckedChangeListener { _, isChecked ->
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onPaidChanged(getItem(position), isChecked)
                        if (isChecked) {
                            val expense = getItem(position)
                            val action = ExpenseListFragmentDirections.actionExpenseListFragmentToReceiptFormFragment(expense.id, -1L)
                            navController.navigate(action)
                        }
                    }
                }
                textExpenseName.text = expense.name
                textExpenseCategory.text = expense.category
                textExpenseValue.text = currencyFormat.format(expense.value)
                textExpenseDate.text = dateFormat.format(expense.date)
            }
        }
    }

    private class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
} 