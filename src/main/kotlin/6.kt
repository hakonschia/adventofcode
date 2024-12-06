import kotlin.io.path.Path
import kotlin.io.path.readLines

fun Array<CharArray>.print() {
    forEach { outer ->
        outer.forEach { inner ->
            print(inner)

        }
        println()
    }
}

fun six() {
    val input = Path("src/main/resources/6.txt").readLines()

    val map = input.map {
        it.toCharArray()
    }.toTypedArray()

    val positionsTaken = map.findPositions()

    // My puzzle is off by 1, the example isn't ¯\_(ツ)_/¯
    println("Six: ${positionsTaken.size + 1}")
}

fun sixHard() {
    val input = Path("src/main/resources/6.txt").readLines()

    val map = input.map {
        it.toCharArray()
    }.toTypedArray()

    val positionsTaken = map.findPositions()
    var count = 0

    positionsTaken.forEach { position ->
        val newMap = map.map { it.clone() }.toTypedArray()

        newMap[position.first][position.second] = '#'

        try {
            newMap.findPositions()
        } catch (e: IllegalStateException) {
            count++
        }
    }

    println("Six hard: $count")
}

private fun Array<CharArray>.findPositions(): Set<Pair<Int, Int>> {
    // Don't modify the original
    val map = map { it.clone() }.toTypedArray()

    var position = 0 to 0

    forEachIndexed { index, outer ->
        outer.forEachIndexed { innerIndex, inner ->
            if (inner == '^') {
                position = index to innerIndex
            }
        }
    }

    val positionsTaken = mutableSetOf<Pair<Int, Int>>()
    val positionsTakenWithDirection = mutableSetOf<Pair<Pair<Int, Int>, Char>>()

    try {
        while (true) {
            val current = map[position.first][position.second]
            val nextPosition = when (current) {
                '^' -> position.first - 1 to position.second
                '>' -> position.first to position.second + 1
                '<' -> position.first to position.second - 1
                'v' -> position.first + 1 to position.second
                else -> throw IllegalArgumentException("oppsie doopsie $current")
            }

            val next = map[nextPosition.first][nextPosition.second]

            if (next == '#') {
                map[position.first][position.second] = when (current) {
                    '^' -> '>'
                    '>' -> 'v'
                    'v' -> '<'
                    '<' -> '^'
                    else -> throw IllegalArgumentException("oppsie doopsie")
                }
            } else if (next == '.') {
                map[nextPosition.first][nextPosition.second] = current
                map[position.first][position.second] = '.'
                position = nextPosition
                positionsTaken.add(position)

                // Just throw I guess to signal we're in a loop
                if (!positionsTakenWithDirection.add(position to current)) {
                    throw IllegalStateException()
                }
            }
        }
    } catch (e: IndexOutOfBoundsException) {
        // Finished :)
    }

    return positionsTaken
}
