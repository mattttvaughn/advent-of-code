import java.io.File

// tbh could use better line pre-processing but not bad
fun main() {

    val input = File("src/main/resources/day04.txt").readLines()

    // pt.1
    val map = mutableMapOf<String, String>()
    print("Pt.1:")
    val keys = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    println(input.count { line ->
        if (line.isBlank()) {
            keys.all { map.containsKey(it) }.also { map.clear() }
        } else {
            line.split(" ").map { bits -> bits.split(":").also { map[it[0]] = it[1] } }
            false
        }
    })

    // pt.2
    map.clear()
    print("Pt.2:")
    println(input.count { line ->
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
        })
    }
