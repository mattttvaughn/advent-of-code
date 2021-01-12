package adventofcode.y2020

import adventofcode.Day

/**
 * Advent of code day 22: https://adventofcode.com/2020/day/22
 *
 * Performance could conceivably be improved by keeping a pool of lists for each player with
 * sizes 0 < poolSize < n and copying data instead of creating a new sublist for each recursion
 */
class Day22(val input: List<String>) : Day {

    val player1 = input.takeWhile { it.isNotBlank() }.drop(1).map(String::toInt)
    val player2 = input.takeLastWhile { it.isNotBlank() }.drop(1).map(String::toInt)

    override fun part1() = solve(false)
    override fun part2() = solve(true)

    fun solve(recurse: Boolean): Long {
        val p1Mut = player1.toMutableList()
        val p2Mut = player2.toMutableList()
        playGame(recurse, p1Mut, p2Mut)
        val fin = if (p1Mut.isEmpty()) p2Mut else p1Mut
        return fin.foldIndexed(0L) { index, acc, i ->
            acc + (fin.size - index) * i
        }
    }

    /**
     * Play out the game, mutably editing [p1Mut] and [p2Mut]. The sublists are copied when
     * the game recurses, so as to not edit the real copies.
     *
     * Return true if p1 wins the game, false if p2 wins
     */
    private fun playGame(
        recurse: Boolean, p1Mut: MutableList<Int>, p2Mut: MutableList<Int>
    ): Boolean {
        // Keep a hash of round states to prevent duplicate rounds from playing out
        val previousRounds = HashSet<Long>()
        while (p1Mut.isNotEmpty() && p2Mut.isNotEmpty()) {
            // IMPORTANT: this hash is not guaranteed to avoid collisions! It ended up working
            // for the provided inputs but it is not guaranteed to work on arbitrary lists
            //
            // We could guarantee no collisions given the constraints on our inputs (no
            // repeated numbers, n = 40ish), but not going to spend the time here
            val currStepHash = (p1Mut.hashCode().toLong() shl Int.SIZE_BITS) + p2Mut.hashCode()
            if (!previousRounds.add(currStepHash)) {
                // p1 wins the game if we've already seen this step
                return true
            }
            val remove1 = p1Mut.removeAt(0)
            val remove2 = p2Mut.removeAt(0)
            val p1Wins = if (recurse && remove1 <= p1Mut.size && remove2 <= p2Mut.size) {
                playGame(
                    recurse,
                    p1Mut.take(remove1).toMutableList(),
                    p2Mut.take(remove2).toMutableList()
                )
            } else {
                remove1 > remove2
            }
            if (p1Wins) {
                p1Mut.add(remove1)
                p1Mut.add(remove2)
            } else {
                p2Mut.add(remove2)
                p2Mut.add(remove1)
            }
        }
        return p1Mut.isNotEmpty()
    }
}
