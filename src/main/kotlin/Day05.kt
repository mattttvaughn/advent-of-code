import java.io.File

// Avoided bitwise operations for the sake of time. Definitely was the way to go in retrospect
fun main() {

    val input: List<String> = File("src/main/resources/day05.txt").readLines()

    val rowValues = listOf(64, 32, 16, 8, 4, 2, 1)
    val colValues = listOf(4, 2, 1)

    val seats = input.map { pass ->
        val row = pass.substring(0, 7).foldIndexed(0) { index, acc, passChar ->
            return@foldIndexed acc + if (passChar == 'B') rowValues[index] else 0
        }
        val col = pass.substring(7).foldIndexed(0) { index, acc, passChar ->
            return@foldIndexed acc + if (passChar == 'R') colValues[index] else 0
        }
        val id = row * 8 + col
        id
    }.toSet()
    println("Pt.1: ${seats.max()}")
    ((seats.min() ?: 0)..(seats.max() ?: 0)).forEach { seatValue ->
        if (!seats.contains(seatValue)) {
            println("Pt.2: $seatValue")
        }
    }
}
