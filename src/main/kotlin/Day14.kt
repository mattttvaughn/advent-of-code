import java.io.File
import java.math.BigInteger

fun main() {
    val input = File("src/main/resources/day14.txt").readLines()
    println("Pt.1: ${parseProgram(input, 1)}")
    println("Pt.2: ${parseProgram(input, 2)}")
}

private fun parseProgram(input: List<String>, decoderVersion: Int): Long {
    val size = 36
    val mem = mutableMapOf<Long, Long>()
    var mask = "X".repeat(size)
    input.forEach {
        when (it.substring(0, 3)) {
            "mas" -> mask = it.split(" = ")[1]
            "mem" -> {
                val addr = it.substring(4, it.indexOf(']')).toLong()
                val writeValue = it.split(" = ")[1].toInt()
                if (decoderVersion == 1) {
                    mem[addr] = mask(writeValue, mask)
                } else {
                    calcAddrs(addr.toInt(), mask).forEach { calcedAddr ->
                        mem[calcedAddr] = writeValue.toLong()
                    }
                }
            }
            else -> throw IllegalStateException("Unknown command: $it")
        }
    }
    return mem.values.sum()
}

fun mask(value: Int, mask: String): Long {
    val valueString = Integer.toBinaryString(value).padStart(36, '0')
    val masked = valueString.zip(mask).map {
        when (it.second) {
            'X' -> it.first
            '1' -> it.second
            '0' -> it.second
            else -> throw IllegalStateException("Invalid input: value=$valueString, mask=$mask")
        }
    }.joinToString("")
    return BigInteger(masked, 2).longValueExact()
}

fun calcAddrs(addr: Int, mask: String): List<Long> {
    val addrString = Integer.toBinaryString(addr).padStart(36, '0')
    val masked = addrString.zip(mask).map {
        when (it.second) {
            'X' -> 'X'
            '1' -> '1'
            '0' -> it.first
            else -> throw IllegalStateException("Invalid character: only [X, 1, 0] allowed")
        }
    }.toMutableList()
    val addrs = mutableListOf(masked)
    fillXs(addrs)
    return addrs.map { it.joinToString("") }
            .map { BigInteger(it, 2).longValueExact() }
}

// Can't think of an immutable way to do this off the top of my head. May revisit
fun fillXs(addrs: MutableList<MutableList<Char>>) {
    // Check for X's to fill. Assume all addrs have same # of X's
    val nextX = addrs[0].indexOf('X')
    if (nextX == -1) {
        return
    }

    // double number of elements in arr. Edit the next available 'X', with half
    // getting '1' and half getting '0'. There's probably a better way to copy...
    val copy = addrs.map { addr -> addr.map { it }.toMutableList() }
    addrs.forEach { it[nextX] = '0' }
    copy.forEach { it[nextX] = '1' }
    addrs.addAll(copy)
    fillXs(addrs)
}
