package adventofcode.y2020

import adventofcode.Day

class Day06(val input: List<String>) : Day {

    override fun part1() = input.fold(0) { acc, group ->
        acc + group.replace("\n", "").toSortedSet().size
    }

    override fun part2() = input.fold(0) { acc, group ->
        val freqMap = mutableMapOf<Char, Int>()
        val groupSize = group.count { it == '\n' } + 1
        group.replace("\n", "").forEach {
            freqMap[it] = freqMap.getOrElse(it) { 0 } + 1
        }
        acc + freqMap.count { it.value == groupSize }
    }
}
