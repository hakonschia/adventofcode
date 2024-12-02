package org.example

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

fun two() {
    val input = Path("src/main/resources/2.txt").readLines()

    val reports = input.map {
        it.split(" ").map { it.toInt() }
    }

    val unsafeCount = reports.filter { it.isUnsafe() }.size

    val safe = (reports.size - unsafeCount)

    println("One:\t\t $safe")
}

fun twoHard() {
    val input = Path("src/main/resources/2.txt").readLines()

    val reports = input.map {
        it.split(" ").map { it.toInt() }
    }

    val originalUnsafeCount = reports.filter { it.isUnsafe() }.size

    var safeCount = 0

    reports.forEach { internal ->
        if (internal.isUnsafe()) {
            var safeList: List<Int>? = null

            for (i in internal.indices) {
                if (safeList == null) {
                    val newList = internal.toMutableList().apply { removeAt(i) }
                    if (!newList.isUnsafe()) {
                        safeList = newList
                        println("$newList is safe!")
                    }
                }
            }

            if (safeList != null) {
                safeCount++
            }
        }
    }

    val safe = reports.size - originalUnsafeCount + safeCount

    println("Twohard:\t\t $safe")
}

private fun List<Int>.isUnsafe(): Boolean {
    var previous = get(0)
    val isIncreasing = get(1) > previous
    var hasSeenUnsafe = false

    for (i in 1 until size) {
        val current = get(i)
        val isThisIncreasing = current > previous

        if (isThisIncreasing == isIncreasing) {
            val diff = abs(current - previous)
            if (diff !in 1..3) {
                hasSeenUnsafe = true
            }
        } else {
            hasSeenUnsafe = true
        }

        previous = current
    }

    return hasSeenUnsafe
}
