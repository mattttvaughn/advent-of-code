import java.io.File
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    val input = File("src/main/resources/day12.txt").readLines()
    println("Pt.1: ${part1(input)}")
    println("Pt.2: ${part2(input)}")
}

private fun part1(input: List<String>) : Int {
    val ship = mutableListOf(0, 0)
    var bearing = 90
    for (line in input) {
        val cmd = line.substring(0, 1)
        val value = line.substring(1).toInt()
        when (cmd) {
            "L" -> bearing = Math.floorMod(bearing - value, 360)
            "R" -> bearing = Math.floorMod(bearing + value, 360)
            "N" -> ship[1] += value
            "S" -> ship[1] -= value
            "E" -> ship[0] += value
            "W" -> ship[0] -= value
            "F" -> {
                when (bearing) {
                    0, 360 -> ship[1] += value
                    90 -> ship[0] += value
                    180 -> ship[1] -= value
                    270 -> ship[0] -= value
                }
            }
            else -> throw UnsupportedOperationException("unknown command")
        }
    }
    return abs(ship[0]) + abs(ship[1])
}

private fun part2(input: List<String>) : Int {

    var wayPoint = mutableListOf(10, 1)
    val ship = mutableListOf(0, 0)

    for (line in input) {
        val cmd = line.substring(0, 1)
        val value = line.substring(1).toInt()
        when (cmd) {
            "L" -> {
                for (x in 0 until (value / 90)) {
                    wayPoint = rotateCCW(wayPoint)
                }
            }
            "R" -> {
                for (x in 0 until (value / 90)) {
                    wayPoint = rotateCW(wayPoint)
                }
            }
            "N" -> wayPoint[1] += value
            "S" -> wayPoint[1] -= value
            "E" -> wayPoint[0] += value
            "W" -> wayPoint[0] -= value
            "F" -> {
                ship[0] += wayPoint[0] * value
                ship[1] += wayPoint[1] * value
            }
            else -> throw UnsupportedOperationException("Unknown Command")
        }
    }
    return abs(ship[0]) + abs(ship[1])
}

// Note: Should use trig to calc, but not going to
fun rotateCW(point: List<Int>): MutableList<Int> {
    val flipped = mutableListOf(point[1], point[0])
    when (sign(point[0].toDouble())..sign(point[1].toDouble())) {
        -1.0..1.0, -1.0..-1.0, 1.0..1.0, 1.0..0.0, -1.0..0.0 -> flipped[1] = -1 * flipped[1]
        1.0..-1.0 -> flipped[0] = -1 * flipped[0]
        0.0..0.0, 0.0..-1.0, 0.0..1.0 -> {}
        else -> throw IllegalStateException("Impossible state: $point")
    }
    return flipped
}

fun rotateCCW(point: List<Int>): MutableList<Int> {
    return rotateCW(rotateCW(rotateCW(point)))
}


