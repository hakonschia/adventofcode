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

    val positionsAdded = mutableListOf<Pair<Char, Pair<Int, Int>>>()

    positions.forEach { (key, innerPositions) ->
        innerPositions.forEach { outer ->
            innerPositions.forEach { inner ->
                if (outer != inner) {
                    val distance = (outer.first - inner.first) to (outer.second - inner.second)

                    val antiNodeA = (outer.first + distance.first) to (outer.second + distance.second)
                    val antiNodeB = inner.first + distance.first to inner.second + distance.second

                    positionsAdded.add(key to antiNodeA)
                    positionsAdded.add(key to antiNodeB)
                }
            }
        }
    }

    val positionsFiltered = positionsAdded
        .filter { (_, position) -> position.first >= 0 && position.first < map.size && position.second >= 0 && position.second < map.size }
        .filterIndexed { index, (key, position) ->
            map[position.first][position.second] == '.' || map[position.first][position.second] != key
        }
        .distinctBy { it.second }

    println("Eight ${positionsFiltered.size}")
}

fun eightHard() {
    val input = Path("src/main/resources/8.txt").readLines()

}
