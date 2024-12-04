import kotlin.io.path.Path
import kotlin.io.path.readLines

fun four() {
    val input = Path("src/main/resources/4.txt").readLines()

    val asChar = input.map { it.toCharArray() }.toTypedArray()

    val soupSolver = SoupSolver(asChar)

    soupSolver.findWord("XMAS")

 //   println(soupSolver.generateBoard())
    println("Found a total of ${soupSolver.solutions.size}")
}

fun fourHard() {
    val input = Path("src/main/resources/4.txt").readLines()

    val asChar = input.map { it.toCharArray() }.toTypedArray()

    val soupSolver = SoupSolver(asChar)

    soupSolver.findWord("MAS")

    val aCoordsFound = mutableListOf<Coordinates>()
    soupSolver.solutions.forEach { solution ->
        val aCoord = when (solution.direction) {
            WordDirection.HORIZONTAL -> Coordinates(
                x = solution.startCoordinates.x + 1,
                y = solution.startCoordinates.y
            )
            WordDirection.HORIZONTAL_REVERSE -> Coordinates(
                x = solution.endCoordinates.x + 1,
                y = solution.startCoordinates.y
            )
            WordDirection.VERTICAL -> Coordinates(
                x = solution.startCoordinates.x,
                y = solution.startCoordinates.y + 1
            )
            WordDirection.VERTICAL_REVERSE -> Coordinates(
                x = solution.startCoordinates.x,
                y = solution.startCoordinates.y - 1
            )
            WordDirection.DIAGONAL_DOWN -> Coordinates(
                x = solution.startCoordinates.x + 1,
                y = solution.startCoordinates.y + 1
            )
            WordDirection.DIAGONAL_DOWN_REVERSE -> Coordinates(
                x = solution.startCoordinates.x - 1,
                y = solution.startCoordinates.y + 1
            )
            WordDirection.DIAGONAL_UP -> Coordinates(
                x = solution.startCoordinates.x + 1,
                y = solution.startCoordinates.y - 1
            )
            WordDirection.DIAGONAL_UP_REVERSE -> Coordinates(
                x = solution.startCoordinates.x - 1,
                y = solution.startCoordinates.y - 1
            )
        }

        if (aCoordsFound.contains(aCoord)) {
            return@forEach
        }

        try {
            /*
            S.S
            .A
            M.M
             */
            if (
                asChar[aCoord.y - 1][aCoord.x - 1] == 'S' &&
                asChar[aCoord.y - 1][aCoord.x + 1] == 'S' &&
                asChar[aCoord.y + 1][aCoord.x - 1] == 'M' &&
                asChar[aCoord.y + 1][aCoord.x + 1] == 'M'
            ) {
                aCoordsFound.add(aCoord)
            } else if (
            /*
            .M.S
            ..A.
            .M.S
             */
                asChar[aCoord.y - 1][aCoord.x - 1] == 'M' &&
                asChar[aCoord.y - 1][aCoord.x + 1] == 'S' &&
                asChar[aCoord.y + 1][aCoord.x - 1] == 'M' &&
                asChar[aCoord.y + 1][aCoord.x + 1] == 'S'
            ) {
                aCoordsFound.add(aCoord)
            } else if (
                asChar[aCoord.y - 1][aCoord.x - 1] == 'M' &&
                asChar[aCoord.y - 1][aCoord.x + 1] == 'M' &&
                asChar[aCoord.y + 1][aCoord.x - 1] == 'S' &&
                asChar[aCoord.y + 1][aCoord.x + 1] == 'S'
            ) {
                /*
                .M.M
                ..A.
                .S.S
                 */
                aCoordsFound.add(aCoord)
            } else if (
                asChar[aCoord.y - 1][aCoord.x - 1] == 'S' &&
                asChar[aCoord.y - 1][aCoord.x + 1] == 'M' &&
                asChar[aCoord.y + 1][aCoord.x - 1] == 'S' &&
                asChar[aCoord.y + 1][aCoord.x + 1] == 'M'
            ) {
                aCoordsFound.add(aCoord)
                /*
                .S.M
                ..A.
                .S.M
                 */
            }
        } catch (e: ArrayIndexOutOfBoundsException) {

        }
    }

    println("x-MASes ${aCoordsFound.size}")
}

