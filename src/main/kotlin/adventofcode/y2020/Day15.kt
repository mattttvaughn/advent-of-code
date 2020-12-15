package adventofcode.y2020

import adventofcode.Day

class Day15(val input: List<String>) : Day {

    private val numList = input[0]
        .split(",")
        .map(String::toInt)

    override fun part1() = playGame(2020)
    override fun part2() = playGame(30000000)

    private fun playGame(stopAt: Int): Int {
        var prev = -1
        var count = 0
        val turnLastSpoken = HashMap<Int, Int>(3611442)
        while (count < stopAt) {
            val spoken = when {
                count < numList.size -> numList[count]
                prev !in turnLastSpoken.keys -> 0
                else -> count - (turnLastSpoken[prev] ?: 0)
            }
            turnLastSpoken[prev] = count++
            prev = spoken
        }
        return prev
    }
}
