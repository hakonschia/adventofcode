package org.example

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.system.measureTimeMillis
import kotlin.time.measureTimedValue

fun seven() {
    val input = Path("src/main/resources/7.txt").readLines()

    val testValuesAndEquations = input
        .map { line ->
            line.split(": ")
        }
        .map {
            it[0].toLong() to it[1].split(" ").map { it.toLong() }
        }

    val (sum, time) = measureTimedValue {
        testValuesAndEquations.sumOf { (testValue, equation) ->
            if (isValid(equation = equation, wantedSum = testValue, sumSoFar = equation.first(), index = 1, concatenate = false)) {
                testValue
            } else {
                0
            }
        }
    }

    println("Seven $sum $time")
}

fun sevenHard() {
    val input = Path("src/main/resources/7.txt").readLines()

    val testValuesAndEquations = input
        .map { line ->
            line.split(": ")
        }
        .map {
            it[0].toLong() to it[1].split(" ").map { it.toLong() }
        }

    val (sum, time) = measureTimedValue {
        testValuesAndEquations.sumOf { (testValue, equation) ->
            if (isValid(equation = equation, wantedSum = testValue, sumSoFar = equation.first(), index = 1, concatenate = true)) {
                testValue
            } else {
                0
            }
        }
    }

    println("Sevenhard $sum $time")
}

private fun isValid(
    equation: List<Long>,
    wantedSum: Long,
    sumSoFar: Long,
    index: Int,
    concatenate: Boolean
): Boolean {
    if (index == equation.size) {
        return sumSoFar == wantedSum
    }

    val currentValue = equation[index]

    if (
        isValid(
            equation = equation,
            sumSoFar = sumSoFar + currentValue,
            index = index + 1,
            wantedSum = wantedSum,
            concatenate = concatenate
        )
    ) {
        return true
    }

    if (
        isValid(
            equation = equation,
            sumSoFar = sumSoFar * currentValue,
            index = index + 1,
            wantedSum = wantedSum,
            concatenate = concatenate
        )
    ) {
        return true
    }

    // Makes it run "a lot" faster since we avoid the string to long conversion
    if (sumSoFar > wantedSum) return false

    if (
        concatenate &&
        isValid(
            equation = equation,
            sumSoFar = "$sumSoFar$currentValue".toLong(),
            index = index + 1,
            wantedSum = wantedSum,
            concatenate = concatenate
        )
    ) {
        return true
    }

    return false
}
