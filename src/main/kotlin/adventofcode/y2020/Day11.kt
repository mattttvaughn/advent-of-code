package adventofcode.y2020

import adventofcode.Day
import java.io.File

class Day11(val input: List<String>): Day {
    override fun part1() = countSteadyOccupied(4, ::countAdjacent)
    override fun part2() = countSteadyOccupied(5, ::firstInDirection)

    private fun countSteadyOccupied(maxAdjacent: Int, countFxn: (List<String>, Int, Int, Int, Int) -> Char?): Int {
        var occupied = 0
        var prevOccupied = -1
        var seats = input
        while (occupied != prevOccupied) {
            seats = iterate(seats, maxAdjacent, countFxn)
            prevOccupied = occupied
            occupied = seats.sumBy { line -> line.count { it == '#' } }
        }
        return occupied
    }

    private fun iterate(
        seats: List<String>,
        maxAdjacent: Int,
        countingFxn: (List<String>, Int, Int, Int, Int) -> Char?
    ): List<String> {
        val copy = mutableListOf<String>()
        for (y in seats.indices) {
            val sb = StringBuilder()
            for (x in seats[0].indices) {
                val currChar = seats[y][x]
                val adjacents = (-1..1).sumBy { innerX ->
                    (-1..1).count { innerY ->
                        if (innerX == 0 && innerY == 0) return@count false
                        countingFxn(seats, y, innerY, x, innerX) == '#'
                    }
                }
                sb.append(
                    when {
                        currChar == '#' && adjacents >= maxAdjacent -> 'L'
                        currChar == 'L' && adjacents == 0 -> '#'
                        else -> currChar
                    }
                )
            }
            copy.add(sb.toString())
        }
        return copy
    }

    private fun countAdjacent(seatMap: List<String>, y: Int, innerY: Int, x: Int, innerX: Int) =
        seatMap.getOrNull(y + innerY)?.getOrNull(x + innerX)

    // return first 'L' or 'X' char seen from (x, y) in the direction (xDir, yDir) of [seatMap], else return null
    private fun firstInDirection(seatMap: List<String>, y: Int, yDir: Int, x: Int, xDir: Int): Char? {
        var prevChar: Char? = '.'
        var stepCount = 1
        while (prevChar != null) {
            prevChar = seatMap.getOrNull(y + yDir * stepCount)?.getOrNull(x + xDir * stepCount)
            if (prevChar != '.') return prevChar else stepCount++
        }
        return null
    }
}