package adventofcode.y2020

import adventofcode.Day
import java.io.File

class Day04(val input: List<String>): Day {

    val passports = File("src/main/resources/day04.txt")
        .readText()
        .split( System.lineSeparator().repeat(2))
        .map {
            it.replace(System.lineSeparator(), " ").split(" ")
                .filter { it.isNotBlank() }
                .associate { bits ->
                    val split = bits.split(":")
                    split[0] to split[1]
                }
        }

    val keys = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

    override fun part1() = passports.count { map -> keys.all { map.containsKey(it) } }
    override fun part2() = passports.count { map ->
        map["byr"]?.toIntOrNull() in 1920..2002
            && map["iyr"]?.toIntOrNull() in 2010..2020
            && map["eyr"]?.toIntOrNull() in 2020..2030
            && (map["hgt"]?.takeIf { it.contains("cm") }?.take(3)?.toIntOrNull() in 150..193
            || map["hgt"]?.takeIf { it.contains("in") }?.take(2)?.toIntOrNull() in 59..76)
            && map["hcl"]?.matches("#[0-9a-f]{6}".toRegex()) == true
            && map["ecl"] in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
            && map["pid"]?.matches("\\d{9}".toRegex()) == true
    }
}

