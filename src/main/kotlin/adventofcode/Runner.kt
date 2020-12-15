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
    // Replace "null" with a file in the resources dir to use it as an input
    val inputOverride: String? = null

    val yearRegex = "\\d{4}".toRegex()
    val yearMatch = args.mapNotNull {
        yearRegex.find(it)?.groupValues?.firstOrNull()
    }.firstOrNull()

    val testFileRegex = ".*.txt".toRegex()
    val testFileMatch = args.mapNotNull {
        testFileRegex.find(it)?.groupValues?.firstOrNull()
    }.firstOrNull()

    val constructors: List<Pair<Int, KFunction<Day>>> = when {
        yearMatch != null -> {
            // Run all programs in a given year
            val year = yearMatch.toInt()
            Reflect.findDays(year = year)
        }
        args.any { it.contains("all") } -> {
            // Run program for all days
            Reflect.findDays()
        }
        else -> {
            // Run the most recent program
            listOf(Reflect.findDays().last())
        }
    }
    printGridRow(
        "Day" to 3,
        "Pt.1" to 20,
        "Time" to 9,
        "Pt.2" to 20,
        "Time" to 9,
        "Init (ms)" to 12
    )
    constructors.map { pair ->
        val constructor = pair.second
        val numStr = pair.first.toString().padStart(2, '0')

        val input = File(
            "src/main/resources/${
                when {
                    inputOverride != null -> inputOverride
                    testFileMatch != null -> testFileMatch
                    else -> "day$numStr.txt"
                }
            }"
        ).readLines()

        lateinit var day: Day
        val initTime = measureTimeMillis {
            day = constructor.call(input)
        }

        lateinit var pt1: Any
        val pt1Time = measureTimeMillis {
            pt1 = day.part1()
        }

        lateinit var pt2: Any
        val pt2Time = measureTimeMillis {
            pt2 = day.part2()
        }

        printGridRow(
            numStr to 3,
            pt1 to 20,
            (pt1Time.toString() + "ms") to 9,
            pt2 to 20,
            (pt2Time.toString() + "ms") to 9,
            (initTime.toString() + "ms") to 12
        )
    }

}

// Prints a row in a grid given a [map] of elements and their corresponding column size
fun printGridRow(vararg values: Pair<Any?, Int>) {
    values.forEach {
        print(it.first.toString().padStart(it.second, ' '))
    }
    println()
}


