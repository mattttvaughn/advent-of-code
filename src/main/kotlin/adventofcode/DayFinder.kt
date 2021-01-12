package adventofcode

import org.reflections.Reflections
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

object DayFinder {

    const val ALL_YEARS = "-1"
    const val ALL_DAYS = "-1"
    val dayRegex = "Day[012]\\d".toRegex()

    fun findDays(
        day: String = ALL_DAYS,
        year: String = ALL_YEARS
    ): List<Pair<Int, KFunction<Day>>> {
        val packg = "adventofcode" + (if (year != ALL_YEARS) ".y$year" else "")
        val filter = if (day != ALL_DAYS) "Day$day".toRegex() else dayRegex
        return Reflections(packg).getSubTypesOf(Day::class.java)
            .filter { it.simpleName.matches(filter) }
            .mapNotNull {
                val constructor = it.kotlin.primaryConstructor
                if (constructor != null) {
                    Pair(it.simpleName.takeLast(2).toInt(), constructor)
                } else {
                    null
                }
            }
            .sortedBy { it.first }
    }
}