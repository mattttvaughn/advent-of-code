package adventofcode.y2020

import adventofcode.Day

typealias Grid<T> = MutableList<MutableList<MutableList<MutableList<T>>>>

class Day17(val input: List<String>) : Day {

    val gridSize = 23
    val directions4D = listOf(1, 0, -1).flatMap { x ->
        listOf(1, 0, -1).flatMap { y ->
            listOf(1, 0, -1).flatMap { z ->
                listOf(1, 0, -1).mapNotNull { q -> if (x or y or z or q == 0) null else listOf(x, y, z, q) }
            }
        }
    }
    val directions3D = directions4D.filter { it[3] == 0 }

    override fun part1() = cycleGrid(false)
    override fun part2() = cycleGrid(true)

    private fun cycleGrid(use4thDimen: Boolean): Int {
        var grid =
            MutableList(gridSize) { MutableList(gridSize) { MutableList(gridSize) { MutableList(if (use4thDimen) gridSize else 1) { 0 } } } }

        val offset = gridSize / 2 - 1
        (input.indices).forEach { yIdx ->
            (input[0].indices).forEach { xIdx ->
                val pointVal = if (input[yIdx][xIdx] == '#') 1 else 0
                grid[xIdx + offset][yIdx + offset][offset][if (use4thDimen) offset else 0] = pointVal
            }
        }

        repeat(6) { grid = step(grid, use4thDimen) }

        return grid.sumBy { it.sumBy { it.sumBy { it.sumBy { it } } } }
    }

    fun step(grid: Grid<Int>, use3rdDimen: Boolean) = MutableList(gridSize) { x ->
        MutableList(gridSize) { y ->
            MutableList(gridSize) { z ->
                MutableList(if (use3rdDimen) gridSize else 1) { q ->
                    val activeNeighbors = (if (use3rdDimen) directions4D else directions3D).count {
                        grid.getOrNull(it[0] + x)?.getOrNull(it[1] + y)?.getOrNull(it[2] + z)
                            ?.getOrNull(if (use3rdDimen) it[3] + q else 0) == 1
                    }
                    when {
                        !use3rdDimen && q != 0 -> 0
                        grid[x][y][z][q] == 1 && activeNeighbors in 2..3 -> 1
                        grid[x][y][z][q] == 0 && activeNeighbors == 3 -> 1
                        else -> 0
                    }
                }
            }
        }
    }
}

