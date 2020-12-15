package adventofcode.y2020

import adventofcode.Day
import kotlin.math.max

class Day10(val input: List<String>) : Day {
    val adapters = input.map { it.toInt() }
        .toMutableList()
        .apply { addAll(listOf(0, max()!! + 3)) }
        .sorted()

    override fun part1(): Any {
        var diff1 = 0
        var diff3 = 0
        adapters.reduce { prev, joltage ->
            when (joltage - prev) {
                1 -> diff1++
                3 -> diff3++
            }
            joltage
        }
        return diff1 * diff3
    }

    override fun part2(): Any {
        val combos = Array<Long>(adapters.max()!!.toInt()) { 0 }
        val set = adapters.toSet()
        for (idx in combos.indices) {
            if (idx in set) {
                val prev1 = combos.getOrElse(idx - 1) { 0L }
                val prev2 = combos.getOrElse(idx - 2) { 0L }
                val prev3 = combos.getOrElse(idx - 3) { 0L }
                combos[idx] = max(prev1 + prev2 + prev3, 1L)
            }
        }
        return combos.toList().max()!!
    }

}

