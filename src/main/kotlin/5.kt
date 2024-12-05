import kotlin.io.path.Path
import kotlin.io.path.readLines

fun five() {
    val input = Path("src/main/resources/5.txt").readLines()

    val (ordering, updates) = input.toOrdersAndUpdates()

    val sum = updates
        .filter { update ->
            update.isValidUpdate(ordering)
        }
        .sumOf { update ->
            update[update.size / 2]
        }

    println("Five: $sum")
}

fun fiveHard() {
    val input = Path("src/main/resources/5.txt").readLines()

    val (ordering, updates) = input.toOrdersAndUpdates()

    val sum = updates
        .filter { update ->
            !update.isValidUpdate(ordering)
        }
        .sumOf { invalidUpdate ->
            val fixed = invalidUpdate.toMutableList()

            while (!fixed.isValidUpdate(ordering)) {
                ordering.forEach { order ->
                    val indexOfFirst = fixed.indexOf(order.first)
                    val indexOfSecond = fixed.indexOf(order.second)

                    if (indexOfFirst != -1 && indexOfSecond != -1) {
                        if (indexOfFirst > indexOfSecond) {
                            fixed.add(index = indexOfFirst + 1, order.second)
                            fixed.removeAt(indexOfSecond)
                        }
                    }
                }
            }

            return@sumOf fixed[fixed.size / 2]
        }

    println("Five hard: $sum")
}

private fun List<String>.toOrdersAndUpdates(): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
    val separator = indexOfFirst { it.isBlank() }
    val ordering = subList(0, separator)
        .map { it.split("|").let { it[0].toInt() to it[1].toInt() } }

    val updates = subList(separator + 1, size)
        .map { it.split(",").map { it.toInt() } }

    return ordering to updates
}

private fun List<Int>.isValidUpdate(ordering: List<Pair<Int, Int>>): Boolean {
    ordering.forEach { order ->
        val indexOfFirst = indexOf(order.first)
        val indexOfSecond = indexOf(order.second)

        if (indexOfFirst != -1 && indexOfSecond != -1) {
            if (indexOfFirst > indexOfSecond) {
                return false
            }
        }
    }

    return true
}
