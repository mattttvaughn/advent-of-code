import java.io.File

val inputFile: List<String> = File("src/main/resources/day08.txt").readLines()

fun main(args: Array<String>) {
    // pt.1
    println("Pt.1: ${runOpCode(inputFile).first}")

    // pt.2
    for (lineNumber in inputFile.indices) {
        val edited = mutableListOf<String>().apply { addAll(inputFile) }
        val line = edited[lineNumber]
        edited[lineNumber] = when {
            line.contains("nop") -> line.replace("nop", "jmp")
            line.contains("jmp") -> line.replace("jmp", "nop")
            else -> line
        }
        runOpCode(edited).takeIf { it.second }?.let { println("Pt.2: ${it.first}") }
    }
}

private fun runOpCode(inputCode: List<String>): Pair<Long, Boolean> {
    val executed = mutableSetOf<Int>()
    var acc = 0L
    var pc = 0

    while (pc < inputCode.size) {
        if (executed.contains(pc)) {
            return Pair(acc, false)
        }
        executed.add(pc)
        val modifier = inputCode[pc].substring(4).strip().toInt()
        when (inputCode[pc].substring(0, 3)) {
            "jmp" -> pc += modifier - 1
            "acc" -> acc += modifier
        }
        pc++
    }
    return Pair(acc, true)
}
