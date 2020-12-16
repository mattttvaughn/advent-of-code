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
        .map { ticket -> ticket.split(",").map { it.toInt() } }

    // Find all invalid values in nearby tickets, sum them
    override fun part1() = nearbyTickets.flatMap { ticket ->
        ticket.map { if (it !in validValues) it else 0 }
    }.sum()

    override fun part2(): Any {
        // Initialize any tag possible at any index/col
        val possibleFields = myTicket.map { rules.map { it.first }.toMutableSet() }

        // Ignore totally invalid tickets
        nearbyTickets.filter { someTicket ->
            someTicket.all { it in validValues }
        }.forEach { nearbyTicket ->
            // Remove tag for indices where rule is broken
            nearbyTicket.forEachIndexed { fieldIdx, fieldValue ->
                rules.forEach { rule ->
                    if (rule.second.all { fieldValue !in it }) {
                        possibleFields[fieldIdx].remove(rule.first)
                    }
                }
            }
        }

        // Could potentially exit early but no need tbh
        while (possibleFields.any { it.size != 1 }) {
            possibleFields.filter { it.size == 1 }.map {
                it.first()
            }.forEach {
                possibleFields.forEach { field ->
                    if (field.size != 1) {
                        field.remove(it)
                    }
                }
            }
        }

        val departureIndices = possibleFields.mapIndexedNotNull { idx, fields ->
            if (fields.first().startsWith("departure")) idx else null
        }
        return myTicket.filterIndexed { idx, _ ->
            idx in departureIndices
        }.fold(1L) { acc, i -> acc * i }
    }
}

