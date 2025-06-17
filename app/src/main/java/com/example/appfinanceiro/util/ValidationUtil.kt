package com.example.appfinanceiro.util

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

object ValidationUtil {
    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        val pattern = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        )
        return pattern.matcher(password).matches()
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val pattern = Pattern.compile(
            "^\\+?[1-9][0-9]{10,14}$"
        )
        return pattern.matcher(phoneNumber).matches()
    }

    fun isValidCPF(cpf: String): Boolean {
        val cpf = cpf.replace("[^0-9]".toRegex(), "")
        if (cpf.length != 11) return false

        // Verifica se todos os dígitos são iguais
        if (cpf.all { it == cpf[0] }) return false

        // Validação do primeiro dígito verificador
        var sum = 0
        for (i in 0..8) {
            sum += (cpf[i].toString().toInt() * (10 - i))
        }
        var digit = 11 - (sum % 11)
        if (digit > 9) digit = 0
        if (digit != cpf[9].toString().toInt()) return false

        // Validação do segundo dígito verificador
        sum = 0
        for (i in 0..9) {
            sum += (cpf[i].toString().toInt() * (11 - i))
        }
        digit = 11 - (sum % 11)
        if (digit > 9) digit = 0
        if (digit != cpf[10].toString().toInt()) return false

        return true
    }

    fun isValidCNPJ(cnpj: String): Boolean {
        val cnpj = cnpj.replace("[^0-9]".toRegex(), "")
        if (cnpj.length != 14) return false

        // Verifica se todos os dígitos são iguais
        if (cnpj.all { it == cnpj[0] }) return false

        // Validação do primeiro dígito verificador
        var sum = 0
        var weight = 5
        for (i in 0..11) {
            sum += (cnpj[i].toString().toInt() * weight)
            weight = if (weight == 2) 9 else weight - 1
        }
        var digit = 11 - (sum % 11)
        if (digit > 9) digit = 0
        if (digit != cnpj[12].toString().toInt()) return false

        // Validação do segundo dígito verificador
        sum = 0
        weight = 6
        for (i in 0..12) {
            sum += (cnpj[i].toString().toInt() * weight)
            weight = if (weight == 2) 9 else weight - 1
        }
        digit = 11 - (sum % 11)
        if (digit > 9) digit = 0
        if (digit != cnpj[13].toString().toInt()) return false

        return true
    }
} 