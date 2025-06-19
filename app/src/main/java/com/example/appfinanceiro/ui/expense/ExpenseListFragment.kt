package com.example.appfinanceiro.ui.expense

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfinanceiro.R
import com.example.appfinanceiro.databinding.FragmentExpenseListBinding
import com.example.appfinanceiro.ui.viewmodel.FinanceViewModel
import java.text.SimpleDateFormat
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.LiveData

class ExpenseListFragment : Fragment() {

    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FinanceViewModel by viewModels()
    private lateinit var expenseAdapter: ExpenseAdapter

    private var currentMonth = Calendar.getInstance()
    private val monthFormat = SimpleDateFormat("MMMM yyyy", Locale("pt", "BR"))

    private var expensesSource: LiveData<List<com.example.appfinanceiro.data.model.Expense>>? = null
    private val mediatorExpenses = MediatorLiveData<List<com.example.appfinanceiro.data.model.Expense>>()

    private var nextMonthObserver: androidx.lifecycle.Observer<List<com.example.appfinanceiro.data.model.Expense>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupMonthNavigation()
        setupObservers()
        updateMonthDisplay()
        observeExpensesForCurrentMonth()
    }

    private fun setupRecyclerView() {
        val navController = findNavController()
        expenseAdapter = ExpenseAdapter(
            onItemClick = { expense ->
                val action = ExpenseListFragmentDirections
                    .actionExpenseListFragmentToExpenseDetailFragment(expense.id)
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

        binding.recyclerExpenses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = expenseAdapter
        }
    }

    private fun setupMonthNavigation() {
        binding.buttonPreviousMonth.setOnClickListener {
            currentMonth.add(Calendar.MONTH, -1)
            updateMonthDisplay()
            observeExpensesForCurrentMonth()
        }

        binding.buttonNextMonth.setOnClickListener {
            currentMonth.add(Calendar.MONTH, 1)
            updateMonthDisplay()
            observeExpensesForCurrentMonth()
        }

        binding.switchShowUnpaid.setOnCheckedChangeListener { _, _ ->
            updateExpensesUI(mediatorExpenses.value ?: emptyList())
        }
    }

    private fun setupObservers() {
        mediatorExpenses.observe(viewLifecycleOwner) { expenses ->
            updateExpensesUI(expenses)
        }
    }

    private fun observeExpensesForCurrentMonth() {
        val start = getStartOfMonth(currentMonth).time
        val end = getEndOfMonth(currentMonth).time
        val newSource = viewModel.getExpensesByDateRange(start, end)
        expensesSource?.let { mediatorExpenses.removeSource(it) }
        expensesSource = newSource
        mediatorExpenses.addSource(newSource) { mediatorExpenses.value = it }
    }

    private fun updateExpensesUI(expenses: List<com.example.appfinanceiro.data.model.Expense>) {
        val filteredExpenses = if (binding.switchShowUnpaid.isChecked) {
            expenses.filter { !it.isPaid }
        } else {
            expenses
        }
        expenseAdapter.submitList(filteredExpenses)
        binding.textEmpty.visibility = if (filteredExpenses.isEmpty()) View.VISIBLE else View.GONE

        // Atualiza o valor total do mês
        val total = expenses.sumOf { it.value }
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        binding.textTotalMonthValue.text = currencyFormat.format(total)
    }

    private fun updateMonthDisplay() {
        binding.textCurrentMonth.text = monthFormat.format(currentMonth.time)
    }

    private fun loadExpenses() {
        viewModel.getExpensesByDateRange(
            getStartOfMonth(currentMonth).time,
            getEndOfMonth(currentMonth).time
        )
    }

    private fun getStartOfMonth(calendar: Calendar): Calendar {
        val newCalendar = Calendar.getInstance()
        newCalendar.time = calendar.time
        newCalendar.apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return newCalendar
    }

    private fun getEndOfMonth(calendar: Calendar): Calendar {
        val newCalendar = Calendar.getInstance()
        newCalendar.time = calendar.time
        newCalendar.apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return newCalendar
    }

    private fun createNextRecurringExpense(expense: com.example.appfinanceiro.data.model.Expense) {
        val calendar = Calendar.getInstance().apply { time = expense.date }
        calendar.add(Calendar.MONTH, 1)
        val nextMonthDate = calendar.time
        
        // Remove observer anterior se existir
        nextMonthObserver?.let { observer ->
            viewModel.getExpensesByDateRange(nextMonthDate, nextMonthDate).removeObserver(observer)
        }
        
        // Cria novo observer
        nextMonthObserver = androidx.lifecycle.Observer { expenses ->
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
        
        viewModel.getExpensesByDateRange(nextMonthDate, nextMonthDate).observe(viewLifecycleOwner, nextMonthObserver!!)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_expense_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_expense -> {
                val action = ExpenseListFragmentDirections
                    .actionExpenseListFragmentToExpenseFormFragment()
                findNavController().navigate(action)
                true
            }
            R.id.action_expense_report -> {
                val action = ExpenseListFragmentDirections
                    .actionExpenseListFragmentToExpenseReportFragment()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 