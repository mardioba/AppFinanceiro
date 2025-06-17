package com.example.appfinanceiro.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.appfinanceiro.data.dao.ExpenseDao
import com.example.appfinanceiro.data.model.Expense
import com.example.appfinanceiro.data.model.ExpenseReport
import java.util.Date

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()

    fun getExpenseById(id: Long): LiveData<Expense> {
        return expenseDao.getExpenseById(id)
    }

    fun getExpensesByDateRange(startDate: Date, endDate: Date): LiveData<List<Expense>> {
        return expenseDao.getExpensesByDateRange(startDate, endDate)
    }

    fun getExpensesByCategoryAndDateRange(category: String, startDate: Date, endDate: Date): LiveData<List<Expense>> {
        return expenseDao.getExpensesByCategoryAndDateRange(category, startDate, endDate)
    }

    fun getExpensesByPaymentStatusAndDateRange(isPaid: Boolean, startDate: Date, endDate: Date): LiveData<List<Expense>> {
        return expenseDao.getExpensesByPaymentStatusAndDateRange(isPaid, startDate, endDate)
    }

    fun getMonthlyReport(startDate: Date, endDate: Date): LiveData<ExpenseReport> {
        return getExpensesByDateRange(startDate, endDate).map { expenses ->
            val categoryTotals = expenses.groupBy { it.category }
                .mapValues { (_, expenses) -> expenses.sumOf { it.value } }

            ExpenseReport(
                month = startDate,
                totalAmount = expenses.sumOf { it.value },
                expenses = expenses,
                categoryTotals = categoryTotals,
                paidExpenses = expenses.count { it.isPaid },
                unpaidExpenses = expenses.count { !it.isPaid }
            )
        }
    }

    suspend fun insertExpense(expense: Expense): Long {
        return expenseDao.insertExpense(expense)
    }

    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }

    fun getExpensesByPaymentStatus(isPaid: Boolean): LiveData<List<Expense>> {
        return expenseDao.getExpensesByPaymentStatus(isPaid)
    }

    fun getExpensesByRecurringStatus(isRecurring: Boolean): LiveData<List<Expense>> {
        return expenseDao.getExpensesByRecurringStatus(isRecurring)
    }
} 