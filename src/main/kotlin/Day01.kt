import java.io.File

fun main(args: Array<String>) {

    val input = File("src/main/resources/day01.txt").readLines().map { it.toInt() }.toHashSet()

    // pt.1
    input.forEach {
        if (input.contains(2020 - it)) {
            println("Pt.1: $it * ${2020 - it} = ${it * (2020 - it)}")
        }
    }

    // pt.2
    input.forEach { first: Int ->
        input.forEach { second: Int ->
            val sum = first + second
            if (input.contains(2020 - sum)) {
                println("Pt.2: $first * $second * ${2020 - sum} = ${first * second * (2020 - sum)}")
            }
        }
    }

}

