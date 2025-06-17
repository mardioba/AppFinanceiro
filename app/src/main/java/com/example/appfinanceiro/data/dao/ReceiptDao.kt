package com.example.appfinanceiro.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.appfinanceiro.data.model.Receipt

@Dao
interface ReceiptDao {
    @Query("SELECT * FROM receipts WHERE expenseId = :expenseId ORDER BY date DESC")
    fun getReceiptsForExpense(expenseId: Long): LiveData<List<Receipt>>

    @Query("SELECT * FROM receipts WHERE expenseId = :expenseId")
    fun getReceiptsByExpenseId(expenseId: Long): LiveData<List<Receipt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipt(receipt: Receipt): Long

    @Update
    suspend fun updateReceipt(receipt: Receipt)

    @Delete
    suspend fun deleteReceipt(receipt: Receipt)

    @Query("DELETE FROM receipts WHERE expenseId = :expenseId")
    suspend fun deleteReceiptsForExpense(expenseId: Long)

    @Query("SELECT * FROM receipts WHERE id = :id")
    suspend fun getReceiptById(id: Long): Receipt?
} 