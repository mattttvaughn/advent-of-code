package adventofcode.y2020

import adventofcode.Day
import java.io.File

class Day03(val input: List<String>): Day {

    private val treeChar = '#'
    private val slopes = listOf(
        listOf(1, 1),
        listOf(3, 1),
        listOf(5, 1),
        listOf(7, 1),
        listOf(1, 2)
    )

    override fun part1() = sumTreesForSlope(input, 1, 3)

    override fun part2() = slopes.foldRight(1L) { slope: List<Int>, cum: Long ->
        cum * sumTreesForSlope(input, slope[1], slope[0])
    }

    private fun sumTreesForSlope(input: List<String>, down: Int, right: Int): Int {
        return input.foldIndexed(Pair(0, 0)) { index: Int, acc: Pair<Int, Int>, line: String ->
            return@foldIndexed if (index % down != 0) {
                acc
            } else {
                val p1 = acc.first + if (line[acc.second] == treeChar) 1 else 0
                Pair(p1, (acc.second + right) % line.length)
            }
        }.first
    }
}
