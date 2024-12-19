import kotlin.io.path.*

fun nineteen() {
    val input = Path("src/main/resources/19.txt").readLines()

    val patterns = input.first().split(", ")
    val designs = input.subList(2, input.size)

    println(patterns)
    println(designs)

    val possibleDesigns = designs.count { design -> design.isPossible(patterns).also { println("$design $it") } }

    println(possibleDesigns)
}

private fun String.isPossible(patterns: List<String>): Boolean {
    val design = this
    val cache = mutableMapOf<String, Boolean>()

    fun tryCombination(currentCombination: String): Boolean {
        if (currentCombination == design) {
            cache[currentCombination] = true
            return true
        }

        if (!design.startsWith(currentCombination)) {
            cache[currentCombination] = false
            return false
        }

        if (currentCombination in cache) {
            return cache[currentCombination]!!
        }

        patterns.forEach { pattern ->
            val newCombination = currentCombination + pattern

            if (tryCombination(newCombination)) {
                cache[newCombination] = true
                return true
            }
        }

        cache[currentCombination] = false
        return false
    }

    return tryCombination("")
}
