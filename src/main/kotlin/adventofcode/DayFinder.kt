package adventofcode

import org.reflections.Reflections
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

object DayFinder {

    const val ALL_YEARS = -1
    val dayRegex = "Day\\d{1,2}".toRegex()

    fun findDays(year: Int = ALL_YEARS): List<Pair<Int, KFunction<Day>>> {
        if (year != ALL_YEARS) {
            return Reflections("adventofcode.y$year")
                .getSubTypesOf(Day::class.java)
                .filter { it.simpleName.matches(dayRegex) }
                .mapNotNull {
                    val constructor = it.kotlin.primaryConstructor
                    if (constructor != null) {
                        Pair(
                            it.simpleName.substring(it.simpleName.length - 2).toInt(),
                            constructor
                        )
                    } else {
                        null
                    }
                }.sortedBy { it.first }
        }
        return Reflections("adventofcode")
            .getSubTypesOf(Day::class.java)
            .filter { it.simpleName.matches(dayRegex) }
            .mapNotNull {
                val constructor = it.kotlin.primaryConstructor
                if (constructor != null) {
                    Pair(
                        it.simpleName.substring(it.simpleName.length - 2).toInt(),
                        constructor
                    )
                } else {
                    null
                }
            }.sortedBy { it.first }
    }
}