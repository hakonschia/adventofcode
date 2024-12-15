import kotlin.io.path.*

fun thirteen() {
    val input = Path("src/main/resources/13.txt").readLines()

    val machines = input.filter { it.isNotBlank() }.chunked(3).map {
        val aX = it[0].substringAfter("X+").substringBefore(",").toInt()
        val aY = it[0].substringAfter("Y+").substringBefore(",").toInt()
        val bX = it[1].substringAfter("X+").substringBefore(",").toInt()
        val bY = it[1].substringAfter("Y+").substringBefore(",").toInt()
        val priceX = it[2].substringAfter("X=").substringBefore(",").toLong()
        val priceY = it[2].substringAfter("Y=").substringBefore(",").toLong()

        Machine(
            priceAt = priceX to priceY,
            aMovements = aX to aY,
            bMovements = bX to bY
        )
    }

    val sum = machines.sumOf { it.findTokens() }
    println("13 easy $sum")
}

fun thirteenHard() {
    val input = Path("src/main/resources/13.txt").readLines()

    val machines = input.filter { it.isNotBlank() }.chunked(3).map {
        val aX = it[0].substringAfter("X+").substringBefore(",").toInt()
        val aY = it[0].substringAfter("Y+").substringBefore(",").toInt()
        val bX = it[1].substringAfter("X+").substringBefore(",").toInt()
        val bY = it[1].substringAfter("Y+").substringBefore(",").toInt()
        val priceX = it[2].substringAfter("X=").substringBefore(",").toInt() + 10000000000000L
        val priceY = it[2].substringAfter("Y=").substringBefore(",").toInt() + 10000000000000L

        Machine(
            priceAt = priceX to priceY,
            aMovements = aX to aY,
            bMovements = bX to bY
        )
    }

    val sum = machines.sumOf { it.cramers() }
    println("13 hard $sum")
}

private data class Machine(
    val priceAt: Pair<Long, Long>,
    val aMovements: Pair<Int, Int>,
    val bMovements: Pair<Int, Int>,
)

private fun Machine.findTokens(): Int {
    for (aTimes in 0 until 100) {
        for (bTimes in 0 until 100) {
            val xPos = aMovements.first.toLong() * aTimes + bMovements.first * bTimes
            val yPos = aMovements.second.toLong() * aTimes + bMovements.second * bTimes

            if (priceAt.first == xPos && priceAt.second == yPos) {
                return aTimes * 3 + bTimes
            }
        }
    }

    return 0
}

private fun Machine.cramers(): Long {
    // https://www.chilimath.com/lessons/advanced-algebra/cramers-rule-with-two-variables/

    // For the first example
    // Button A: X+94, Y+34
    // Button B: X+22, Y+67
    // Prize: X=8400, Y=5400

    // 94x + 22y = 8400
    // 34x + 67y = 5400

    // (94 * 67) - (22 * 34) = 5550
    val determinant = (aMovements.first * bMovements.second) - (bMovements.first * aMovements.second)
    // (8400 * 67) - (22 * 5400) = 444000
    val aDeterminant = (priceAt.first * bMovements.second) - (bMovements.first * priceAt.second)
    // (94 * 5400) - (8400 * 34) = 222000
    val bDeterminant = (aMovements.first * priceAt.second) - (priceAt.first * aMovements.second)

    val aTimes = aDeterminant / determinant
    val bTimes = bDeterminant / determinant

    val xPos = (aMovements.first * aTimes) + (bMovements.first * bTimes)
    val yPos = (aMovements.second * aTimes) + (bMovements.second * bTimes)

    val position = xPos to yPos

    return if (position == priceAt) {
        aTimes * 3 + bTimes
    } else {
        0
    }
}
