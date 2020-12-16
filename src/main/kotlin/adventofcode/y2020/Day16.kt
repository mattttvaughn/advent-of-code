package adventofcode.y2020

import adventofcode.Day

class Day16(val input: List<String>) : Day {

    private val rangeRegex = "\\d+-\\d+".toRegex()

    private val myTicket = input[input.indexOf("your ticket:") + 1]
        .split(",")
        .map { it.toInt() }

    private val rules = input.take(input.indexOfFirst { it.isBlank() })
        .map { line ->
            val split = line.split(": ")
            val fieldName = split[0]
            val ranges = split[1].split(" or ").map {
                val innerSplit = it.split("-")
                innerSplit[0].toInt()..innerSplit[1].toInt()
            }
            fieldName to ranges
        }

    private val validValues = input.take(input.indexOfFirst { it.isBlank() })
        .flatMap { line ->
            rangeRegex.findAll(line).flatMap { rangeResult ->
                rangeResult.groupValues.map {
                    val split = it.split("-")
                    split[0].toInt()..split[1].toInt()
                }
            }.toList()
        }.flatMap { validRange ->
            validRange.map { it }
        }.toHashSet()

    private val nearbyTickets = input.drop(input.indexOf("nearby tickets:") + 1)
        .map { ticket ->
            ticket.split(",").map { it.toInt() }
        }

    // Find all invalid values in nearby tickets, sum them
    override fun part1() = nearbyTickets.sumBy { ticket ->
        ticket.sumBy {
            if (it !in validValues) it else 0
        }
    }

    override fun part2(): Any {
        // Map from each index in field to a set of all possible tags
        val possibleFields = mutableMapOf<Int, MutableSet<String>>()
        val allFields = rules.map { it.first }

        // Initialize any tag possible at any index/col
        rules.forEachIndexed { idx: Int, _: Any ->
            possibleFields[idx] = HashSet(allFields)
        }

        // Ignore totally invalid tickets
        nearbyTickets.filter { someTicket ->
            someTicket.all { it in validValues }
        }.forEach { nearbyTicket ->
            rules.forEach { rule: Pair<String, List<IntRange>> ->
                // Remove tags for an index when the tag's rule is broken
                nearbyTicket.forEachIndexed { fieldIdx: Int, fieldValue: Int ->
                    if (rule.second.all { fieldValue !in it }) {
                        possibleFields[fieldIdx]?.remove(rule.first)
                    }
                }
            }
        }

        // Could potentially exit early but no need tbh
        for (x in 0 until possibleFields.size) {
            possibleFields.values.filter { it.size == 1 }.map {
                it.first()
            }.forEach {
                possibleFields.forEach { field ->
                    if (field.value.size != 1) {
                        field.value.remove(it)
                    }
                }
            }
        }

        val departureIndices = possibleFields.mapNotNull {
            if (it.value.first().startsWith("departure")) it.key else null
        }
        return myTicket.filterIndexed { idx: Int, _: Int ->
            idx in departureIndices
        }.fold(1L) { acc: Long, i: Int -> acc * i }
    }
}

