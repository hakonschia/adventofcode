import kotlin.io.path.*

data class Robot(
    val position: Pair<Int, Int>,
    val velocity: Pair<Int, Int>
)

fun fourteen() {
    val input = Path("src/main/resources/14.txt").readLines()

    val robots = input.map { line ->
        val numbers = "-?\\d+".toRegex().findAll(line).map { it.value.toInt() }.toList()
        Robot(
            position = numbers[0] to numbers[1],
            velocity = numbers[2] to numbers[3],
        )
    }

    val size = 101 to 103
    val robotsAtPosition = mutableMapOf<Pair<Int,Int>, Int>()

    robots.forEach { robot ->
        var position = robot.position

        for (i in 0 until 100) {
            val first = position.first + robot.velocity.first
            val second = position.second + robot.velocity.second

            val realFirst = if (first >= size.first) {
                (first % size.first)
            } else if (first < 0) {
                (size.first + first)
            } else {
                first
            }

            val realSecond = if (second >= size.second) {
                (second % size.second)
            } else if (second < 0) {
                (size.second + second)
            } else {
                second
            }

            position = realFirst to realSecond
        }

        val count = robotsAtPosition.getOrDefault(position, 0)
        robotsAtPosition[position] = count + 1
    }

    var q1 = 0
    var q2 = 0
    var q3 = 0
    var q4 = 0

    val middleRow = size.first / 2
    val middleColumn = size.second / 2

    robotsAtPosition.forEach { (position, count) ->
        if (position.first == middleRow || position.second == middleColumn) {
            return@forEach
        }

        if (position.first > middleRow) {
            if (position.second > middleColumn) {
                q1 += count
            } else {
                q2 += count
            }
        } else {
            if (position.second > middleColumn) {
                q3 += count
            } else {
                q4 += count
            }
        }
    }

    val sum = q1 * q2 * q3 * q4

    println("$sum $q1 $q2 $q3 $q4")
}
