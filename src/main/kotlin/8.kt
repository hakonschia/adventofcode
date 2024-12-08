import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

fun eight() {
    val input = Path("src/main/resources/8.txt").readLines()

    val map = input.map { it.toCharArray() }.toTypedArray()

    val positions = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()

    map.forEachIndexed { outerIndex, charArray ->
        charArray.forEachIndexed { innerIndex, char ->
            if (char != '.') {
                val list = positions.getOrDefault(char, mutableListOf())

                list.add(outerIndex to innerIndex)

                positions[char] = list
            }
        }
    }

    val safeToEditMap = input.map { it.toCharArray() }.toTypedArray()

    var count = 0

    positions.forEach { key, innerPositions ->
        innerPositions.forEach { outer ->
            innerPositions.forEach { inner ->
                val directionY = if (inner.first < outer.first) {
                    abs(inner.first - outer.first)
                } else {
                    outer.first - inner.first
                }

                val directionX = if (inner.second < outer.second) {
                    abs(inner.second - outer.second)
                } else {
                    outer.second - inner.second
                }

                val direction = directionY to directionX

                val antiNodeA = (outer.first + direction.first) to (outer.second + direction.second)
                val antiNodeB = inner.first + directionY to inner.second + directionX

                println("outer=$outer inner=$inner antinodeA: $antiNodeA antinodeB: $antiNodeB, direction $direction")

                try {
                    val current = safeToEditMap[antiNodeA.first][antiNodeA.second]
                    if (current == '#') {
                        count++
                        println("- Already found # at $antiNodeA for $key!")
                    }

                    if (current == '.') {
                        safeToEditMap[antiNodeA.first][antiNodeA.second] = '#'
                        count++
                        println("- put # at $antiNodeA for $key (count=$count)")
                        safeToEditMap.print()
                    }
                } catch (e: IndexOutOfBoundsException) {
                    println("- out of bounds $antiNodeA")
                }

                try {
                    val current = safeToEditMap[antiNodeB.first][antiNodeB.second]
                    if (current == '#') {
                        println("- Already found # at $antiNodeB for $key!")
                        count++
                    }

                    if (current == '.') {
                        safeToEditMap[antiNodeB.first][antiNodeB.second] = '#'
                        count++
                        println("- put # at $antiNodeB for $key (count=$count)")
                        safeToEditMap.print()
                    }
                } catch (e: IndexOutOfBoundsException) {
                    println("- out of bounds $antiNodeB")
                }
            }
            println()
        }
    }

    safeToEditMap.print()

    println("Eight $count, size=${safeToEditMap.size}")
}

fun eightHard() {
    val input = Path("src/main/resources/8.txt").readLines()

}
