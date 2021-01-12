package adventofcode.y2020

import adventofcode.Day

/**
 * Advent of code day 24: https://adventofcode.com/2020/day/24
 */
class Day24(val input: List<String>) : Day {

    val dirs = setOf("e", "w", "ne", "nw", "se", "sw")
    val initialBlackTiles = input.map {
        var idx = 0
        var loc = Pair(0, 0)
        while (idx < it.length) {
            val dirLen = if (it[idx] in setOf('e', 'w')) 1 else 2
            loc = move(it.substring(idx, idx + dirLen), loc)
            idx += dirLen
        }
        loc
    }.groupBy {
        it
    }.mapNotNull {
        if (it.value.size % 2 == 1) it.value.first() else null
    }.toSet()

    override fun part1() = initialBlackTiles.size

    val days = 100
    override fun part2(): Int {
        var currTiles = initialBlackTiles.toSet()
        repeat(days) { day -> currTiles = applyRules(currTiles) }
        return currTiles.size
    }

    private fun applyRules(input: Set<Pair<Int, Int>>) = input.mapNotNull { tile ->
        // Black tiles with 1 or 2 adjacent black tiles are retained
        val neighborCount = dirs.count { dir -> input.contains(move(dir, tile)) }
        if (neighborCount in 1..2) tile else null
    }.toSet() + input.flatMap { tile ->
        // White tiles with exactly 2 adjacent black tiles are flipped
        val whiteNeighbors = dirs.mapNotNull { dir ->
            val neighbor = move(dir, tile)
            if (neighbor !in input) neighbor else null
        }
        whiteNeighbors.mapNotNull { neighbor ->
            val blackCount = dirs.count { dir -> input.contains(move(dir, neighbor)) }
            if (blackCount == 2) neighbor else null
        }
    }

    operator fun Pair<Int, Int>.plus(o: Pair<Int, Int>) = Pair(first + o.first, second + o.second)

    /**
     * Given a current tile location and a direction from [dirs], return the new location
     * of the tile as a [Pair<Int, Int>]
     */
    private fun move(
        dir: String, loc: Pair<Int, Int>
    ) = loc + when (dir) {
        "e" -> Pair(1, 0)
        "w" -> Pair(-1, 0)
        "se" -> if (loc.second % 2 == 0) Pair(1, -1) else Pair(0, -1)
        "ne" -> if (loc.second % 2 == 0) Pair(1, 1) else Pair(0, 1)
        "sw" -> if (loc.second % 2 == 0) Pair(0, -1) else Pair(-1, -1)
        "nw" -> if (loc.second % 2 == 0) Pair(0, 1) else Pair(-1, 1)
        else -> throw IllegalStateException("Invalid direction: $dir")
    }

}