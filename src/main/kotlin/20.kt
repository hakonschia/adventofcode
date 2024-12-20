import utils.Graph
import kotlin.io.path.*

fun twenty() {
    val input = Path("src/main/resources/20.txt").readLines()

    val map = input.map { it.toCharArray() }.toTypedArray()
    val startPosition = map.findChar('S')

    val path = Graph.dijkstra(
        startPosition = startPosition.second to startPosition.first,
        neighbors = { position, _ ->
            listOf(
                Graph.Edge(position.first + 1 to position.second, 1),
                Graph.Edge(position.first - 1 to position.second, 1),
                Graph.Edge(position.first to position.second + 1, 1),
                Graph.Edge(position.first to position.second - 1, 1)
            ).filter {
                map[it.vertexPosition] != null && map[it.vertexPosition] != '#'
            }
        },
        endCondition = { position ->
            map[position] == 'E'
        }
    )

    val counts = hashMapOf<Int, Int>()

    map.forEachIndexed { index, chars ->
        chars.forEachIndexed { innerIndex, c ->
            val cheatingPath = Graph.dijkstra(
                startPosition = startPosition.second to startPosition.first,
                neighbors = { position, _ ->
                    listOf(
                        Graph.Edge(position.first + 1 to position.second, 1),
                        Graph.Edge(position.first - 1 to position.second, 1),
                        Graph.Edge(position.first to position.second + 1, 1),
                        Graph.Edge(position.first to position.second - 1, 1)
                    ).filter {
                        if (it.vertexPosition == index to innerIndex) {
                            true
                        } else {
                            map[it.vertexPosition] != null && map[it.vertexPosition] != '#'
                        }
                    }
                },
                endCondition = { position ->
                    map[position] == 'E'
                }
            )

            val count = counts.getOrDefault(cheatingPath.size, 0)
            counts[cheatingPath.size] = count + 1
        }
    }

    var over100Count = 0
    counts.filter { it.key != path.size }.forEach { (t, u) ->
        val saved = path.size - t
        if (saved >= 100) {
            over100Count += u
        }
    }

    println(over100Count)
}
