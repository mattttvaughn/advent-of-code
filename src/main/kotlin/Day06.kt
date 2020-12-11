import java.io.File

fun main() {

    val input: List<String> = File("src/main/resources/day06.txt").readText().split("\n\n")


    print("Pt.1: ")
    println(input.fold(0) { acc, group ->
        acc + group.replace("\n", "").toSortedSet().size
    })

    print("Pt.2: ")
    println(input.fold(0) { acc, group ->
        val freqMap = mutableMapOf<Char, Int>()
        val groupSize = group.count { it == '\n' } + 1
        group.replace("\n", "").forEach {
            freqMap[it] = freqMap.getOrElse(it) { 0 } + 1
        }
        acc + freqMap.count { it.value == groupSize }
    })
}