/**
 * @param boardToSolve The board to solve (must be an n*n square)
 */
class SoupSolver(
    boardToSolve: Array<CharArray>
) {

    /**
     * The board to solve
     */
    private val board: Array<CharArray>

    /**
     * The board being solved, with the current solutions added as upper case characters
     */
    private val boardWithSolutions: Array<CharArray>

    /**
     * The internal solutions found so far
     */
    private val _solutions: MutableList<SoupWordSolution> = ArrayList()

    /**
     * The solutions found so far. This list will not contain duplicate solutions
     */
    val solutions: List<SoupWordSolution> = _solutions

    init {
        board = boardToSolve.clone()

        // Ensure all characters are lowercased as a lowercase letter is seen as "no solution" for that given character
        board.forEachIndexed { outerIndex, row ->
            // Check that each row is equal size to the column (the board is a perfect square)
            check(row.size == board.size) {
                "Given board is not a square"
            }

            row.forEachIndexed { innerIndex, c ->
                board[outerIndex][innerIndex] = c.lowercaseChar()
            }
        }

        boardWithSolutions = board.clone()
    }

    /**
     * Finds a word in the board
     *
     * @param word The word to find
     *
     * @return The solution for the word, or `null` if the word wasn't found. If the solution is a duplicate then it is
     * still returned here, but is not added to [solutions]
     */
    fun findWord(word: String) {
        val checkers = listOf(
            this::checkRows,
            this::checkReverseRows,
            this::checkColumns,
            this::checkReverseColumns,
            this::checkDownwardsDiagonal,
            this::checkReverseDownwardsDiagonal,
            this::checkUpwardsDiagonal,
            this::checkReverseUpwardsDiagonal
        )

        // Call each function until we find a match
        checkers.forEach { checker ->
            checker.invoke(word)
        }
    }

    /**
     * Generates the current board with the current solutions.
     *
     * @return A string representation of the board. Capital letters indicate a solution
     */
    fun generateBoard(): String {
        val solution = boardWithSolutions.clone()
        val solutionAsRawString = StringBuilder()

        solution.forEachIndexed { index, array ->
            solutionAsRawString.append(array.joinToString(" "))

            // Don't add a newline after the last line
            if (index + 1 != solution.size) {
                solutionAsRawString.append("\n")
            }
        }

        return solutionAsRawString.toString()
    }

    /**
     * Modifies [boardWithSolutions] with a solution
     */
    private fun modifySolutionBoard(solution: SoupWordSolution) {
        // Change boardWithSolutions so that the word is capitalized in the matrix

        val word = solution.word
        word.forEachIndexed { index, _ ->
            val coordinatesToChange: Coordinates = when (solution.direction) {
                // Moving horizontally, only the x changes
                WordDirection.HORIZONTAL -> {
                    Coordinates(x = solution.startCoordinates.x + index, y = solution.startCoordinates.y)
                }

                // Moving reverse horizontally, only the x changes, but subtract the index
                WordDirection.HORIZONTAL_REVERSE -> {
                    Coordinates(x = solution.startCoordinates.x - index, y = solution.startCoordinates.y)
                }

                // Moving vertically, only the y changes
                WordDirection.VERTICAL -> {
                    Coordinates(x = solution.startCoordinates.x, y = solution.startCoordinates.y + index)
                }

                // Moving vertically horizontally, only the y changes, but subtract the index
                WordDirection.VERTICAL_REVERSE -> {
                    Coordinates(x = solution.startCoordinates.x, y = solution.startCoordinates.y - index)
                }

                // Moving diagonally down, x and y both change
                WordDirection.DIAGONAL_DOWN -> {
                    Coordinates(x = solution.startCoordinates.x + index, y = solution.startCoordinates.y + index)
                }

                // Moving diagonally down reversed, x change is negative, y is positive
                WordDirection.DIAGONAL_DOWN_REVERSE -> {
                    Coordinates(x = solution.startCoordinates.x - index, y = solution.startCoordinates.y + index)
                }

                // Moving diagonally up, x change is positive, y is negative
                WordDirection.DIAGONAL_UP -> {
                    Coordinates(x = solution.startCoordinates.x + index, y = solution.startCoordinates.y - index)
                }

                // Moving diagonally reversed up, x and y both change (negatively)
                WordDirection.DIAGONAL_UP_REVERSE -> {
                    Coordinates(x = solution.startCoordinates.x - index, y = solution.startCoordinates.y - index)
                }
            }

            val xToChange = coordinatesToChange.x
            val yToChange = coordinatesToChange.y

            boardWithSolutions[yToChange][xToChange] = boardWithSolutions[yToChange][xToChange].uppercaseChar()
        }
    }


    // The only thing we really need to do to check if a word is valid is build a string of the lines
    // going horizontal, vertical, diagonally etc. and check if the word is a substring of the word we built
    // Then we need to figure out the coordinates of where it starts and ends

    // (0, 0) in the board is the top left, so some logic below is somewhat reversed of what is natural
    // E.g. the image below is the downwards diagonal
    /*
    - - - . - - -
    - - - - . - -
    - - - - - . -
     */

    var solutionCount = 0

    /**
     * Checks the rows for a word
     *
     * @return A solution, or `null` if no solution was found
     */
    private fun checkRows(word: String): SoupWordSolution? {
        board.forEachIndexed { rowIndex, row ->
            val rowAsString = row.joinToString("")

            solutionCount += word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(rowAsString).toList().size

            word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(rowAsString).forEach {
                it.groups.filterNotNull().forEach { group ->
                    val startPos = group.range.first

                    SoupWordSolution(
                        word = word,
                        startCoordinates = Coordinates(x = startPos, y = rowIndex),
                        // -1 as "WORD" ends at index 3, not 4
                        endCoordinates = Coordinates(x = startPos + word.length - 1, y = rowIndex),
                        direction = WordDirection.HORIZONTAL
                    ).also {
                        if (!_solutions.contains(it)) {
                            _solutions.add(it)
                            modifySolutionBoard(it)
                        }
                    }
                }
            }
        }
        // No solution found
        return null
    }

    /**
     * Checks the reverse rows for a word (i.e. backwards words on the rows)
     *
     * @return A solution, or `null` if no solution was found
     */
    private fun checkReverseRows(word: String): SoupWordSolution? {
        board.forEachIndexed { rowIndex, row ->
            val rowAsString = row.joinToString("").reversed()


            solutionCount += word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(rowAsString).toList().size
            word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(rowAsString).forEach {
                it.groups.filterNotNull().forEach { group ->
                    val startPos = group.range.first


                    SoupWordSolution(
                        word = word,
                        // The start and end for reversed words are also reversed

                        // For row: RNESPOMSWPZBDA
                        // The reversed word "MOPSEN" is from index 1 to 6 (in the original word as "NESPOM")

                        // Reverse the row: ADBZPWSMOPSENR
                        // startPos = 7

                        // -1 as "WORD" ends at index 3, not 4
                        // 14 - 7 - 1 = 6
                        startCoordinates = Coordinates(x = rowAsString.length - startPos - 1, y = rowIndex),

                        // 14 - 7 - 6 = 1
                        endCoordinates = Coordinates(x = rowAsString.length - startPos - word.length, y = rowIndex),
                        direction = WordDirection.HORIZONTAL_REVERSE
                    ).also {
                        if (!_solutions.contains(it)) {
                            _solutions.add(it)
                            modifySolutionBoard(it)
                        }
                    }
                }
            }

        }

        // No solution found
        return null
    }


    /**
     * Builds a word from [board] for a given column
     *
     * @param column The column to build on (no checks is done that this is a valid number)
     * @return The word built on the given column
     */
    private fun buildColumnWord(column: Int): String {
        val columnWordBuilder = StringBuilder()

        // Append the character at the column for each row to build the word
        board.forEach { row ->
            columnWordBuilder.append(row[column])
        }

        return columnWordBuilder.toString()
    }

    /**
     * Checks the columns word a word
     *
     * @return A solution, or `null` if no solution was found
     */
    private fun checkColumns(word: String): SoupWordSolution? {
        for (rowIndex in board.indices) {
            val columnWord = buildColumnWord(rowIndex)

            solutionCount += word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(columnWord).toList().size

            word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(columnWord).forEach {
                it.groups.filterNotNull().forEach { group ->
                    val startPos = group.range.first

                    SoupWordSolution(
                        word = word,
                        startCoordinates = Coordinates(x = rowIndex, y = startPos),
                        // -1 as "WORD" ends at index 3, not 4
                        endCoordinates = Coordinates(x = rowIndex, y = startPos + word.length - 1),
                        direction = WordDirection.VERTICAL
                    ).also {
                        if (!_solutions.contains(it)) {
                            _solutions.add(it)
                            modifySolutionBoard(it)
                        }
                    }

                }
            }
        }

        // No solution found
        return null
    }

    /**
     * Checks the reverse columns for a word (i.e. columns going upwards)
     *
     * @return A solution, or `null` if no solution was found
     */
    private fun checkReverseColumns(word: String): SoupWordSolution? {
        for (rowIndex in board.indices) {
            val columnWord = buildColumnWord(rowIndex).reversed()

            solutionCount += word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(columnWord).toList().size

            word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(columnWord).forEach {
                it.groups.filterNotNull().forEach { group ->
                    val startPos = group.range.first

                    SoupWordSolution(
                        word = word,

                        // See documentation for the reversed rows
                        // Using "board.size" we assume an n*n square (which is currently true based on init{}"
                        startCoordinates = Coordinates(x = rowIndex, y = board.size - startPos - 1),
                        endCoordinates = Coordinates(x = rowIndex, y = board.size - startPos - word.length),
                        direction = WordDirection.VERTICAL_REVERSE
                    ).also {
                        if (!_solutions.contains(it)) {
                            _solutions.add(it)
                            modifySolutionBoard(it)
                        }
                    }
                }
            }
        }

        // No solution found
        return null
    }


    /**
     * Builds a word on the downward diagonal for a column or row in [board]
     *
     * @param columnOrRow The index of the column or row to build the diagonal on
     * @param buildForColumns If true, [columnOrRow] indicates the index of a column, if false it indicates the index of a row
     */
    private fun buildDownwardDiagonal(columnOrRow: Int, buildForColumns: Boolean): String {
        val wordBuilder = StringBuilder()

        for (i in 0 until board.size - columnOrRow) {
            // The diagonal is offset by the column/row index
            if (buildForColumns) {
                wordBuilder.append(board[i][i + columnOrRow])
            } else {
                wordBuilder.append(board[i + columnOrRow][i])
            }
        }

        return wordBuilder.toString()
    }


    /**
     * Checks the downwards diagonal for a word
     *
     * @return A solution, or `null` if no solution was found
     */
    private fun checkDownwardsDiagonal(word: String): SoupWordSolution? {
        for (rowIndex in board.indices) {
            // On an n*n square we can use rowIndex, but if we're not on n*n this wouldn't work
            val columnWord = buildDownwardDiagonal(rowIndex, buildForColumns = true)

            solutionCount += word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(columnWord).toList().size

            word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(columnWord).forEach {
                it.groups.filterNotNull().forEach { group ->
                    val startPosColumn = group.range.first

                    SoupWordSolution(
                        word = word,
                        startCoordinates = Coordinates(x = rowIndex + startPosColumn, y = startPosColumn),
                        endCoordinates = Coordinates(
                            x = rowIndex + startPosColumn + word.length - 1,
                            y = startPosColumn + word.length - 1
                        ),
                        direction = WordDirection.DIAGONAL_DOWN
                    ).also {
                        if (!_solutions.contains(it)) {
                            _solutions.add(it)
                            modifySolutionBoard(it)
                        }
                    }

                }
            }

            val rowWord = buildDownwardDiagonal(rowIndex, buildForColumns = false)

            solutionCount += word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(rowWord).toList().size

            word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(rowWord).forEach {
                it.groups.filterNotNull().forEach { group ->
                    val startPosRow = group.range.first

                    SoupWordSolution(
                        word = word,

                        startCoordinates = Coordinates(x = startPosRow, y = rowIndex + startPosRow),
                        endCoordinates = Coordinates(
                            x = startPosRow + word.length - 1,
                            y = rowIndex + startPosRow + word.length - 1
                        ),
                        direction = WordDirection.DIAGONAL_DOWN
                    ).also {
                        if (!_solutions.contains(it)) {
                            _solutions.add(it)
                            modifySolutionBoard(it)
                        }
                    }

                }
            }
        }

        // No solution found
        return null
    }


    /**
     * Builds a word on the downward reverse diagonal for a column or row in [board]. Words are built from the top right
     * of the board
     *
     * @param columnOrRow The index of the column or row to build the diagonal on
     * @param buildForColumns If true, [columnOrRow] indicates the index of a column, if false it indicates the index of a row
     */
    private fun buildReverseDownwardDiagonal(columnOrRow: Int, buildForColumns: Boolean): String {
        val wordBuilder = StringBuilder()

        for (i in 0 until board.size - columnOrRow) {
            val pos = board.size - 1 - i

            // Not sure how to explain this
            // We're starting from top right, i.e. (0, board.size - 1)
            // So just look at a board and try to figure it out :)
            if (buildForColumns) {
                wordBuilder.append(board[i][pos - columnOrRow])
            } else {
                wordBuilder.append(board[columnOrRow + i][pos])
            }
        }

        return wordBuilder.toString()
    }


    /**
     * Checks the reverse downwards diagonal for a word
     *
     * @return A solution, or `null` if no solution was found
     */
    private fun checkReverseDownwardsDiagonal(word: String): SoupWordSolution? {
        for (rowIndex in board.indices) {
            // On an n*n square we can use rowIndex, but if we're not on n*n this wouldn't work
            val columnWord = buildReverseDownwardDiagonal(rowIndex, buildForColumns = true)

            solutionCount += word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(columnWord).toList().size

            word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(columnWord).forEach {
                it.groups.filterNotNull().forEach { group ->
                    val startPosColumn = group.range.first

                    SoupWordSolution(
                        word = word,
                        startCoordinates = Coordinates(
                            // - 1 on the board size since we're 0-indexed
                            x = board.size - 1 - rowIndex - startPosColumn,
                            // Y is just downwards, and the entire diagonal is in columnWord
                            y = startPosColumn
                        ),
                        endCoordinates = Coordinates(
                            // + 1 for the correct word index
                            x = board.size - 1 - rowIndex - startPosColumn - word.length + 1,
                            // - 1 for the correct word index
                            y = startPosColumn + word.length - 1
                        ),
                        direction = WordDirection.DIAGONAL_DOWN_REVERSE
                    ).also {
                        if (!_solutions.contains(it)) {
                            _solutions.add(it)
                            modifySolutionBoard(it)
                        }
                    }

                }
            }


            val rowWord = buildReverseDownwardDiagonal(rowIndex, buildForColumns = false)

            // We need to ignore the case as words
            solutionCount += word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(rowWord).toList().size
            word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(rowWord).forEach {
                it.groups.filterNotNull().forEach { group ->
                    val startPosRow = group.range.first

                    SoupWordSolution(
                        word = word,
                        startCoordinates = Coordinates(
                            x = (board.size - 1) - startPosRow,
                            y = rowIndex + startPosRow
                        ),
                        endCoordinates = Coordinates(
                            x = (board.size - 1) - startPosRow - (word.length - 1),
                            y = rowIndex + startPosRow + word.length - 1
                        ),
                        direction = WordDirection.DIAGONAL_DOWN_REVERSE
                    ).also {
                        if (!_solutions.contains(it)) {
                            _solutions.add(it)
                            modifySolutionBoard(it)
                        }
                    }
                }
            }
        }

        // No solution found
        return null
    }

    /**
     * Checks the upwards diagonal for a word
     *
     * @return A solution, or `null` if no solution was found
     */
    private fun checkUpwardsDiagonal(word: String): SoupWordSolution? {
        for (rowIndex in board.indices) {
            // An upwards diagonal is the downwards diagonal reversed, when the word is built for a row (since a diagonal one
            // way starts at a row and ends at a column on the other side)
            val columnWord = buildReverseDownwardDiagonal(rowIndex, buildForColumns = false).reversed()
            solutionCount += word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(columnWord).toList().size

            word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(columnWord).forEach {
                it.groups.filterNotNull().forEach { group ->
                    val startPosColumn = group.range.first

                    SoupWordSolution(
                        word = word,
                        startCoordinates = Coordinates(
                            x = rowIndex + startPosColumn,
                            y = (board.size - 1) - startPosColumn
                        ),
                        endCoordinates = Coordinates(
                            x = rowIndex + startPosColumn + (word.length - 1),
                            y = (board.size - 1) - startPosColumn - (word.length - 1)
                        ),
                        direction = WordDirection.DIAGONAL_UP
                    ).also {
                        if (!_solutions.contains(it)) {
                            _solutions.add(it)
                            modifySolutionBoard(it)
                        }
                    }

                }
            }

            // A reversed upwards diagonal is the downwards diagonal reversed
            val rowWord = buildReverseDownwardDiagonal(rowIndex, buildForColumns = true).reversed()
            solutionCount += word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(rowWord).toList().size

            word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(rowWord).forEach {
                it.groups.filterNotNull().forEach { group ->
                    val startPosRow = group.range.first

                    SoupWordSolution(
                        word = word,
                        startCoordinates = Coordinates(
                            // This is normal left-to-right, so the start index is the x index
                            x = startPosRow,
                            y = (board.size - 1) - rowIndex - startPosRow
                        ),
                        endCoordinates = Coordinates(
                            x = startPosRow + (word.length - 1),
                            // + 1 for the word index (this is a "reversed" word for y)
                            y = (board.size - 1) - rowIndex - startPosRow - word.length + 1
                        ),
                        direction = WordDirection.DIAGONAL_UP
                    ).also {
                        if (!_solutions.contains(it)) {
                            _solutions.add(it)
                            modifySolutionBoard(it)
                        }
                    }

                }
            }
        }

        // No solution found
        return null
    }

    /**
     * Checks the reverse upwards diagonal for a word
     *
     * @return A solution, or `null` if no solution was found
     */
    private fun checkReverseUpwardsDiagonal(word: String): SoupWordSolution? {
        for (rowIndex in board.indices) {
            val columnWord = buildDownwardDiagonal(rowIndex, buildForColumns = false).reversed()

            solutionCount += word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(columnWord).toList().size

            word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(columnWord).forEach {
                it.groups.filterNotNull().forEach { group ->
                    val startPosColumn = group.range.first

                    SoupWordSolution(
                        word = word,
                        startCoordinates = Coordinates(
                            x = (board.size - 1) - startPosColumn - rowIndex,
                            y = (board.size - 1) - startPosColumn
                        ),
                        endCoordinates = Coordinates(
                            x = (board.size - 1) - startPosColumn - rowIndex - (word.length - 1),
                            y = (board.size - 1) - startPosColumn - (word.length - 1)
                        ),
                        direction = WordDirection.DIAGONAL_UP_REVERSE
                    ).also {
                        if (!_solutions.contains(it)) {
                            _solutions.add(it)
                            modifySolutionBoard(it)
                        }
                    }
                }
            }

            val rowWord = buildDownwardDiagonal(rowIndex, buildForColumns = true).reversed()
            solutionCount += word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(rowWord).toList().size

            word.toRegex(setOf(RegexOption.IGNORE_CASE)).findAll(rowWord).forEach {
                it.groups.filterNotNull().forEach { group ->
                    val startPosRow = group.range.first

                    SoupWordSolution(
                        word = word,
                        startCoordinates = Coordinates(
                            x = (board.size - 1) - startPosRow,
                            y = (board.size - 1) - rowIndex - startPosRow
                        ),
                        endCoordinates = Coordinates(
                            x = (board.size - 1) - startPosRow - (word.length - 1),
                            y = (board.size - 1) - rowIndex - startPosRow - word.length + 1
                        ),
                        direction = WordDirection.DIAGONAL_UP_REVERSE
                    ).also {
                        if (!_solutions.contains(it)) {
                            _solutions.add(it)
                            modifySolutionBoard(it)
                        }
                    }
                }
            }
        }

        // No solution found
        return null
    }
}

data class SoupWordSolution(
    val word: String,
    val startCoordinates: Coordinates,
    val endCoordinates: Coordinates,
    val direction: WordDirection
)

/**
 * The valid directions a word can be found in the soup
 */
enum class WordDirection {
    HORIZONTAL,
    HORIZONTAL_REVERSE,
    VERTICAL,
    VERTICAL_REVERSE,
    DIAGONAL_DOWN,
    DIAGONAL_DOWN_REVERSE,
    DIAGONAL_UP,
    DIAGONAL_UP_REVERSE
}

data class Coordinates(
    val x: Int,
    val y: Int
)
