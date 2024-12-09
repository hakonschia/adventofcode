import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

fun nine() {
    val input = Path("src/main/resources/9.txt").readText()

    println(input)

    val lengthAndSpace = mutableListOf<Pair<Int, Int>>()

    var totalEmptySpace = 0
    var str = ""
    input.chunked(2).forEachIndexed { index, s ->
        //println(s)
        val length = "${s[0]}".toInt()
        val emptySpace = "${s.getOrElse(1) { _ -> '0' }}".toInt()

        totalEmptySpace += emptySpace

        str += "$index".repeat(length)
        str += ".".repeat(emptySpace)

        // println(str)
        lengthAndSpace.add(length to emptySpace)
    }

    var totalSum = 0L
    var spacePosition = 0
    var remainingAvailableSpace = totalEmptySpace
    lengthAndSpace.forEachIndexed { index, pair ->
        //println("ID $index has length ${pair.first} and then ${pair.second} empty space")


        for (i in 0 until pair.second) {
            remainingAvailableSpace--
        }
    }

    val path = mutableListOf<Int>()

    /*
    89947706548 wrong
    17462933598374 too high
    4784430163756 too low
     */
    var emptySpaceUsed = 0
    str.forEachIndexed { index, c ->
        // Quick and dirty to stop after all empty space has been finished
        if (index < str.length - totalEmptySpace) {
            val emptySpaceLeft = totalEmptySpace - emptySpaceUsed
            val id = lengthAndSpace.findFileForCurrentSpace(totalEmptySpace, emptySpaceLeft)

            if (c == '.') {
                if (id != -1) {

                    // println("found $id at index $index")

                    id.toString().forEach {
                       // println("$id - $it")
                        val asInt = "$it".toInt()
                        path.add(asInt)
                        totalSum += (id * index)
                        emptySpaceUsed++
                    }
                }

                // println("added $index * $id = ${id * index} (moved)")
            } else {
                path.add("$c".toInt())

                val s = index * "$c".toLong()
                totalSum += s
                //println("added $index * $c = ${s}")
            }
        }
    }

    var s2 = 0
    path.forEachIndexed { index, i ->
        s2 += index * i
    }

    //println(path.toString())

    println("sum $totalSum $s2")

    /*
        val strr = buildString {
            append(str)

            var totalIndex = length - 1

            for (i in lengthAndSpace.size - 1 downTo 0) {
                val current = lengthAndSpace[i]
                for (j in 0 until current.first) {

                    val indexOfLastNotEmptySpace = indexOfLast { it != '.' }
                    val indexOfFirstEmptySpace = indexOf('.')
                    if (indexOfLastNotEmptySpace > indexOfFirstEmptySpace) {
                        println(this)
                        replace(indexOfFirstEmptySpace, indexOfFirstEmptySpace + 1, "$i")
                        replace(totalIndex, totalIndex + 1, ".")
                        totalIndex--
                    }
                }
            }
        }





        println(str)
        val strCopy = str.toCharArray()

        fun shouldContinue(): Boolean {
            val indexOfLastNotEmptySpace = strCopy.indexOfLast { it != '.' }
            val indexOfFirstEmptySpace = strCopy.indexOf('.')

            return indexOfLastNotEmptySpace > indexOfFirstEmptySpace
        }

        var i = 0

        while (shouldContinue()) {
            val indexOfLastNotEmptySpace = strCopy.indexOfLast { it != '.' }
            val indexOfFirstEmptySpace = strCopy.indexOf('.')

            val notEmptySpaceChar = strCopy[indexOfLastNotEmptySpace]
            strCopy[indexOfFirstEmptySpace] = notEmptySpaceChar
            strCopy[indexOfLastNotEmptySpace] = '.'
            i++
            //  println(strCopy)
        }


        val realString = buildString {
            strCopy.forEach { char ->
                if (char != '.') {
                    append(char)
                }
            }
        }

        println("real: $realString")

        var sum: ULong = 0u
        realString.forEachIndexed { index, c ->
            val asInt = "$c".toULong()
            val s = index.toULong() * asInt
            sum += s
        }

        println("sum: $sum")

     */
}

private fun List<Pair<Int, Int>>.findFileForCurrentSpace(totalSpace: Int, spacePosition: Int): Int {
    var currentSpacePosition = totalSpace

    indices.reversed().forEach { index ->
        val (length, _) = get(index)

        for (i in 0 until length) {
            if (currentSpacePosition == spacePosition) {
                return index
            }
            currentSpacePosition--
        }
    }

    return -1
}

fun nineHard() {
    val input = Path("src/main/resources/9.txt").readLines()

}