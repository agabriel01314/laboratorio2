import java.util.Stack
import kotlin.math.*

class ScientificCalculator {
    // Función para evaluar una expresión matemática dada como una cadena
    fun evaluate(expression: String): Double {
        val tokens = expression.replace("\\s".toRegex(), "").toCharArray()
        val values = Stack<Double>()
        val operators = Stack<Char>()

        var i = 0
        while (i < tokens.size) {
            when {
                tokens[i] in '0'..'9' -> {
                    val sb = StringBuilder()
                    while (i < tokens.size && (tokens[i] in '0'..'9' || tokens[i] == '.')) {
                        sb.append(tokens[i++])
                    }
                    values.push(sb.toString().toDouble())
                    i--
                }
                tokens[i] == '(' -> operators.push(tokens[i])
                tokens[i] == ')' -> {
                    while (operators.peek() != '(') {
                        values.push(applyOp(operators.pop(), values.pop(), values.pop()))
                    }
                    operators.pop()
                }
                tokens[i] in arrayOf('+', '-', '*', '/', '^', '√') -> {
                    while (operators.isNotEmpty() && hasPrecedence(tokens[i], operators.peek())) {
                        values.push(applyOp(operators.pop(), values.pop(), values.pop()))
                    }
                    operators.push(tokens[i])
                }
            }
            i++
        }

        while (operators.isNotEmpty()) {
            values.push(applyOp(operators.pop(), values.pop(), values.pop()))
        }

        return values.pop()
    }

    // Función para aplicar un operador a dos operandos y devolver el resultado
    private fun applyOp(op: Char, b: Double, a: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> {
                if (b == 0.0) throw UnsupportedOperationException("No se puede dividir por cero")
                a / b
            }
            '^' -> a.pow(b)
            '√' -> sqrt(b)
            else -> throw UnsupportedOperationException("Operador no soportado: $op")
        }
    }

    // Función para verificar la precedencia de los operadores
    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')') return false
        if ((op1 == '*' || op1 == '/' || op1 == '√') && (op2 == '+' || op2 == '-')) return false
        if ((op1 == '^') && (op2 != '^')) return false
        return true
    }
}

fun main() {
    val calculator = ScientificCalculator()
    val expression = "(454 + (34 / 2)^3) + 5"
    try {
        val result = calculator.evaluate(expression)
        println("Resultado: $result")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}
