package org.example

import kotlin.io.path.Path
import kotlin.io.path.readText

fun three() {
    val input = Path("src/main/resources/3.txt").readText()

    val result = input.findMuls().sumOfMuls()

    println("Three:\t\t $result")
}

fun threeHard() {
    val input = Path("src/main/resources/3.txt").readText()

    val result = input.split("do").sumOf { doGroup ->
        if (!doGroup.contains("n't")) {
            return@sumOf doGroup.findMuls().sumOfMuls()
        }

        return@sumOf 0
    }

    println("Three hard $result")
}

private fun String.findMuls() = "mul\\(\\d{1,3},\\d{1,3}\\)".toRegex().findAll(this)
private fun Sequence<MatchResult>.sumOfMuls(): Long {
    return sumOf {
        val (first, second) = it.value.substringAfter("(").substringBefore(")").split(",").let { it[0].toLong() to it[1].toLong() }

        first * second
    }
}
