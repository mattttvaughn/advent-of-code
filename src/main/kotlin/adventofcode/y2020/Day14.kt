package adventofcode.y2020

import adventofcode.Day
import java.math.BigInteger

class Day14(val input: List<String>) : Day {

    override fun part1() = parseProgram(1)
    override fun part2() = parseProgram(2)

    private fun parseProgram(decoderVersion: Int): Long {
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

    private fun mask(value: Int, mask: String): Long {
        val masked = Integer.toBinaryString(value).padStart(36, '0')
            .zip(mask).map {
                when (it.second) {
                    'X' -> it.first
                    '1' -> it.second
                    '0' -> it.second
                    else -> throw IllegalStateException("Invalid input: value=$value, mask=$mask")
                }
            }.joinToString("")
        return BigInteger(masked, 2).longValueExact()
    }

    private fun calcAddrs(addr: Int, mask: String): List<Long> {
        val masked = Integer.toBinaryString(addr).padStart(36, '0')
            .zip(mask).map {
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
    private fun fillXs(addrs: MutableList<MutableList<Char>>) {
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
}
