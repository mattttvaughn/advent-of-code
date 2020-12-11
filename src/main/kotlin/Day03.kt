import java.io.File

const val treeChar = '#'

fun main() {

    val input: List<String> = File("src/main/resources/day03.txt").readLines()

    println("Pt.1: ${sumTreesForSlope(input, 1, 3)}")

    val slopes = listOf(
        listOf(1, 1),
        listOf(3, 1),
        listOf(5, 1),
        listOf(7, 1),
        listOf(1, 2)
    )
    println("Pt.2: ${slopes.foldRight(1L){ slope: List<Int>, cum: Long ->
       cum * sumTreesForSlope(input, slope[1], slope[0])
    }}")
}

fun sumTreesForSlope(input: List<String>, down: Int, right: Int): Int {
    return input.foldIndexed(Pair(0, 0)) { index: Int, acc: Pair<Int, Int>, line: String ->
        return@foldIndexed if (index % down != 0) {
            acc
        } else {
            Pair(acc.first + if (line[acc.second] == treeChar) 1 else 0, (acc.second + right) % line.length)
        }
    }.first
}
