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
            it[0].toInt() to it[1].split(" ").map { it.toInt() }
        }

    testValuesAndEquations.forEach { (testValue, equation) ->
        println("$testValue and $equation")
    }
}

fun sevenHard() {
    val input = Path("src/main/resources/7.txt").readLines()


}
