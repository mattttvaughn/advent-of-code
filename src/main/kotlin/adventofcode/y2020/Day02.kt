package adventofcode.y2020

import adventofcode.Day

class Day02(val input: List<String>) : Day {

    override fun part1(): Any = input.map { it.split(" ") }
        .count { segments: List<String> ->
            val range: List<Int> = segments[0].split("-")
                .map { it.toInt() }
            val neededChar = segments[1].replace(":", "")
            val sortedString = segments[2].toList()
                .sorted()
                .joinToString("")
            val longEnough = sortedString.contains(neededChar.repeat(range[0]))
            val tooLong = sortedString.contains(neededChar.repeat(range[1] + 1))

            return@count longEnough && !tooLong
        }

    override fun part2() = input.map { it.split(" ") }
        .count { segments: List<String> ->
            val range: List<Int> = segments[0]
                .split("-")
                .map { it.toInt() }
            val neededChar = segments[1].replace(":", "")[0]

            val p1 = segments[2][range[0] - 1] == neededChar
            val p2 = segments[2][range[1] - 1] == neededChar

            return@count p1 xor p2
        }
}
