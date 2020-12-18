package adventofcode.y2020

import adventofcode.Day

// Not particularly proud of solution to pt.1
//
// Couldn't think of a reasonable way to solve both problems using a
// reusable approach either
class Day18(val input: List<String>) : Day {

    override fun part1() = input.fold(0L) { acc, line ->
        acc + exec(line.replace(" ", ""))
    }

    override fun part2() = input.fold(0L) { acc, line ->
        acc + reduce(line.replace(" ", "")).toLong()
    }

    // Reduce recursively on parent parentheses groups
    fun reduceParens(eq: String): String {
        var s = eq
        val ranges = s.mapIndexedNotNull { idx, c ->
            if (c == '(') idx..(idx + matchParen(s.substring(idx))) else null
        }
        ranges.filterNot { outer ->
            ranges.any { inner ->
                outer.first > inner.first && outer.last < inner.last
            }
        }.map {
            Pair(s.substring(it.first, it.last), reduce(s.substring(it.first + 1, it.last - 1)))
        }.forEach {
            s = s.replace(it.first, it.second)
        }
        return s
    }

    val unneededParensRegex = "\\((\\d+)\\)".toRegex()
    fun reduceAdds(eq: String) = "(\\d+)\\+(\\d+)".toRegex().replace(eq) {
        (it.destructured.component1().toLong() + it.destructured.component2().toLong()).toString()
    }.replace(unneededParensRegex) { it.destructured.component1() }

    fun reduceMults(eq: String) = "(\\d+)\\*(\\d+)".toRegex().replace(eq) {
        (it.destructured.component1().toLong() * it.destructured.component2().toLong()).toString()
    }.replace(unneededParensRegex) { it.destructured.component1() }

    // recursively solve all bits within parens, then
    // reduce all [int] + [int] statements into [int]s, removing any ([int])s after,
    // then reduce all [int] * [int] statements into [int]s, removing at ([int])s after
    fun reduce(eq: String): String {
        // reduce elementary additions
        var curr = eq
        listOf('(', '+', '*').forEach { op ->
            while (curr.any { it == op }) {
                curr = when (op) {
                    '+' -> reduceAdds(curr)
                    '*' -> reduceMults(curr)
                    '(' -> reduceParens(curr)
                    else -> throw UnsupportedOperationException("Unknown operator: $op")
                }
            }
        }
        return curr
    }

    // Given a string starting with an open paren, return the index of matching paren
    fun matchParen(s: String): Int {
        var parenParity = -1
        var counter = 1
        while (parenParity < 0 && counter < s.length) {
            parenParity += when (s[counter++]) {
                '(' -> -1
                ')' -> 1
                else -> 0
            }
        }
        return counter
    }

    // Exec LTR, parsing anything within a parens recursively
    fun exec(eq: String): Long {
        val term1 = consumeNext(eq)
        var acc = term1.second
        var currEq = term1.first
        while (currEq.isNotEmpty()) {
            val operator = currEq[0]
            currEq = currEq.substring(1)
            val term2 = consumeNext(currEq)
            currEq = term2.first
            acc = when (operator) {
                '+' -> acc + term2.second
                '*' -> acc * term2.second
                else -> throw IllegalStateException("Unknown operator: ${operator}")
            }
        }
        return acc
    }

    private fun consumeNext(eq: String): Pair<String, Long> {
        return when {
            eq.startsWith("(") -> {
                val end = matchParen(eq)
                Pair(eq.substring(end), exec(eq.substring(1, end - 1)))
            }
            else -> {
                // consume a number
                val numMatch = Regex("\\d+").findAll(eq).first()
                Pair(eq.substring(numMatch.range.last + 1), numMatch.value.toLong())
            }
        }
    }
}
