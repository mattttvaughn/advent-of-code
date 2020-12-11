import java.io.File

val input: List<String> = File("src/main/resources/day07.txt").readLines()

fun main(args: Array<String>) {
    combined()
}

private fun combined() {

    val map = mutableMapOf<String, MutableList<Pair<String, Int>>>()

    input.forEach { line ->
        val splitted = line.replace(".", "")
                .replace("bags", "bag")
                .split(" contain ")
        val key = splitted[0]
        val interiors = splitted[1].split(", ").mapNotNull { interiorBag ->
            val bag = interiorBag.takeIf { !interiorBag.contains("no other bag") }
            if (bag == null) null else Pair(interiorBag.substring(2), interiorBag.substring(0, 1).toInt())
        }
        interiors.forEach { interior ->
            val currValues = map.getOrDefault(key, mutableListOf()).also { it.add(interior) }
            map[key] = currValues
        }
    }

    val containedBags = mutableSetOf("shiny gold bag")
    val unusedBags = map.keys.toMutableSet()
    var prevContainingSize = 0
    // keep finding contained bags, if no new contained bags we stop looping. O(n^2)
    while (containedBags.size != prevContainingSize) {
        prevContainingSize = containedBags.size
        // find all keys containing any of [contain], remove from keySet, add to contain
        val matched = map.filter { matchedBag ->
            matchedBag.value.map { it.first }.toSet().intersect(containedBags).isNotEmpty()
        }.map { it.key }
        containedBags.addAll(matched)
        unusedBags.removeAll(matched)
    }

    println("Pt.1: ${containedBags.size}")
    println("Pt.2: ${containsCount(map, "shiny gold bag") - 1}")

}

/** Recursively find all bags contained within [bag] */
fun containsCount(map: Map<String, List<Pair<String, Int>>>, bag: String): Int {
    if (map[bag] == null) {
        return 1
    }
    return map[bag]!!.map { containsCount(map, it.first) * it.second }.sum() + 1
}
