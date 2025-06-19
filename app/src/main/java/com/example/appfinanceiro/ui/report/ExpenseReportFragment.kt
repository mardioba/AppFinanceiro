package com.example.appfinanceiro.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfinanceiro.R
import com.example.appfinanceiro.data.model.ExpenseReport
import com.example.appfinanceiro.databinding.FragmentExpenseReportBinding
import com.example.appfinanceiro.ui.expense.ExpenseAdapter
import com.example.appfinanceiro.viewmodel.FinanceViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ExpenseReportFragment : Fragment() {
    private var _binding: FragmentExpenseReportBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FinanceViewModel by viewModels()
    private val categoryAdapter = CategoryReportAdapter()
    private val expenseAdapter by lazy {
        val navController = findNavController()
        ExpenseAdapter(
            onItemClick = { expense ->
                val action = ExpenseReportFragmentDirections
                    .actionExpenseReportFragmentToExpenseDetailFragment(expense.id)
                navController.navigate(action)
            },
            onPaidChanged = { expense, isPaid ->
                viewModel.updateExpense(expense.copy(isPaid = isPaid))
                if (isPaid && expense.isRecurring) {
                    createNextRecurringExpense(expense)
                }
            },
            navController = navController
        )
    }

    private var currentMonth: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupButtons()
        loadReport()
    }

    private fun setupRecyclerViews() {
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }

        binding.rvExpenses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = expenseAdapter
        }
    }

    private fun setupButtons() {
        binding.btnPreviousMonth.setOnClickListener {
            currentMonth.add(Calendar.MONTH, -1)
            loadReport()
        }

        binding.btnNextMonth.setOnClickListener {
            currentMonth.add(Calendar.MONTH, 1)
            loadReport()
        }
    }

    private fun loadReport() {
        val startDate = getStartOfMonth(currentMonth)
        val endDate = getEndOfMonth(currentMonth)

        viewModel.getMonthlyReport(startDate, endDate).observe(viewLifecycleOwner) { report ->
            updateUI(report)
        }
    }

    private fun updateUI(report: ExpenseReport) {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val monthFormat = SimpleDateFormat("MMMM yyyy", Locale("pt", "BR"))

        binding.toolbar.title = getString(R.string.expense_report_month, monthFormat.format(report.month))
        binding.tvTotalAmount.text = getString(R.string.total_amount, numberFormat.format(report.totalAmount))
        binding.tvPaidExpenses.text = getString(R.string.paid_expenses, report.paidExpenses)
        binding.tvUnpaidExpenses.text = getString(R.string.unpaid_expenses, report.unpaidExpenses)

        categoryAdapter.submitList(report.categoryTotals.toList().sortedByDescending { it.second })
        expenseAdapter.submitList(report.expenses)
    }

    private fun getStartOfMonth(calendar: Calendar): Date {
        val newCalendar = Calendar.getInstance()
        newCalendar.time = calendar.time
        newCalendar.set(Calendar.DAY_OF_MONTH, 1)
        newCalendar.set(Calendar.HOUR_OF_DAY, 0)
        newCalendar.set(Calendar.MINUTE, 0)
        newCalendar.set(Calendar.SECOND, 0)
        newCalendar.set(Calendar.MILLISECOND, 0)
        return newCalendar.time
    }

    private fun getEndOfMonth(calendar: Calendar): Date {
        val newCalendar = Calendar.getInstance()
        newCalendar.time = calendar.time
        newCalendar.add(Calendar.MONTH, 1)
        newCalendar.add(Calendar.DAY_OF_MONTH, -1)
        newCalendar.set(Calendar.HOUR_OF_DAY, 23)
        newCalendar.set(Calendar.MINUTE, 59)
        newCalendar.set(Calendar.SECOND, 59)
        newCalendar.set(Calendar.MILLISECOND, 999)
        return newCalendar.time
    }

    private fun createNextRecurringExpense(expense: com.example.appfinanceiro.data.model.Expense) {
        val calendar = Calendar.getInstance().apply { time = expense.date }
        calendar.add(Calendar.MONTH, 1)
        val nextMonthDate = calendar.time
        viewModel.getExpensesByDateRange(nextMonthDate, nextMonthDate).observe(viewLifecycleOwner) { expenses ->
            val exists = expenses.any {
                it.isRecurring &&
                it.name == expense.name &&
                it.category == expense.category &&
                it.value == expense.value &&
                it.date.month == nextMonthDate.month &&
                it.date.year == nextMonthDate.year
            }
            if (!exists) {
                val newExpense = expense.copy(
                    id = 0,
                    date = nextMonthDate,
                    isPaid = false
                )
                viewModel.insertExpense(newExpense)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 