import kotlin.io.path.*

fun thirteen() {
    val input = Path("src/main/resources/13.txt").readLines()

    val machines = input.filter { it.isNotBlank() }.chunked(3).map {
        val aX = it[0].substringAfter("X+").substringBefore(",").toInt()
        val aY = it[0].substringAfter("Y+").substringBefore(",").toInt()
        val bX = it[1].substringAfter("X+").substringBefore(",").toInt()
        val bY = it[1].substringAfter("Y+").substringBefore(",").toInt()
        val priceX = it[2].substringAfter("X=").substringBefore(",").toInt()
        val priceY = it[2].substringAfter("Y=").substringBefore(",").toInt()

        Machine(
            priceAt = priceX to priceY,
            aMovements = aX to aY,
            bMovements = bX to bY
        )
    }

    val sum = machines.sumOf { it.findTokens() }
    println("13 easy $sum")
}

private data class Machine(
    val priceAt: Pair<Int, Int>,
    val aMovements: Pair<Int, Int>,
    val bMovements: Pair<Int, Int>,
)

private fun Machine.findTokens(): Int {
    for (aTimes in 0 until 100) {
        for (bTimes in 0 until 100) {
            val xPos = aMovements.first * aTimes + bMovements.first * bTimes
            val yPos = aMovements.second * aTimes + bMovements.second * bTimes

            if (priceAt.first == xPos && priceAt.second == yPos) {
                return aTimes * 3 + bTimes
            }
        }
    }

    return 0
}
