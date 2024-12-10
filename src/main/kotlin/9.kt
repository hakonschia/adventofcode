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

    val lengthsWithSpacesAdjusted = lengthAndSpace.mapIndexed { index, pair ->
        index to pair
    }.toMutableList()


    fun List<Pair<Int, Pair<Int, Int>>>.print() {
        forEach {
            val (fileId, pair) = it
            val (length, space) = pair
            for (i in 0 until length) {
                print(fileId)
            }
            for (i in 0 until space) {
                print('.')
            }
        }
        println()
    }

    lengthAndSpace.indices.reversed().forEach { fileId ->
        val (length, _) = lengthAndSpace[fileId]

        //   println("Searching for a place for $fileId who is $length long $lengthsWithSpacesAdjusted")
        val indexOfPlaceItFits = lengthsWithSpacesAdjusted.indexOfFirst { it.second.second >= length }
        if (indexOfPlaceItFits > fileId) {
            return@forEach
        }
        if (indexOfPlaceItFits == -1) {
            return@forEach
        }

        val indexToRemove = lengthsWithSpacesAdjusted.indexOfFirst { it.first == fileId }
        val space = lengthsWithSpacesAdjusted[indexToRemove].second.second
        val placeItFits = lengthsWithSpacesAdjusted[indexOfPlaceItFits]
        println("$fileId can be moved to after $indexOfPlaceItFits $placeItFits")

        lengthsWithSpacesAdjusted.print()

        // Remove space from this as it was set on the one above
        lengthsWithSpacesAdjusted[indexOfPlaceItFits] = placeItFits.first to (placeItFits.second.first to 0)
        println("- Removed space from where we move to")
        lengthsWithSpacesAdjusted.print()
        println()

        println("------${lengthsWithSpacesAdjusted.get(indexToRemove)}")
        lengthsWithSpacesAdjusted.removeAt(indexToRemove)
        println("- Removed $fileId from its original position")
        lengthsWithSpacesAdjusted.print()
        println()

        // Add the item in the space, and make this item have the remaining space if someone else needs it later
        val remainingSpace = placeItFits.second.second - length
        lengthsWithSpacesAdjusted.add(indexOfPlaceItFits + 1, fileId to (length to remainingSpace))
        println("- Moved $fileId to its new position")
        lengthsWithSpacesAdjusted.print()
        println()

        // Update space at the item before the moved item, it should now also have the moved space
        val indexOfBeforeMoved = indexToRemove
        val beforeMoved = lengthsWithSpacesAdjusted[indexOfBeforeMoved]
        lengthsWithSpacesAdjusted[indexOfBeforeMoved] = beforeMoved.first to (beforeMoved.second.first to (beforeMoved.second.second + length + space))
        println("- Added space to item before the moved one ($beforeMoved) $indexOfBeforeMoved ${beforeMoved.second.second} $length $space")
        lengthsWithSpacesAdjusted.print()


        println("iteration over")

        println()
        println()
    }

    lengthsWithSpacesAdjusted.print()
    println()

    var totalIndex = 0
    var s = 0L
    lengthsWithSpacesAdjusted.forEach {
        val (fileId, pair) = it
        val (length, space ) = pair
        for (i in 0 until length) {
            println("added $fileId")
            s += totalIndex * fileId
            totalIndex++
        }
        for (i in 0 until space) {
            totalIndex++
        }
    }

    println("n9ne $s")
}

fun nineHard() {
    val input = Path("src/main/resources/9.txt").readLines()

}

/*
    val lengthsWithSpacesAdjusted = lengthAndSpace.toMutableList()

    lengthAndSpace.indices.reversed().forEach { fileId ->
        val (length, _) = lengthAndSpace[fileId]

        println("Searching for a place for $fileId who is $length long $lengthsWithSpacesAdjusted")
        val indexOfPlaceItFits = lengthsWithSpacesAdjusted.indexOfFirst { it.second >= length }
        if (indexOfPlaceItFits > fileId) {
            println("$fileId can move, but its backwards lul")
            return@forEach
        }
        if (indexOfPlaceItFits == -1) {
            println("$fileId doesnt fit anywhere, not moving")
            return@forEach
        }

        val fuckingCuck = lengthsWithSpacesAdjusted[indexOfPlaceItFits]
        println("$fileId can be moved to after $indexOfPlaceItFits $fuckingCuck")

        val adjusted =  fuckingCuck.second - length

        // Remove space from this as it was set on the one above
        lengthsWithSpacesAdjusted[indexOfPlaceItFits] = fuckingCuck.first to 0
        println("- Removed space from where we move to")
            lengthsWithSpacesAdjusted.print()


        // Update space at the item before the moved item, it should now also have the moved space
        lengthsWithSpacesAdjusted[fileId - 1].let {
            lengthsWithSpacesAdjusted[fileId - 1] = it.first to it.second + length
        }
        println("- Added space to item before the moved one")
        println("- $lengthsWithSpacesAdjusted")

        lengthsWithSpacesAdjusted.removeAt(fileId)
        println("- Removed moved from its original position")
        println("- $lengthsWithSpacesAdjusted")

        // Add the item in the space, and make this item have the remaining space if someone else needs it later
        // -fileId to know the fileId of moved items
        lengthsWithSpacesAdjusted.add(indexOfPlaceItFits + 1, -fileId to adjusted)
        println("- Moved the moving to its new position")
        println("- $lengthsWithSpacesAdjusted")

        println()
    }
 */