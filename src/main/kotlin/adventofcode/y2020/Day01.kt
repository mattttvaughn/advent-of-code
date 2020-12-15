package adventofcode.y2020

import adventofcode.Day

class Day01(val input: List<String>) : Day {
    private val hashSet = input.map { it.toInt() }.toHashSet()

    override fun part1(): Any {
        hashSet.forEach {
            if (hashSet.contains(2020 - it)) {
                return it * (2020 - it)
            }
        }
        throw IllegalStateException("No result found")
    }

    // pt.2
    override fun part2(): Any {
        hashSet.forEach { first: Int ->
            hashSet.forEach { second: Int ->
                val sum = first + second
                if (hashSet.contains(2020 - sum)) {
                    return first * second * (2020 - sum)
                }
            }
        }
        throw IllegalStateException("No result found")
    }
}

