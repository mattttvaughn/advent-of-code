import java.io.File

fun main() {

    val input = File("src/main/resources/day09.txt").readLines().map { it.toLong() }

    val preambleSize = 25

    val pt1Soln = input.indices.drop(preambleSize).mapNotNull { idx ->
        val prev = input.subList(idx - preambleSize, idx)
        val sums = prev.flatMap { outer -> prev.map { it + outer } }
        if (!sums.contains(input[idx])) input[idx] else null
    }.first()

    println("Pt.1: $pt1Soln")

    for (start in input.indices) {
        var sum = input[start]
        var end = start
        while (sum < pt1Soln && end < input.size) {
            end++
            sum += input[end]
        }
        if (sum == pt1Soln && start != end) {
            val slice = input.subList(start, end + 1)
            println("Pt.2: ${(slice.max() ?: 0) + (slice.min() ?: 0)}")
        }
    }
}

