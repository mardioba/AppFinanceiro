package com.example.appfinanceiro.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val value: Double,
    val date: Date,
    val category: String,
    val isPaid: Boolean = false,
    val isRecurring: Boolean = false,
    val notes: String? = null,
    val dueDate: Date? = null
) 