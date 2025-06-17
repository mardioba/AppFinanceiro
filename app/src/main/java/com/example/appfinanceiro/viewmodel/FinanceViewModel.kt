package com.example.appfinanceiro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.appfinanceiro.data.AppDatabase
import com.example.appfinanceiro.data.model.Expense
import com.example.appfinanceiro.data.model.ExpenseReport
import com.example.appfinanceiro.data.model.Receipt
import com.example.appfinanceiro.data.repository.ExpenseRepository
import com.example.appfinanceiro.data.repository.ReceiptRepository
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    private val expenseRepository: ExpenseRepository
    private val receiptRepository: ReceiptRepository

    init {
        val database = AppDatabase.getInstance(application)
        expenseRepository = ExpenseRepository(database.expenseDao())
        receiptRepository = ReceiptRepository(database.receiptDao())
    }

    fun insertExpense(expense: Expense) {
        viewModelScope.launch {
            expenseRepository.insertExpense(expense)
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            expenseRepository.updateExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expenseRepository.deleteExpense(expense)
        }
    }

    fun getAllExpenses(): LiveData<List<Expense>> {
        return expenseRepository.allExpenses
    }

    fun getExpensesByDateRange(startDate: Date, endDate: Date): LiveData<List<Expense>> {
        return expenseRepository.getExpensesByDateRange(startDate, endDate)
    }

    fun getMonthlyReport(startDate: Date, endDate: Date): LiveData<ExpenseReport> {
        return expenseRepository.getMonthlyReport(startDate, endDate)
    }

    fun insertReceipt(receipt: Receipt) {
        viewModelScope.launch {
            receiptRepository.insertReceipt(receipt)
        }
    }

    fun updateReceipt(receipt: Receipt) {
        viewModelScope.launch {
            receiptRepository.updateReceipt(receipt)
        }
    }

    fun deleteReceipt(receipt: Receipt) {
        viewModelScope.launch {
            receiptRepository.deleteReceipt(receipt)
        }
    }

    fun getReceiptsByExpenseId(expenseId: Long): LiveData<List<Receipt>> {
        return receiptRepository.getReceiptsByExpenseId(expenseId)
    }

    fun getExpensesForCurrentMonth(): LiveData<List<Expense>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startDate = calendar.time

        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endDate = calendar.time

        return expenseRepository.getExpensesByDateRange(startDate, endDate)
    }

    fun getExpensesByCategory(category: String, startDate: Date, endDate: Date): LiveData<List<Expense>> {
        return expenseRepository.getExpensesByCategoryAndDateRange(category, startDate, endDate)
    }

    fun getExpensesByPaymentStatus(isPaid: Boolean, startDate: Date, endDate: Date): LiveData<List<Expense>> {
        return expenseRepository.getExpensesByPaymentStatusAndDateRange(isPaid, startDate, endDate)
    }

    fun getUnpaidExpenses(): LiveData<List<Expense>> {
        return expenseRepository.getExpensesByPaymentStatus(false)
    }

    fun getRecurringExpenses(): LiveData<List<Expense>> {
        return expenseRepository.getExpensesByRecurringStatus(true)
    }

    fun getReceiptsForExpense(expenseId: Long): LiveData<List<Receipt>> {
        return receiptRepository.getReceiptsByExpenseId(expenseId)
    }
} 