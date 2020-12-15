package adventofcode.y2020

import adventofcode.Day
import java.io.File

class Day04(val input: List<String>): Day {

    private val map = mutableMapOf<String, String>()
    val keys = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    override fun part1() = input.count { line ->
        if (line.isBlank()) {
            keys.all { map.containsKey(it) }.also { map.clear() }
        } else {
            line.split(" ").map { bits -> bits.split(":").also { map[it[0]] = it[1] } }
            false
        }
    }.also { map.clear() }

    override fun part2() = input.count { line ->
        if (line.isBlank()) {
            val valid = map["byr"]?.toIntOrNull() in 1920..2002
                    && map["iyr"]?.toIntOrNull() in 2010..2020
                    && map["eyr"]?.toIntOrNull() in 2020..2030
                    && (map["hgt"]?.takeIf { it.contains("cm") }?.substring(0, 3)?.toIntOrNull() in 150..193
                    || map["hgt"]?.takeIf { it.contains("in") }?.substring(0, 2)?.toIntOrNull() in 59..76)
                    && map["hcl"]?.matches(Regex("#[0-9a-f]{6}")) == true
                    && map["ecl"] in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
                    && map["pid"]?.matches(Regex("\\d{9}")) == true
            map.clear()
            valid
        } else {
            line.split(" ").map { bits -> bits.split(":").also { map[it[0]] = it[1] } }
            false
        }
    }
}

