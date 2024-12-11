import kotlin.io.path.Path
import kotlin.io.path.readText

fun eleven() {
    val input = Path("src/main/resources/11.txt").readText()

    val numbers = input.split(" ").map { it.toLong() }

    var numbersSoFar = numbers.toList()

    for (i in 0 until 25) {
        val newNumbers = mutableListOf<Long>()

        numbersSoFar.forEach { number ->
            if (number== 0L) {
                newNumbers.add(1)
                return@forEach
            }

            val asString = number.toString()

            if (asString.length % 2 == 0) {
                val split1 = asString.substring(0, asString.length / 2).toLong()
                val split2 = asString.substring(asString.length / 2, asString.length).toLong()

                newNumbers.add(split1)
                newNumbers.add(split2)
                return@forEach
            }

            newNumbers.add(number * 2024L)
        }

        println("finished iteration $i")

        numbersSoFar = newNumbers
    }

    println("Ten easy ${numbersSoFar.size}")
}

fun elevenHard() {
    val input = Path("src/main/resources/11.txt").readText()

    val numbers = input.split(" ").map { it.toInt() }

    println("Ten easy $numbers")
}
