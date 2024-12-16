import kotlin.io.path.*

fun fifteen() {
    val input = Path("src/main/resources/15.txt").readLines()

    val (map, moves) = input.let { inp ->
        val blankLine = inp.indexOfFirst { it.isBlank() }

        val map = inp.subList(0, blankLine).map { it.toCharArray() }.toTypedArray()
        val movements = inp.subList(blankLine, inp.size).joinToString("")

        map to movements
    }

    var position = map.findChar('@')

    moves.forEach { move ->
        val movementDelta = when (move) {
            '<' -> -1 to 0
            '>' -> 1 to 0
            '^' -> 0 to -1
            'v' -> 0 to 1
            else -> throw IllegalStateException("$move")
        }

        val positionToMoveTo = position + movementDelta

        val atNewPos = map[positionToMoveTo] ?: return
        println("move: $move $positionToMoveTo $atNewPos")

        when (atNewPos) {
            // Wall, do nothing
            '#' -> {
                println("Hit a wall")
            }

            // Empty spot, move there
            '.' -> {
                println("Hit an empty spot")
                map[position] = '.'
                map[positionToMoveTo] = '@'

                position = positionToMoveTo
            }

            // Box, move the box (and all immediately after)
            'O' -> {
                var movesToDo = 1
                var atAfterBox = map[positionToMoveTo + movementDelta]

                while (atAfterBox == 'O') {
                    atAfterBox = map[positionToMoveTo + (movementDelta * (movesToDo + 1))]
                    movesToDo++
                }

                val isAWall = map[positionToMoveTo + (movementDelta * movesToDo)] == '#'

                if (!isAWall) {
                    for (i in 0 until movesToDo) {
                        val p = positionToMoveTo + (movementDelta * (movesToDo - i))
                        println("\t$p")
                        map[p] = 'O'
                        //  map.print()
                        //  println()
                        //  println()
                    }
                    map[positionToMoveTo] = '@'
                    map[position] = '.'

                    position = positionToMoveTo
                }
            }
        }
        println()
    }

    var sum = 0
    map.forEachIndexed { index, chars ->
        chars.forEachIndexed { innerIndex, c ->
            if (c == 'O') {
                sum += index * 100 + innerIndex
            }
        }
    }

    println(sum)
}
