import java.io.File
import kotlin.math.max

fun main() {

    val input = File("src/main/resources/day10.txt")
            .readLines()
            .map { it.toInt() }
            .toMutableList()
            .apply { addAll(listOf(0, max()!! + 3)) }
            .sorted()

    var diff1 = 0
    var diff3 = 0
    input.reduce { prev, joltage ->
        when (joltage - prev) {
            1 -> diff1++
            3 -> diff3++
        }
        joltage
    }
    println("Pt.1: ${diff1 * diff3}")

    val combos = Array<Long>(input.max()!!.toInt()) { 0 }
    val set = input.toSet()
    for (idx in combos.indices) {
        if (idx in set) {
            val prev1 = combos.getOrElse(idx - 1) { 0L }
            val prev2 = combos.getOrElse(idx - 2) { 0L }
            val prev3 = combos.getOrElse(idx - 3) { 0L }
            combos[idx] = max(prev1 + prev2 + prev3, 1L)
        }
    }
    println("Pt.2: ${combos.toList().max()!!}")

}

