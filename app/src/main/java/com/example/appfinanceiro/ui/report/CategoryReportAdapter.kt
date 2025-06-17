package com.example.appfinanceiro.ui.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appfinanceiro.R
import com.example.appfinanceiro.databinding.ItemCategoryReportBinding
import java.text.NumberFormat
import java.util.Locale

class CategoryReportAdapter : ListAdapter<Pair<String, Double>, CategoryReportAdapter.ViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryReportBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemCategoryReportBinding) : RecyclerView.ViewHolder(binding.root) {
        private val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        fun bind(item: Pair<String, Double>) {
            binding.tvCategoryName.text = item.first
            binding.tvCategoryAmount.text = numberFormat.format(item.second)
        }
    }

    private class CategoryDiffCallback : DiffUtil.ItemCallback<Pair<String, Double>>() {
        override fun areItemsTheSame(oldItem: Pair<String, Double>, newItem: Pair<String, Double>): Boolean {
            return oldItem.first == newItem.first
        }

        override fun areContentsTheSame(oldItem: Pair<String, Double>, newItem: Pair<String, Double>): Boolean {
            return oldItem == newItem
        }
    }
} 