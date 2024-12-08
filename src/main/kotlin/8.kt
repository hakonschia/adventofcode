import kotlin.io.path.Path
import kotlin.io.path.readLines

fun eight() {
    val input = Path("src/main/resources/8.txt").readLines()

    val map = input.map { it.toCharArray() }.toTypedArray()
    val positionsFiltered = map.findPositions(false)

    println("Eight ${positionsFiltered.size}")
}

fun eightHard() {
    val input = Path("src/main/resources/8.txt").readLines()

    val map = input.map { it.toCharArray() }.toTypedArray()
    val safeToEditMap = input.map { it.toCharArray() }.toTypedArray()

    val positionsFiltered = map.findPositions(true)

    positionsFiltered.forEachIndexed { index, pair ->
        val position = pair.second
        safeToEditMap[position.first][position.second] = '#'
    }

    var additionalCount = 0
    safeToEditMap.forEachIndexed { index, chars ->
        chars.forEach { char ->
            if (char != '#' && char != '.') {
                additionalCount++
            }
        }
    }

    println("Eight ${positionsFiltered.size + additionalCount} $additionalCount")
}

private fun Array<CharArray>.findPositions(hard: Boolean): List<Pair<Char, Pair<Int, Int>>> {
    val positions = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
    val map = this

    map.forEachIndexed { outerIndex, charArray ->
        charArray.forEachIndexed { innerIndex, char ->
            if (char != '.') {
                val list = positions.getOrDefault(char, mutableListOf())

                list.add(outerIndex to innerIndex)

                positions[char] = list
            }
        }
    }

    val positionsAdded = mutableListOf<Pair<Char, Pair<Int, Int>>>()

    positions.forEach { (key, innerPositions) ->
        innerPositions.forEach { outer ->
            innerPositions.forEach { inner ->
                val distance = (outer.first - inner.first) to (outer.second - inner.second)

                // Will add tons of out-of-bounds nodes, will just remove them later
                val iterations = if (hard) map.size else 1

                for (i in 1 until iterations + 1) {
                    val antiNodeA = (outer.first + (distance.first * i)) to (outer.second + (distance.second * i))
                    val antiNodeB = inner.first + (distance.first * i) to inner.second + (distance.second * i)

                    positionsAdded.add(key to antiNodeA)
                    positionsAdded.add(key to antiNodeB)
                }
            }
        }
    }

    return positionsAdded
        .filter { (_, position) -> position.first >= 0 && position.first < map.size && position.second >= 0 && position.second < map.size }
        .filterIndexed { index, (key, position) ->
            map[position.first][position.second] == '.' || map[position.first][position.second] != key
        }
        .distinctBy { it.second }
}
