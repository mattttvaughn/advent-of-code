package adventofcode.y2020

import adventofcode.Day

/**
 * Advent of code day 21: https://adventofcode.com/2020/day/21
 */
class Day21(val input: List<String>) : Day {

    val ingredientSets = mutableSetOf<Set<String>>()
    val allergensToRecipes: Map<String, List<Set<String>>> = input.flatMap {
        val split = it.replace("[,)]".toRegex(), "")
            .replace("contains ", "")
            .split(" (")
        val ingredients = split[0].split(" ").toSet()
        ingredientSets.add(ingredients)
        split[1].split(" ").map { it to ingredients }
    }.groupBy { it.first }.mapValues { it.value.map { it.second } }

    val safeIngredients = allergensToRecipes.flatMap {
        it.value.flatten()
    }.toSet() - (allergensToRecipes.flatMap {
        it.value.reduce { acc, set -> acc.intersect(set) }
    }.toSet())

    // for each allergen, create a set of all ingredient sets containing that allergen
    override fun part1() = ingredientSets.sumBy { it.count { it in safeIngredients } }

    override fun part2(): Any {
        val remainingMap = mutableMapOf<String, Set<String>>()
        allergensToRecipes.forEach {
            val includedInAll = it.value.reduce { acc, set -> acc.intersect(set) }
            remainingMap[it.key] = includedInAll.toSet() - safeIngredients
        }
        while (remainingMap.any { it.value.size != 1 }) {
            val removedIngrs = remainingMap.filter { it.value.size == 1 }.flatMap { it.value }
            remainingMap.replaceAll { allergen: String, remainingIngrs: Set<String> ->
                if (remainingIngrs.size != 1) {
                    remainingIngrs - removedIngrs
                } else {
                    remainingIngrs
                }
            }
        }
        return remainingMap.toList().sortedBy {
            it.first
        }.map { it.second.first() }.joinToString(",")
    }
}
