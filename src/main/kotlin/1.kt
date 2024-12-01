package org.example

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

fun one() {
    val input = Path("src/main/resources/1.txt").readLines()

    val (first, second) = input
        .map {
            it.split("   ").let { (first, second) -> first.toInt() to second.toInt() }
        }
        .unzip()
        .let { (first, second) ->
            first.sorted() to second.sorted()
        }

    val total = first.foldIndexed(initial = 0) { index, acc, value ->
        acc + abs(value - second[index])
    }

    println("One:\t\t $total")
}

fun oneHard() {
    val input = Path("src/main/resources/1.txt").readLines()

    val (first, second) = input
        .map {
            it.split("   ").let { (first, second) -> first.toInt() to second.toInt() }
        }
        .unzip()

    val total = first.fold(initial = 0) { acc, value ->
        val count = second.count { it == value }
        acc + count * value
    }

    println("One hard:\t $total")
}
