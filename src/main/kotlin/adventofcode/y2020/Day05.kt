package adventofcode.y2020

import adventofcode.Day
import java.lang.IllegalStateException

// Avoided bitwise operations for the sake of time. Definitely was the way to go in retrospect
class Day05(val input: List<String>): Day{

    private val rowValues = listOf(64, 32, 16, 8, 4, 2, 1)
    private val colValues = listOf(4, 2, 1)

    private val seats = input.map { pass ->
        val row = pass.substring(0, 7).foldIndexed(0) { index, acc, passChar ->
            return@foldIndexed acc + if (passChar == 'B') rowValues[index] else 0
        }
        val col = pass.substring(7).foldIndexed(0) { index, acc, passChar ->
            return@foldIndexed acc + if (passChar == 'R') colValues[index] else 0
        }
        val id = row * 8 + col
        id
    }.toSet()

    override fun part1() = seats.maxOrNull() ?: throw IllegalStateException("Null pt.1")
    override fun part2() : Any = ((seats.minOrNull() ?: 0)..(seats.maxOrNull() ?: 0)).forEach { seatValue ->
        if (!seats.contains(seatValue)) {
            return seatValue
        }
    }
}
