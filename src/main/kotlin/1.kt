package org.example

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

fun one() {
    val input = Path("src/main/resources/1.txt").readLines()

    val totalDistance = input
        .splitToLists()
        .let { (first, second) ->
            first.sorted() to second.sorted()
        }
        .let { (first, second) ->
            first.foldIndexed(initial = 0) { index, acc, value ->
                acc + abs(value - second[index])
            }
        }

    println("One:\t\t $totalDistance")
}

fun oneHard() {
    val input = Path("src/main/resources/1.txt").readLines()

    val similarity = input
        .splitToLists()
        .let { (first, second) ->
            first.fold(initial = 0) { acc, value ->
                val count = second.count { it == value }
                acc + count * value
            }
        }

    println("One hard:\t $similarity")
}

private fun List<String>.splitToLists(): Pair<List<Int>, List<Int>> {
    return this
        .map {
            it.split("   ").let { (first, second) -> first.toInt() to second.toInt() }
        }
        .unzip()
}
