fun List<CharArray>.print() {
    forEach { outer ->
        outer.forEach { inner ->
            print(inner)
        }
        println()
    }
}

fun Array<CharArray>.print() {
    forEach { outer ->
        outer.forEach { inner ->
            print(inner)

        }
        println()
    }
}

fun List<CharArray>.mutableCopy(): Array<CharArray> {
    return map {
        it.clone()
    }.toTypedArray()
}

fun Array<CharArray>.findChar(char: Char): Pair<Int, Int> {
    forEachIndexed { index, outer ->
        outer.forEachIndexed { innerIndex, inner ->
            if (inner == char) {
                return index to innerIndex
            }
        }
    }

    throw IllegalStateException("$char not found")
}

operator fun Array<CharArray>.get(position: Pair<Int, Int>): Char? {
    return try {
        get(position.second)[position.first]
    } catch (e: IndexOutOfBoundsException) {
        null
    }
}

operator fun Array<CharArray>.set(position: Pair<Int, Int>, char: Char) {
    try {
        this[position.second][position.first] = char
    } catch (_: IndexOutOfBoundsException) {
    }
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return first + other.first to second + other.second
}

operator fun Pair<Int, Int>.times(other: Int): Pair<Int, Int> {
    return (first * other) to (second * other)
}
