package com.example.appfinanceiro.data.model

import java.util.Date

data class ExpenseReport(
    val month: Date,
    val totalAmount: Double,
    val expenses: List<Expense>,
    val categoryTotals: Map<String, Double>,
    val paidExpenses: Int,
    val unpaidExpenses: Int
) 