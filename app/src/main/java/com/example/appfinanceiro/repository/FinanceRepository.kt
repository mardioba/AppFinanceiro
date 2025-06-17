package com.example.appfinanceiro.repository

import androidx.lifecycle.LiveData
import com.example.appfinanceiro.data.dao.ExpenseDao
import com.example.appfinanceiro.data.dao.ReceiptDao
import com.example.appfinanceiro.data.model.Expense
import com.example.appfinanceiro.data.model.Receipt
import java.util.Date

class FinanceRepository(
    private val expenseDao: ExpenseDao,
    private val receiptDao: ReceiptDao
) {
    // Operações com Despesas
    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()

    fun getExpensesByDateRange(startDate: Date, endDate: Date): LiveData<List<Expense>> {
        return expenseDao.getExpensesByDateRange(startDate, endDate)
    }

    fun getExpensesByPaymentStatus(isPaid: Boolean): LiveData<List<Expense>> {
        return expenseDao.getExpensesByPaymentStatus(isPaid)
    }

    fun getExpensesByRecurringStatus(isRecurring: Boolean): LiveData<List<Expense>> {
        return expenseDao.getExpensesByRecurringStatus(isRecurring)
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

    fun getExpenseById(id: Long): LiveData<Expense> {
        return expenseDao.getExpenseById(id)
    }

    // Operações com Comprovantes
    fun getReceiptsForExpense(expenseId: Long): LiveData<List<Receipt>> {
        return receiptDao.getReceiptsForExpense(expenseId)
    }

    suspend fun insertReceipt(receipt: Receipt): Long {
        return receiptDao.insertReceipt(receipt)
    }

    suspend fun deleteReceipt(receipt: Receipt) {
        receiptDao.deleteReceipt(receipt)
    }

    suspend fun getReceiptById(id: Long): Receipt? {
        return receiptDao.getReceiptById(id)
    }
} 