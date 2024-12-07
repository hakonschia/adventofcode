package org.example

import kotlin.io.path.Path
import kotlin.io.path.readLines

fun seven() {
    val input = Path("src/main/resources/7.txt").readLines()

    val testValuesAndEquations = input
        .map { line ->
            line.split(": ")
        }
        .map {
            it[0].toLong() to it[1].split(" ").map { it.toLong() }
        }

    val sum = testValuesAndEquations.sumOf { (testValue, equation) ->
        if (isValidEquation(equation, testValue)) {
            testValue
        } else {
            0
        }
    }

    println("Seven $sum")
}

fun sevenHard() {
    val input = Path("src/main/resources/7.txt").readLines()


}

private fun isValidEquation(equation: List<Long>, wantedSum: Long): Boolean {
    var temporarySum = 0L

    fun addOrMultiple(index: Int, add: Boolean) {
        if (index == equation.size) {
            return
        }

        val value = equation[index]

        if (add) {
            println("Equation: $temporarySum + $value = ${temporarySum + value}")
            temporarySum += value
        } else {
            println("Equation: $temporarySum * $value = ${temporarySum * value}")
            temporarySum *= value
        }

        val sumBefore = temporarySum
        addOrMultiple(index + 1, true)

        if (temporarySum == wantedSum) {
            return
        }

        temporarySum = sumBefore
        addOrMultiple(index + 1, false)
    }

    addOrMultiple(0, true)

    if (temporarySum == wantedSum) {
        return true
    } else {
        temporarySum = equation.first()
        addOrMultiple(0, false)

        if (temporarySum == wantedSum) {
            return true
        }
    }

    return false
}
