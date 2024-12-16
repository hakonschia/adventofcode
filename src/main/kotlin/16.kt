import utils.Graph
import kotlin.io.path.*

fun sixteen() {
    val input = Path("src/main/resources/16.txt").readLines()

    val map = input.map { it.toCharArray() }.toTypedArray()
    val startPosition = map.findChar('S')

    fun Pair<Int, Int>.flattenedIndex(): Int {
        return first * map.size + second
    }

    val path = Graph.dijkstra(
        // Reversed for some reason
        startPosition = startPosition.second to startPosition.first,
        neighbors = { position, parent ->
            // First is facing east
            val realParent = parent ?: (position.first - 1 to position.second)

            listOf(
                (position.first + 1 to position.second).let {
                    val weight = if ((realParent.first + 1 to realParent.second) == position) {
                        1
                    } else {
                        1001
                    }

                    Graph.Edge(it, weight)
                },
                (position.first - 1 to position.second).let {
                    val weight = if ((realParent.first - 1 to realParent.second) == position) {
                        1
                    } else {
                        1001
                    }

                    Graph.Edge(it, weight)
                },
                (position.first to position.second + 1).let {
                    val weight = if ((realParent.first to realParent.second + 1) == position) {
                        1
                    } else {
                        1001
                    }

                    Graph.Edge(it, weight)
                },
                (position.first to position.second - 1).let {
                    val weight = if ((realParent.first to realParent.second - 1) == position) {
                        1
                    } else {
                        1001
                    }

                    Graph.Edge(it, weight)
                }
            ).filter {
                map[it.vertexPosition] != null && map[it.vertexPosition] != '#'
            }
        },
        endCondition = { position ->
            map[position] == 'E'
        }
    )

    println(path.last().weight)
}
