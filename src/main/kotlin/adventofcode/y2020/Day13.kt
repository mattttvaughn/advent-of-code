package adventofcode.y2020

import adventofcode.Day
import java.io.File
import java.lang.Math.floorMod

class Day13(val input : List<String>) : Day {

    override fun part1() = input[1].split(",")
        .filter { it != "x" }
        .map(String::toInt)
        .map { Pair(it - input[0].toInt() % it, it) }
        .minByOrNull { it.first }!!
        .run { first * second }

    override fun part2(): Long {
        val departures = input[1].split(",")
            .mapIndexedNotNull { idx: Int, str: String ->
                val mod = str.toLongOrNull() ?: return@mapIndexedNotNull null
                Pair(floorMod(mod - idx, mod), mod)
            }

        // https://en.wikipedia.org/wiki/Chinese_remainder_theorem
        val M = departures.fold(1L) { acc, p -> acc * p.second }
        return floorMod(departures.fold(0L) { acc, p ->
            val b = M / p.second
            // Could alternatively use Euclid or Euler (if mod prime) algos for inverse
            val bInv = b.toBigInteger().modInverse(p.second.toBigInteger()).longValueExact()
            acc + p.first * b * bInv
        }, M)
    }
}