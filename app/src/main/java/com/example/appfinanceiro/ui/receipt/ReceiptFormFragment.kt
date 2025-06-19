package com.example.appfinanceiro.ui.receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.appfinanceiro.databinding.FragmentReceiptFormBinding
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.example.appfinanceiro.data.model.Receipt
import com.example.appfinanceiro.ui.viewmodel.FinanceViewModel
import androidx.fragment.app.viewModels
import java.util.Date

class ReceiptFormFragment : Fragment() {
    private var _binding: FragmentReceiptFormBinding? = null
    private val binding get() = _binding!!
    private val args: ReceiptFormFragmentArgs by navArgs()
    private var selectedFileUri: Uri? = null
    private val viewModel: FinanceViewModel by viewModels()

    private val selectFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedFileUri = uri
                val fileName = getFileName(uri)
                binding.textSelectedFile.text = fileName ?: "Arquivo selecionado"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceiptFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSelectFile.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 100)
                    return@setOnClickListener
                }
            }
            openFilePicker()
        }
        binding.buttonSaveReceipt.setOnClickListener {
            if (selectedFileUri == null) {
                Toast.makeText(requireContext(), "Selecione um comprovante!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val receipt = Receipt(
                expenseId = args.expenseId,
                filePath = selectedFileUri.toString(),
                date = Date()
            )
            viewModel.insertReceipt(receipt)
            Toast.makeText(requireContext(), "Comprovante cadastrado para despesa ${args.expenseId}", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        selectFileLauncher.launch(intent)
    }

    private fun getFileName(uri: Uri): String? {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker()
        } else if (requestCode == 100) {
            Toast.makeText(requireContext(), "Permissão negada para acessar arquivos.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 