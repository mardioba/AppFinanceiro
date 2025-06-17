package com.example.appfinanceiro.data.repository

import androidx.lifecycle.LiveData
import com.example.appfinanceiro.data.dao.ReceiptDao
import com.example.appfinanceiro.data.model.Receipt

class ReceiptRepository(private val receiptDao: ReceiptDao) {
    fun getReceiptsByExpenseId(expenseId: Long): LiveData<List<Receipt>> {
        return receiptDao.getReceiptsByExpenseId(expenseId)
    }

    suspend fun insertReceipt(receipt: Receipt): Long {
        return receiptDao.insertReceipt(receipt)
    }

    suspend fun updateReceipt(receipt: Receipt) {
        receiptDao.updateReceipt(receipt)
    }

    suspend fun deleteReceipt(receipt: Receipt) {
        receiptDao.deleteReceipt(receipt)
    }
} 