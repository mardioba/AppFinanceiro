package com.example.appfinanceiro.ui.viewmodel

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
import java.util.Date

class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    private val expenseRepository: ExpenseRepository
    private val receiptRepository: ReceiptRepository

    init {
        val database = AppDatabase.getInstance(application)
        expenseRepository = ExpenseRepository(database.expenseDao())
        receiptRepository = ReceiptRepository(database.receiptDao())
    }

    fun getExpenseById(id: Long): LiveData<Expense> {
        return expenseRepository.getExpenseById(id)
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
} 