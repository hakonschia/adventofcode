package org.example

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

fun two() {
    val input = Path("src/main/resources/2.txt").readLines()

    val reports = input.map {
        it.split(" ").map { it.toInt() }
    }

    val safe = reports.filter { !it.isUnsafe() }.size

    println("One:\t\t $safe")
}

fun twoHard() {
    val input = Path("src/main/resources/2.txt").readLines()

    val reports = input.map {
        it.split(" ").map { it.toInt() }
    }

    val safeCount = reports.count { internal ->
        for (i in internal.indices) {
            val newList = internal.toMutableList().apply { removeAt(i) }
            if (!newList.isUnsafe()) {
                return@count true
            }
        }

        return@count false
    }

    println("Twohard:\t\t $safeCount")
}

private fun List<Int>.isUnsafe(): Boolean {
    var previous = get(0)
    val isIncreasing = get(1) > previous

    for (i in 1 until size) {
        val current = get(i)
        val isThisIncreasing = current > previous

        if (isThisIncreasing == isIncreasing) {
            val diff = abs(current - previous)
            if (diff !in 1..3) {
                return true
            }
        } else {
            return true
        }

        previous = current
    }

    return false
}
