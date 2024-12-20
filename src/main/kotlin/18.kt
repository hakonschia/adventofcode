import utils.Graph
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun eighteen() {
    val input = Path("src/main/resources/18.txt").readLines()
    val corruptedCoordinates = input.map { it.split(",").let { it[0].toInt() to it[1].toInt() } }.take(1024)

    val map = Array(71) { index ->
        CharArray(71) { innerIndex ->
            if (corruptedCoordinates.contains(innerIndex to index)) {
                '#'
            } else {
                '.'
            }
        }
    }

    val path = Graph.dijkstra(
        startPosition = 0 to 0,
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
            position == 70 to 70
        }
    )

    println(path.size - 1)
}

fun eighteenHard() {
    val input = Path("src/main/resources/18.txt").readLines()
    val corruptedCoordinates = input.map { it.split(",").let { it[0].toInt() to it[1].toInt() } }

    val size = 71

    corruptedCoordinates.indices.forEach { corruptedIndex ->
        if (corruptedIndex == 0) return@forEach

        val newList = corruptedCoordinates.take(corruptedIndex)

        val map = Array(size) { index ->
            CharArray(size) { innerIndex ->
                if (newList.contains(innerIndex to index)) {
                    '#'
                } else {
                    '.'
                }
            }
        }

        val path = Graph.dijkstra(
            startPosition = 0 to 0,
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
                position == size - 1 to size - 1
            }
        )

        if (path.isEmpty()) {
            println("${newList.last()}")
            throw IllegalStateException()
        }
    }
}
