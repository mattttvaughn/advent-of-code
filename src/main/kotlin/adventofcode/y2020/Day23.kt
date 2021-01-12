package adventofcode.y2020

import adventofcode.Day
import java.util.*

/**
 * Advent of code day 23: https://adventofcode.com/2020/day/23
 *
 * IMPORTANT: Pt. 2 currently takes 5-8 hours to run. This could be reduced dramatically by taking
 * advantage of the fast remove/appends of a LinkedList as well as tracking location of the next cup
 * in order to prevent expensive O(N) lookups every cycle
 */
class Day23(val input: List<String>) : Day {

    val runDay23 = false

    override fun part1(): Any {
        if (!runDay23) {
            return "See source"
        }
        return p1()
    }

    override fun part2(): Any {
        if (!runDay23) {
            return "See source"
        }
        return p2()
    }


    val cupsInput = "523764819".split("").mapNotNull { it.toIntOrNull() }

    fun p1(): Any {
        val steps = 100
        var idx = 0
        val cups = mutableListOf<Int>()
        cups.addAll(cupsInput)
        val max = cups.maxOrNull() ?: throw IllegalStateException("Max must exist")
        for (x in 0 until steps) {
            idx = step(idx, cups, max)
        }
        println(cups.rotate(cups.indexOf(1)).drop(1).joinToString(""))
        return cups.rotate(cups.indexOf(1)).drop(1).joinToString("").toLong()
    }

    fun p2(): Any {
        val fixedInput = (0 until 1_000_000).map {
            if (cupsInput.contains(it + 1)) cupsInput[it] else (it + 1)
        }
        val steps = 10_000_000
        var idx = 0
        val cups = mutableListOf<Int>()
        cups.addAll(fixedInput)
        val max = cups.maxOrNull() ?: throw IllegalStateException("Max must exist")
        var lastMs = System.currentTimeMillis()
        for (x in 0 until steps) {
            if (x % 10_000 == 0) {
                println("$x: ${System.currentTimeMillis() - lastMs}ms")
                lastMs = System.currentTimeMillis()
            }
            idx = step(idx, cups, max)
        }
        val indexOf1 = cups.indexOf(1)
        return cups.getOrRollOver(indexOf1 + 1).toLong() * cups.getOrRollOver(indexOf1 + 2).toLong()
    }

    // Use this over creating a new list every step
    val pickedUpValues = mutableListOf(-1, -1, -1)
    val pickedUpIndices = TreeSet { i1: Int, i2: Int -> i2.compareTo(i1) }

    /**
     * Given the [idx] to act upon and a [List] of [Int]s [cups], perform a
     * step in the algorithm
     */
    fun step(currIdx: Int, cups: MutableList<Int>, max: Int): Int {
        val currVal = cups[currIdx]
        pickedUpIndices.clear()
        (1..3).forEach { i ->
            val idx = Math.floorMod(currIdx + i, cups.size)
            pickedUpValues[i - 1] = cups[idx]
            pickedUpIndices.add(idx)
        }

        // select a destination cup where label is next smallest label number, ignoring picked-up
        var destLabel = -1
        var destCount = 1
        while (destLabel == -1) {
            var possDestValue = Math.floorMod(currVal - destCount++, cups.size)
            if (possDestValue == 0) {
                possDestValue = max
            }
            if (possDestValue !in pickedUpValues && possDestValue > 0) {
                destLabel = possDestValue
            }
        }

        for (pick in pickedUpIndices) {
            cups.removeAt(pick)
        }
        cups.addAll(cups.indexOf(destLabel) + 1, pickedUpValues)

        return Math.floorMod(cups.indexOf(currVal) + 1, cups.size)
    }

    fun List<Int>.getOrRollOver(idx: Int) = this[Math.floorMod(idx, this.size)]

    // Rotates the last [x] characters in a [String] to the start, assuming 0 < x < [this.size()]
    fun List<Int>.rotate(x: Int) = subList(x, size).plus(subList(0, x))


}
