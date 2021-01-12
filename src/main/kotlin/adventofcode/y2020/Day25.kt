package adventofcode.y2020

import adventofcode.Day

/**
 * Advent of code day 25: https://adventofcode.com/2020/day/25
 */
class Day25(val input: List<String>) : Day {

    val cardPublicKey = input[0].toInt()
    val doorPublicKey = input[1].toInt()

    val mod = 20201227
    val initialSubjectNumber = 7L

    override fun part1(): Long {
        val cardLoops = coerceLoopSize(cardPublicKey.toLong())

        var acc = 1L
        repeat(cardLoops) {
            acc = (acc * doorPublicKey) % mod
        }

        return acc
    }

    private fun coerceLoopSize(publicKey: Long): Int {
        var acc = 1L
        var loopCount = 0
        while (acc != publicKey) {
            acc = (acc * initialSubjectNumber) % mod
            loopCount++
        }
        return loopCount
    }

    override fun part2() = "Freebie :)"
}
