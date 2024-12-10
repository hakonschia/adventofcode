import kotlin.io.path.Path
import kotlin.io.path.readLines

fun ten() {
    val input = Path("src/main/resources/10.txt").readLines()

    val map = input.map { it.map { "$it".toInt() } }

    val trailheads = mutableListOf<Pair<Int, Int>>()
    map.forEachIndexed { index, ints ->
        ints.forEachIndexed { innerIndex, i ->
            if (i == 0) {
                trailheads.add(index to innerIndex)
            }
        }
    }

    val sum = trailheads.sumOf { map.findPathsToTop(hard = false, it) }

    println("Ten easy $sum")
}

fun tenHard() {
    val input = Path("src/main/resources/10.txt").readLines()

    val map = input.map { it.map { "$it".toInt() } }

    val trailheads = mutableListOf<Pair<Int, Int>>()
    map.forEachIndexed { index, ints ->
        ints.forEachIndexed { innerIndex, i ->
            if (i == 0) {
                trailheads.add(index to innerIndex)
            }
        }
    }

    val sum = trailheads.sumOf { map.findPathsToTop(hard = true, it) }

    println("Ten hard $sum")
}

private fun List<List<Int>>.findPathsToTop(hard: Boolean, position: Pair<Int, Int>): Int {
    val trailsFound: MutableCollection<Pair<Int, Int>> = if (hard) {
        mutableListOf()
    } else {
        mutableSetOf()
    }

    fun find(position: Pair<Int, Int>, value: Int) {
        if (position.first < 0 || position.first >= size || position.second < 0 || position.second >= size) {
            return
        }

        val next = get(position.first).get(position.second)

        if (next != value + 1) {
            return
        }

        if (next == 9) {
            trailsFound.add(position)
            return
        }

        find(position.first + 1 to position.second, next)
        find(position.first to position.second + 1, next)
        find(position.first - 1 to position.second, next)
        find(position.first to position.second - 1, next)
    }

    find(position, -1)

    return trailsFound.size
}
