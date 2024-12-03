package org.example

import kotlin.io.path.Path
import kotlin.io.path.readText

fun three() {
    val input = Path("src/main/resources/3.txt").readText()

    val result = input.sumOfMuls()

    println("Three:\t\t $result")
}

fun threeHard() {
    val input = Path("src/main/resources/3.txt").readText()

    val result = input.split("do").filter { !it.contains("n't") }.sumOf { doGroup ->
        doGroup.sumOfMuls()
    }

    println("Three hard $result")
}

private fun String.sumOfMuls(): Long {
    return "mul\\(\\d{1,3},\\d{1,3}\\)".toRegex().findAll(this).sumOf {
        val (first, second) = it.value
            .substringAfter("(")
            .substringBefore(")")
            .split(",")
            .let { it[0].toLong() to it[1].toLong() }

        first * second
    }
}
