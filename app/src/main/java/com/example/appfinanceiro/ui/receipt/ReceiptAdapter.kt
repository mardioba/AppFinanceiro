package com.example.appfinanceiro.ui.receipt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appfinanceiro.data.model.Receipt
import com.example.appfinanceiro.databinding.ItemReceiptBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ReceiptAdapter(
    private val onItemClick: (Receipt) -> Unit
) : ListAdapter<Receipt, ReceiptAdapter.ReceiptViewHolder>(ReceiptDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val binding = ItemReceiptBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReceiptViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ReceiptViewHolder(
        private val binding: ItemReceiptBinding,
        private val onItemClick: (Receipt) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

        fun bind(receipt: Receipt) {
            binding.apply {
                tvReceiptDate.text = dateFormat.format(receipt.date)
                tvReceiptPath.text = receipt.filePath
                root.setOnClickListener { onItemClick(receipt) }
            }
        }
    }

    private class ReceiptDiffCallback : DiffUtil.ItemCallback<Receipt>() {
        override fun areItemsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
            return oldItem == newItem
        }
    }
} 