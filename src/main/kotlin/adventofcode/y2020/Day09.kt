package adventofcode.y2020

import adventofcode.Day

class Day09(val input: List<String>) : Day {

    private val nums = input.map { it.toLong() }

    private val preambleSize = 25

    private val p1 = nums.indices.drop(preambleSize).mapNotNull { idx ->
        val prev = nums.subList(idx - preambleSize, idx)
        val sums = prev.flatMap { outer -> prev.map { it + outer } }
        if (!sums.contains(nums[idx])) nums[idx] else null
    }.first()

    override fun part1() = p1

    override fun part2(): Any {
        for (start in nums.indices) {
            var sum = nums[start]
            var end = start
            while (sum < p1 && end < nums.size) {
                end++
                sum += nums[end]
            }
            if (sum == p1 && start != end) {
                val slice = nums.subList(start, end + 1)
                return (slice.maxOrNull() ?: 0) + (slice.minOrNull() ?: 0)
            }
        }
        throw IllegalStateException("Execution did not produce results")
    }
}

