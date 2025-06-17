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
import java.util.Calendar
import java.util.Locale

class ExpenseListFragment : Fragment() {

    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FinanceViewModel by viewModels()
    private lateinit var expenseAdapter: ExpenseAdapter

    private var currentMonth = Calendar.getInstance()
    private val monthFormat = SimpleDateFormat("MMMM yyyy", Locale("pt", "BR"))

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
        loadExpenses()
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter(
            onItemClick = { expense ->
                val action = ExpenseListFragmentDirections
                    .actionExpenseListFragmentToExpenseDetailFragment(expense.id)
                findNavController().navigate(action)
            },
            onPaidChanged = { expense, isPaid ->
                viewModel.updateExpense(expense.copy(isPaid = isPaid))
            }
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
            loadExpenses()
        }

        binding.buttonNextMonth.setOnClickListener {
            currentMonth.add(Calendar.MONTH, 1)
            updateMonthDisplay()
            loadExpenses()
        }

        binding.switchShowUnpaid.setOnCheckedChangeListener { _, _ ->
            loadExpenses()
        }
    }

    private fun setupObservers() {
        viewModel.getExpensesByDateRange(
            getStartOfMonth(currentMonth).time,
            getEndOfMonth(currentMonth).time
        ).observe(viewLifecycleOwner) { expenses ->
            val filteredExpenses = if (binding.switchShowUnpaid.isChecked) {
                expenses.filter { !it.isPaid }
            } else {
                expenses
            }
            expenseAdapter.submitList(filteredExpenses)
            binding.textEmpty.visibility = if (filteredExpenses.isEmpty()) View.VISIBLE else View.GONE
        }
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
            add(Calendar.MONTH, 1)
            add(Calendar.DAY_OF_MONTH, -1)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return newCalendar
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