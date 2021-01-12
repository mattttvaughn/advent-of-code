package adventofcode.y2020

import adventofcode.Day
import java.io.File

class Day07(val input: List<String>): Day {

    private val map = mutableMapOf<String, MutableList<Pair<String, Int>>>()

    private fun combined(): Set<String> {
        input.forEach { line ->
            val split = line.replace(".", "")
                .replace("bags", "bag")
                .split(" contain ")
            val key = split[0]
            val interiors = split[1].split(", ").mapNotNull { interiorBag ->
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

        return containedBags
    }

    private val bags = combined()
    override fun part1() = bags.size - 1
    override fun part2() = containsCount(map, "shiny gold bag") - 1

    /** Recursively find all bags contained within [bag] */
    private fun containsCount(map: Map<String, List<Pair<String, Int>>>, bag: String): Int {
        if (map[bag] == null) {
            return 1
        }
        return map[bag]!!.map { containsCount(map, it.first) * it.second }.sum() + 1
    }
}
