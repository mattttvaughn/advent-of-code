package adventofcode.y2020

import adventofcode.Day
import java.io.File

class Day08(val input: List<String>): Day {

    override fun part1() = runOpCode(input).first

    override fun part2(): Long {
        for (lineNumber in input.indices) {
            val edited = ArrayList<String>(input)
            val line = edited[lineNumber]
            edited[lineNumber] = when {
                line.contains("nop") -> line.replace("nop", "jmp")
                line.contains("jmp") -> line.replace("jmp", "nop")
                else -> line
            }
            runOpCode(edited).takeIf { it.second }?.let { return it.first }
        }
        throw IllegalStateException("Execution did not produce results")
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
}

