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
            if (equation.isValid(wantedSum = testValue, concatenate = false)) {
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
            if (equation.isValid(wantedSum = testValue, concatenate = true)) {
                testValue
            } else {
                0
            }
        }
    }

    println("Sevenhard $sum $time")
}

private fun List<Long>.isValid(wantedSum: Long, concatenate: Boolean): Boolean {
    fun isValid(
        sumSoFar: Long,
        index: Int
    ): Boolean {
        if (index == size) {
            return sumSoFar == wantedSum
        }

        val currentValue = get(index)

        if (isValid(sumSoFar = sumSoFar + currentValue, index = index + 1)) return true
        if (isValid(sumSoFar = sumSoFar * currentValue, index = index + 1)) return true

        // Not necessary as it will be caught in the next iteration
        // But makes it run significantly faster since we avoid some unnecessary string to long conversions
        if (sumSoFar > wantedSum) return false
        if (concatenate && isValid(sumSoFar = "$sumSoFar$currentValue".toLong(), index = index + 1)) return true

        return false
    }

    // Either send first() with index = 1, or 0 and 0 and handle if (index == 0) for the multiplication case
    // But 0 and 0 is a lot slower!
    return isValid(sumSoFar = first(), index = 1)
}

