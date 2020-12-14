import java.io.File
import java.lang.Math.floorMod
import kotlin.math.pow
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

fun main() {
    val input = File("src/main/resources/day13_example.txt").readLines()
    println("Pt.1: ${part1(input)}")
    println("Pt.2: ${part2(input)}")
}

private fun part1(input: List<String>) = input[1].split(",")
        .filter { it != "x" }
        .map(String::toInt)
        .map { Pair(it - input[0].toInt() % it, it) }
        .minBy { it.first }!!
        .run { first * second }

private fun part2(input: List<String>): Long {
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
