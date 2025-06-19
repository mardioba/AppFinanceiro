package com.example.appfinanceiro

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.appfinanceiro.databinding.ActivityMainBinding
import com.example.appfinanceiro.ui.expense.ExpenseListFragmentDirections

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        // FAB só visível na tela de lista de despesas
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.fabAddExpense.visibility =
                if (destination.id == R.id.expenseListFragment) View.VISIBLE else View.GONE
        }

        binding.fabAddExpense.setOnClickListener {
            val action = ExpenseListFragmentDirections
                .actionExpenseListFragmentToExpenseFormFragment(-1L)
            navController.navigate(action)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
} 