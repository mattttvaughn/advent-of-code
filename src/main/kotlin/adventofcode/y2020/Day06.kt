package adventofcode.y2020

import adventofcode.Day
import java.io.File

class Day06(val input: List<String>) : Day {

    val groups = File("src/main/resources/day06.txt").readText()
        .split(System.lineSeparator().repeat(2))
        .map { group ->
            group.lines().size to group.replace(
                System.lineSeparator(), ""
            )
        }

    override fun part1() = groups.sumBy { it.second.toSet().size }

    override fun part2() = groups.sumBy { (groupSize, answers) ->
        answers.groupBy { it }.count { it.value.size == groupSize }
    }
}
