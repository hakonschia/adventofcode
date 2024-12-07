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

    val testValuesAndEquations = input
        .map { line ->
            line.split(": ")
        }
        .map {
            it[0].toLong() to it[1].split(" ").map { it.toLong() }
        }

    val invalid = testValuesAndEquations.filterNot { (testValue, equation) -> isValidEquation(equation, testValue) }

    val sum1 = testValuesAndEquations.sumOf { (testValue, equation) ->
        if (isValidEquation(equation, testValue)) {
            testValue
        } else {
            0
        }
    }

    val sum2 = testValuesAndEquations.filterNot { (testValue, equation) -> isValidEquation(equation, testValue) }.sumOf { (testValue, equation) ->
        if (isValidEquation2(equation, testValue)) {
            testValue
        } else {
            0
        }
    }

    println("Sevenh $sum1 + $sum2 = ${sum1 + sum2}")
}

private fun isValidEquation(equation: List<Long>, wantedSum: Long): Boolean {
    var temporarySum = 0L

    fun addOrMultiple(index: Int, add: Boolean) {
        if (index == equation.size) {
            return
        }

        val value = equation[index]

        if (add) {
            //println("Equation: $temporarySum + $value = ${temporarySum + value}")
            temporarySum += value
        } else {
            //println("Equation: $temporarySum * $value = ${temporarySum * value}")
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
        temporarySum = 0
        addOrMultiple(0, false)

        if (temporarySum == wantedSum) {
            return true
        }
    }

    return false
}

private enum class Type {
    Add,
    Multiply,
    CombineAdd,
    CombineMultiple
}

private fun isValidEquation2(equation: List<Long>, wantedSum: Long): Boolean {
    var temporarySum = 0L

    fun addOrMultiple(index: Int, type: Type) {
        if (index + 1 == equation.size) {
            return
        }

        val value = equation[index]
        val combined = "$temporarySum${equation[index]}".toLongOrNull() ?: return

        when (type) {
            Type.Add -> {
                //println("Equation: $temporarySum + $value = ${temporarySum + value} $index")
                temporarySum += value
            }

            Type.Multiply -> {
                //println("Equation: $temporarySum * $value = ${temporarySum * value} $index")
                temporarySum *= value
            }

            Type.CombineAdd -> {
                if (index == 0 && equation.size == 2) {
                    temporarySum = "${equation[0]}${equation[1]}".toLong()
                    //println("Equation comb add: $combined  = $temporarySum")
                } else {
                    //println("Equation comb add: $combined + ${equation[index + 1]} = $temporarySum")
                    temporarySum = combined + equation[index + 1]
                }
            }

            Type.CombineMultiple -> {
                if (index == 0 && equation.size == 2) {
                    temporarySum = "${equation[0]}${equation[1]}".toLong()
                   // println("Equation comb multiple: $combined  = $temporarySum")
                } else {
                    temporarySum = combined * equation[index + 1]
                   // println("Equation comb multiply: $combined * ${equation[index + 1]} = $temporarySum")
                }
            }
        }

        val sumBefore = temporarySum
        addOrMultiple(index + 1, Type.Add)

        if (temporarySum == wantedSum) {
            return
        }
        temporarySum = sumBefore
        addOrMultiple(index + 1, Type.Multiply)

        if (temporarySum == wantedSum) {
            return
        }
        temporarySum = sumBefore
        addOrMultiple(index + 1, Type.CombineAdd)

        if (temporarySum == wantedSum) {
            return
        }
        temporarySum = sumBefore
        addOrMultiple(index + 1, Type.CombineMultiple)
    }

    addOrMultiple(0, Type.Add)

    if (temporarySum == wantedSum) {
        return true
    } else {
        temporarySum = 0
        addOrMultiple(0, Type.Multiply)

        if (temporarySum == wantedSum) {
            return true
        }

        temporarySum = 0
        addOrMultiple(0, Type.CombineAdd)

        if (temporarySum == wantedSum) {
            return true
        } else {
            temporarySum = 0
            addOrMultiple(0, Type.CombineMultiple)

            if (temporarySum == wantedSum) {
                return true
            }
        }
    }

    return false
}

