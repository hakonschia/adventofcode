import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

fun nine() {
    val input = Path("src/main/resources/9.txt").readText()

    val lengthAndSpace = mutableListOf<Pair<Int, Int>>()

    var totalEmptySpace = 0
    input.chunked(2).forEach { s ->
        val length = "${s[0]}".toInt()
        val emptySpace = "${s.getOrElse(1) { _ -> '0' }}".toInt()

        totalEmptySpace += emptySpace

        lengthAndSpace.add(length to emptySpace)
    }

    fun List<Pair<Int, Int>>.sublistWithSpaces(afterSpace: Int, spaceToTake: Int): List<Pair<Int, Int>> {
        val newList = mutableListOf<Pair<Int, Int>>()
        var spaceUsed = 0
        var totalIndex = 0

        indices.reversed().forEach { index ->
            val (length, _) = get(index)

            var lengthToTakeFromThis = 0
            for (i in 0 until length) {
                if (totalIndex >= afterSpace) {
                    if (spaceUsed < spaceToTake) {
                        lengthToTakeFromThis++
                        spaceUsed++
                    }
                }

                totalIndex++
            }

            if (lengthToTakeFromThis != 0) {
                newList.add(0, index to lengthToTakeFromThis)
            }
        }

        return newList
    }

    val lengthsWithSpacesAdjusted = lengthAndSpace.toMutableList()

    var spaceUsed = 0
    var index = 0
    val path2 = mutableListOf<Int>()

    lengthAndSpace.forEachIndexed { fileId, (length, space) ->
        for (i in 0 until length) {
            path2.add(fileId)
            index++
        }

        val movedFiles = lengthsWithSpacesAdjusted.sublistWithSpaces(spaceUsed, space)

        movedFiles.reversed().forEach { (fileId, length) ->
            for (i in 0 until length) {
                path2.add(fileId)
                spaceUsed++
                index++
            }
        }
    }

    var s = 0L

    val realPath = path2.take(path2.size - spaceUsed)
    realPath.forEachIndexed { index2, fileId ->
        s += index2 * fileId
    }

    println("n9ne  $s")
}

fun nineHard() {
    val input = Path("src/main/resources/9.txt").readLines()

}