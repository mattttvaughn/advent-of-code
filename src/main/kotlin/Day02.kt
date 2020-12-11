import java.io.File

fun main() {

    val input: List<String> = File("src/main/resources/day02.txt").readLines()

    // pt. 1
    val validLines = input.map { it.split(" ") }.count { segments: List<String> ->
        val range: List<Int> = segments[0].split("-").map { it.toInt() }
        val neededChar = segments[1].replace(":", "")
        val sortedString = segments[2].toList().sorted().joinToString("")
        val longEnough = sortedString.contains(neededChar.repeat(range[0]))
        val tooLong = sortedString.contains(neededChar.repeat(range[1] + 1))

        return@count longEnough && !tooLong
    }
    println("Pt.1: ${validLines}/${input.size} valid lines")

    // pt.2
    val pt2ValidLines = input.map { it.split(" ") }.count { segments: List<String> ->
        val range: List<Int> = segments[0].split("-").map { it.toInt() }
        val neededChar = segments[1].replace(":", "")[0]

        return@count (segments[2][range[0] - 1] == neededChar) xor (segments[2][range[1] - 1] == neededChar)
    }
    println("Pt.2: ${pt2ValidLines}/${input.size} valid lines")
}
