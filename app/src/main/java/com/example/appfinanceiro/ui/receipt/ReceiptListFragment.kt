package com.example.appfinanceiro.ui.receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.appfinanceiro.databinding.FragmentReceiptListBinding

class ReceiptListFragment : Fragment() {
    private var _binding: FragmentReceiptListBinding? = null
    private val binding get() = _binding!!
    private val args: ReceiptListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceiptListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAddReceipt.setOnClickListener {
            val action = ReceiptListFragmentDirections.actionReceiptListFragmentToReceiptFormFragment(args.expenseId, -1L)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 