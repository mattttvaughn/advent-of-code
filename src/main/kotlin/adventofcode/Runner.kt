package adventofcode

import java.io.File
import kotlin.reflect.KFunction
import kotlin.system.measureTimeMillis

/**
 * Runs problems implementing [Day]
 *
 * Defaults to running the most recent day
 * Include string "all" in the args to run all tests
 * Include a file pattern (".*.txt") to run against that file as input
 */
fun main(args: Array<String>) {
    val yearRegex = "\\d{4}".toRegex()
    val yearMatch = args.mapNotNull {
        yearRegex.find(it)?.groupValues?.firstOrNull()
    }.firstOrNull()

    val dayRegex = "\\d{4}-\\d{2}".toRegex()
    val dayMatch = args.mapNotNull {
        dayRegex.find(it)?.groupValues?.firstOrNull()
    }.firstOrNull()

    val constructors: List<Pair<Int, KFunction<Day>>> = when {
        dayMatch != null -> {
            // Run all programs in a given year
            val year = dayMatch.split("-")[0]
            val day = dayMatch.split("-")[1]
            DayFinder.findDays(day = day, year = year)
        }
        yearMatch != null -> {
            // Run all programs in a given year
            val year = yearMatch
            DayFinder.findDays(year = year)
        }
        args.any { it.contains("all") } -> {
            // Run program for all days
            DayFinder.findDays()
        }
        else -> {
            // Run the most recent program
            listOf(DayFinder.findDays().last())
        }
    }
    printGridRow(
        "Day" to 3,
        "Pt.1" to 20,
        "Time" to 9,
        "Pt.2" to 50,
        "Time" to 9,
        "Init (ms)" to 12
    )
    constructors.map { pair ->
        val constructor = pair.second
        val numStr = pair.first.toString().padStart(2, '0')

        val input = File( "src/main/resources/day$numStr.txt" ).readLines()

        lateinit var day: Day
        val initTime = measureTimeMillis { day = constructor.call(input) }

        lateinit var pt1: Any
        val pt1Time = measureTimeMillis { pt1 = day.part1() }

        lateinit var pt2: Any
        val pt2Time = measureTimeMillis { pt2 = day.part2() }

        printGridRow(
            numStr to 3,
            pt1 to 20,
            (pt1Time.toString() + "ms") to 9,
            pt2 to 50,
            (pt2Time.toString() + "ms") to 9,
            (initTime.toString() + "ms") to 12
        )
    }

}

// Prints a row in a grid given a [map] of elements and their corresponding column size
fun printGridRow(vararg values: Pair<Any?, Int>) {
    println(values.joinToString("") { (s, len) ->
        s.toString().padStart(len, ' ')
    })
}


