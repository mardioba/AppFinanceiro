package com.example.appfinanceiro.ui.expense

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.appfinanceiro.R
import com.example.appfinanceiro.data.model.Expense
import com.example.appfinanceiro.databinding.FragmentExpenseFormBinding
import com.example.appfinanceiro.ui.viewmodel.FinanceViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ExpenseFormFragment : Fragment() {

    private var _binding: FragmentExpenseFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FinanceViewModel by viewModels()
    private val args: ExpenseFormFragmentArgs by navArgs()

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    private var selectedDate = Calendar.getInstance().time

    private val categories = listOf(
        "Moradia",
        "Alimentação",
        "Transporte",
        "Lazer",
        "Saúde",
        "Educação",
        "Vestuário",
        "Outros"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategoryDropdown()
        setupDatePicker()
        setupClickListeners()
        loadExpenseIfEditing()
        binding.switchExpenseRecurring.setOnCheckedChangeListener { _, isChecked ->
            binding.layoutRecurringMonths.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }

    private fun setupCategoryDropdown() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categories
        )
        binding.dropdownExpenseCategory.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        binding.editExpenseDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setupClickListeners() {
        binding.buttonSave.setOnClickListener {
            saveExpense()
        }
    }

    private fun loadExpenseIfEditing() {
        if (args.expenseId != -1L) {
            viewModel.getExpenseById(args.expenseId).observe(viewLifecycleOwner) { expense ->
                expense?.let { displayExpense(it) }
            }
        } else {
            binding.editExpenseDate.setText(dateFormat.format(selectedDate))
        }
    }

    private fun displayExpense(expense: Expense) {
        binding.apply {
            editExpenseName.setText(expense.name)
            editExpenseValue.setText(currencyFormat.format(expense.value).replace("R$", "").trim())
            editExpenseDate.setText(dateFormat.format(expense.date))
            dropdownExpenseCategory.setText(expense.category, false)
            editExpenseNotes.setText(expense.notes)
            switchExpensePaid.isChecked = expense.isPaid
            switchExpenseRecurring.isChecked = expense.isRecurring
        }
        selectedDate = expense.date
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance().apply { time = selectedDate }
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(year, month, day)
                selectedDate = calendar.time
                binding.editExpenseDate.setText(dateFormat.format(selectedDate))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun saveExpense() {
        val name = binding.editExpenseName.text.toString()
        val valueStr = binding.editExpenseValue.text.toString()
        val category = binding.dropdownExpenseCategory.text.toString()
        val notes = binding.editExpenseNotes.text.toString()
        val isPaid = binding.switchExpensePaid.isChecked
        val isRecurring = binding.switchExpenseRecurring.isChecked
        val recurringMonthsStr = binding.editRecurringMonths.text.toString()
        val recurringMonths = recurringMonthsStr.toIntOrNull() ?: 1

        if (name.isBlank() || valueStr.isBlank() || category.isBlank()) {
            // TODO: Mostrar mensagem de erro
            return
        }

        val value = valueStr.replace(",", ".").toDoubleOrNull() ?: 0.0

        if (isRecurring && recurringMonths > 1 && args.expenseId == -1L) {
            val calendar = Calendar.getInstance().apply { time = selectedDate }
            for (i in 0 until recurringMonths) {
                val expense = Expense(
                    id = 0,
                    name = name,
                    value = value,
                    date = calendar.time,
                    category = category,
                    notes = notes.takeIf { it.isNotBlank() },
                    isPaid = isPaid,
                    isRecurring = true
                )
                viewModel.insertExpense(expense)
                calendar.add(Calendar.MONTH, 1)
            }
        } else {
            val expense = Expense(
                id = if (args.expenseId == -1L) 0 else args.expenseId,
                name = name,
                value = value,
                date = selectedDate,
                category = category,
                notes = notes.takeIf { it.isNotBlank() },
                isPaid = isPaid,
                isRecurring = isRecurring
            )
            if (args.expenseId == -1L) {
                viewModel.insertExpense(expense)
            } else {
                viewModel.updateExpense(expense)
            }
        }

        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 