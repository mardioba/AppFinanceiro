package com.example.appfinanceiro.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.appfinanceiro.data.model.Expense
import java.util.Date

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpenseById(id: Long): LiveData<Expense>

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesByDateRange(startDate: Date, endDate: Date): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate AND category = :category ORDER BY date DESC")
    fun getExpensesByCategoryAndDateRange(category: String, startDate: Date, endDate: Date): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate AND isPaid = :isPaid ORDER BY date DESC")
    fun getExpensesByPaymentStatusAndDateRange(isPaid: Boolean, startDate: Date, endDate: Date): LiveData<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE isPaid = 0 ORDER BY date ASC")
    fun getUnpaidExpenses(): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses WHERE isRecurring = 1 ORDER BY date ASC")
    fun getRecurringExpenses(): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses WHERE isPaid = :isPaid ORDER BY date DESC")
    fun getExpensesByPaymentStatus(isPaid: Boolean): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses WHERE isRecurring = :isRecurring ORDER BY date DESC")
    fun getExpensesByRecurringStatus(isRecurring: Boolean): LiveData<List<Expense>>
} 