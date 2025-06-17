package com.example.appfinanceiro.ui.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfinanceiro.R
import com.example.appfinanceiro.data.model.Expense
import com.example.appfinanceiro.databinding.FragmentExpenseDetailBinding
import com.example.appfinanceiro.ui.receipt.ReceiptAdapter
import com.example.appfinanceiro.ui.viewmodel.FinanceViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class ExpenseDetailFragment : Fragment() {

    private var _binding: FragmentExpenseDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FinanceViewModel by viewModels()
    private val args: ExpenseDetailFragmentArgs by navArgs()
    private val receiptAdapter = ReceiptAdapter()

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupToolbar()
    }

    private fun setupRecyclerView() {
        binding.rvReceipts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = receiptAdapter
        }
    }

    private fun setupObservers() {
        viewModel.getExpenseById(args.expenseId).observe(viewLifecycleOwner) { expense ->
            expense?.let {
                updateUI(it)
            }
        }

        viewModel.getReceiptsByExpenseId(args.expenseId).observe(viewLifecycleOwner) { receipts ->
            receiptAdapter.submitList(receipts)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_expense_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                val action = ExpenseDetailFragmentDirections
                    .actionExpenseDetailFragmentToExpenseFormFragment(args.expenseId)
                findNavController().navigate(action)
                true
            }
            R.id.action_delete -> {
                viewModel.getExpenseById(args.expenseId).observe(viewLifecycleOwner) { expense ->
                    expense?.let {
                        viewModel.deleteExpense(it)
                        findNavController().navigateUp()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(expense: Expense) {
        binding.toolbar.title = getString(R.string.expense_details)
        binding.tvDescription.text = expense.name
        binding.tvCategory.text = expense.category
        binding.tvValue.text = currencyFormat.format(expense.value)
        binding.tvDate.text = dateFormat.format(expense.date)
        binding.tvPaymentStatus.text = getString(
            if (expense.isPaid) R.string.expense_paid else R.string.expense_unpaid
        )
        binding.tvRecurringStatus.text = getString(
            if (expense.isRecurring) R.string.expense_recurring else R.string.expense_one_time
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 