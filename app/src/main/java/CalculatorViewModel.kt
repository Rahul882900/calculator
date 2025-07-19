package com.example.calculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    private val _equationText = MutableLiveData("")
    val equationText: LiveData<String> = _equationText

    private val _resultText = MutableLiveData("0")
    val resultText: LiveData<String> = _resultText

    fun onButtonClick(btn: String) {
        Log.i("Clicked Button", btn)

        _equationText.value?.let {
            if (btn == "AC") {
                _equationText.value = ""
                _resultText.value = "0"
                return
            }

            if (btn == "C") {
                if (it.isNotEmpty()) {
                    _equationText.value = it.substring(0, it.length - 1)
                }
                return
            }

            if (btn == "=") {
                calculateResult(it)
                return
            }

            _equationText.value = it + btn
        }
    }

    private fun calculateResult(equation: String) {
        try {
            val result = evaluateExpression(equation)
            _resultText.value = if (result % 1 == 0.0) {
                result.toInt().toString()
            } else {
                result.toString()
            }
        } catch (e: Exception) {
            _resultText.value = "Error"
        }
    }

    private fun evaluateExpression(expression: String): Double {

        var expr = expression.replace("\\s+".toRegex(), "")


        while (expr.contains("(")) {
            val innerMost = Regex("\\(([^()]+)\\)").find(expr)
            innerMost?.let {
                val innerExpr = it.groupValues[1]
                val innerResult = evaluateSimpleOperations(innerExpr)
                expr = expr.replace(it.value, innerResult.toString())
            }
        }

        return evaluateSimpleOperations(expr)
    }

    private fun evaluateSimpleOperations(expr: String): Double {
        var currentExpr = expr
        val mulDivRegex = Regex("([+-]?\\d+\\.?\\d*)([*/])([+-]?\\d+\\.?\\d*)")
        while (mulDivRegex.containsMatchIn(currentExpr)) {
            val match = mulDivRegex.find(currentExpr)!!
            val (left, op, right) = match.destructured
            val result = when (op) {
                "*" -> left.toDouble() * right.toDouble()
                "/" -> left.toDouble() / right.toDouble()
                else -> throw IllegalArgumentException("Unknown operator")
            }
            currentExpr = currentExpr.replace(match.value, result.toString())
        }


        val addSubRegex = Regex("([+-]?\\d+\\.?\\d*)([+-])([+-]?\\d+\\.?\\d*)")
        while (addSubRegex.containsMatchIn(currentExpr)) {
            val match = addSubRegex.find(currentExpr)!!
            val (left, op, right) = match.destructured
            val result = when (op) {
                "+" -> left.toDouble() + right.toDouble()
                "-" -> left.toDouble() - right.toDouble()
                else -> throw IllegalArgumentException("Unknown operator")
            }
            currentExpr = currentExpr.replace(match.value, result.toString())
        }

        return currentExpr.toDouble()
    }
}

