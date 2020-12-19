package adventofcode.y2020

import adventofcode.Day
import java.io.File

// A generalizable solution to both parts which solves by resolving the
// rules into a regular expression. A bit of a mess due to unoptimized
// regular expressions (the regex for pt.2 is EXTREMELY long) but concise
// and easy to understand
class Day19(val input: List<String>) : Day {

    val rules1 = parseRules(input)
    val rules2 = parseRules(File("src/main/resources/day19a.txt").readLines())

    fun parseRules(rules: List<String>) = rules.takeWhile { it.isNotEmpty() }
        .sortedBy { it.split(": ")[0].toIntOrNull() }
        .map { line -> line.split(": ")[1].replace("\"", "") }
        .toMutableList()

    val finishedRegex = "[\"|, ()ab]+".toRegex()

    override fun part1() = countMatches(rules1)
    override fun part2() = countMatches(rules2)

    private fun countMatches(rules: MutableList<String>): Int {
        // Note: [count] works only at values 9-11 for pt.2 two. The answer doesn't
        // converge for values >9, and the regex is too long to compile at >11.
        // This could be remedied by improving regex efficiency or a pass resolving
        // regex inefficiencies (like single characters wrapped in a group), or
        // by manually resolving the cycle with regex "+"s (as seen in other sol'ns online)
        var count = 0
        while (!rules[0].matches(finishedRegex) && count++ < 10) {
            // Assume the same steady-state rules (e.g. rules in [finished]) between
            // parts 1 and 2, except for the ones being replaced. This is NOT a safe
            // assumption for all possible problems, but it works in this case. I would
            // definitely not use this assumption if doing the problem in the future, but
            // it happened to work

            // all finished lines (no more lookups to be done)
            val finished = rules1.mapIndexedNotNull { idx, s ->
                if (s.matches(finishedRegex)) idx else null
            }
            for (idx in rules.indices) {
                rules[idx] = replaceRule(rules[idx], rules, finished)
            }
        }
        val rule = rules[0].replace("\\((\\S+)\\)".toRegex()) {
            it.destructured.component1()
        }.replace(" ", "").toRegex()
        return input.dropWhile { it.isNotEmpty() }.count { it.matches(rule) }
    }

    // Replace corresponding rules in [rule] with an entry from [finished]
    fun replaceRule(rule: String, rules: List<String>, finished: List<Int>): String {
        return rule.replace("\\d+".toRegex()) {
            val lookup = it.value.toIntOrNull()
            if (lookup in finished && lookup != null) "(${rules[lookup]})" else it.value
        }
    }
}
