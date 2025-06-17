package com.example.appfinanceiro.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "receipts",
    foreignKeys = [
        ForeignKey(
            entity = Expense::class,
            parentColumns = ["id"],
            childColumns = ["expenseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("expenseId")]
)
data class Receipt(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val expenseId: Long,
    val filePath: String,
    val date: Date = Date()
) 