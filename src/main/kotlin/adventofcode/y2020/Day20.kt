package adventofcode.y2020

import adventofcode.Day
import kotlin.math.sqrt

typealias Tile = List<String>

/**
 * Advent of code day 20: https://adventofcode.com/2020/day/20
 *
 * Assumptions: There are a lot of assumptions made below about the input data provided. The most
 * important is that each edge in the set only matches one other edge. It wouldn't be that much
 * more difficult to write a program to solve that, but it would run for considerably longer
 * because...
 *
 *  1) we likely wouldn't be able to find any of the corners immediately
 *
 *  2) we would have to test most combinations of edge pairs when reconstructing the
 *  image, which would take way longer
 *
 * Other assumptions made: the image is a square. The only way this solution takes advantage of
 * that is by assuming sides are sqrt(n) instead of calculating all 2-number factorizations and
 * iterating over them
 */
class Day20(val input: List<String>) : Day {

    val idToTile = input.chunked(12).map { i ->
        i.filter { it.isNotBlank() }
    }.associate {
        val id = it[0].split(" ")[1].split(":")[0].toInt()
        id to it.drop(1)
    }

    /**
     * Assuming square image: I don't think the spec explicitly says this but it
     * definitely seems to be the case for the example and my input
     */
    val width = sqrt(idToTile.size.toFloat()).toInt()
    val height = sqrt(idToTile.size.toFloat()).toInt()

    val charsInMonster = 15
    val monsterLength = 20
    val seaMonster = listOf(
        "..................#.".toRegex(),
        "#....##....##....###".toRegex(),
        ".#..#..#..#..#..#...".toRegex(),
    )

    val corners = idToTile.mapNotNull { (id, tile) ->
        val variantBorders = tile.permute().map { it.borders() }
        if (idToTile.values.map { it.borders() }.count { otherBorder ->
                variantBorders.any { variantBorder ->
                    variantBorder.left == otherBorder.right || variantBorder.top == otherBorder.bottom || variantBorder.bottom == otherBorder.top || variantBorder.right == otherBorder.left
                }
            } == 3) id else null
    }

    override fun part1() = corners.fold(1L) { acc, i -> acc * i }
    override fun part2(): Any {
        val image = generateImage(corners, idToTile)

        // Remove edges from each tile:
        val edgesRemoved = image.map {
            it.map { tile ->
                tile.subList(1, tile.size - 1).map { s ->
                    s.substring(1, s.length - 1)
                }
            }
        }
        val board = mutableListOf<String>()
        edgesRemoved.flatten().chunked(width).map { tiles: List<Tile> ->
            for (y in tiles[0].indices) {
                board.add(tiles.joinToString("") { it[y] })
            }
        }

        val monstersFound = board.permute().map { tileVariant ->
            // Map<WHICH_MONSTER, Set<Pair<LINE_FOUND, MATCH_INDEX_START>>>
            val spottedInPermutation = mutableMapOf<Int, MutableSet<Pair<Int, Int>>>()
            tileVariant.mapIndexed { lineNumber, currLine ->
                lineNumber to currLine
            }.forEach { (lineNumber, currLine) ->
                seaMonster.forEachIndexed { whichMonster, regex ->
                    // Since Java regexes don't capture overlapping substrings, iterate over
                    // all possible substrings and locate exact matches
                    for (start in 0..(currLine.length - monsterLength)) {
                        regex.matchEntire(
                            currLine.substring(start, start + monsterLength)
                        )?.let {
                            spottedInPermutation.compute(whichMonster) { _, m ->
                                val prevMatches = m ?: mutableSetOf()
                                prevMatches.add(lineNumber to start)
                                prevMatches
                            }
                        }
                    }
                }
            }

            val monstersFound = spottedInPermutation[2]?.count { (p2Line, p2Index) ->
                val p0 = spottedInPermutation[0]?.any { (p0Line, p0Index) ->
                    p0Index == p2Index && p0Line == p2Line - 2
                } ?: false
                val p1 = spottedInPermutation[1]?.any { (p1Line, p1Index) ->
                    p1Index == p2Index && p1Line == p2Line - 1
                } ?: false
                p0 && p1
            } ?: 0
            monstersFound
        }.maxOrNull()!!

        // Assume no overlapping monsters
        return board.sumBy { t -> t.count { it == '#' } } - (charsInMonster * monstersFound)
    }

    private fun generateImage(corners: List<Int>, tileMap: Map<Int, Tile>): List<List<Tile>> {
        var image: MutableList<MutableList<List<String>>>
        tileMap.filter {
            it.key in corners
        }.flatMap { (id, tile) ->
            tile.permute().map { id to it }
        }.map { (id, tileVariant) ->
            image = MutableList(height) { MutableList(width) { emptyList() } }
            // Place variant of our chosen corner tile at (0, 0)
            image[0][0] = tileVariant
            val usedTiles = mutableSetOf(id)
            // Assume only (0, 0) is filled
            image.flatMapIndexed { y, row ->
                row.mapIndexedNotNull { x, _ ->
                    if (x == 0 && y == 0) null else Pair(x, y)
                }
            }.forEach { (x, y) ->
                val validMoves = tileMap.filter { placement ->
                    placement.key !in usedTiles
                }.mapNotNull { (id, toPlace) ->
                    val neighbors = image.getNeighbors(x, y)
                    val matchesNeeded = neighbors.toList().count { it != null }

                    // find any variants where [it] can be placed at [x], [y] of [image]
                    val match = toPlace.permute().firstOrNull { variant ->
                        countMatches(neighbors, variant.borders()) == matchesNeeded
                    } ?: return@mapNotNull null
                    id to match
                }
                // If no tile can be placed, then the whole image is impossible
                val chosenPlacement = validMoves.firstOrNull() ?: return@forEach
                usedTiles.add(chosenPlacement.first)
                image[y][x] = chosenPlacement.second
            }
            val filledCount = image.flatten().count { it.isNotEmpty() }
            if (image.flatten().size == filledCount) {
                return image
            }
        }
        throw IllegalStateException("No image was generated")
    }

    private fun countMatches(neighbors: Neighbors, variantBorder: Border): Int {
        val topMatch = neighbors.top?.borders()?.bottom == variantBorder.top
        val rightMatch = neighbors.right?.borders()?.left == variantBorder.right
        val bottomMatch = neighbors.bottom?.borders()?.top == variantBorder.bottom
        val leftMatch = neighbors.left?.borders()?.right == variantBorder.left

        return (if (topMatch) 1 else 0) + (if (rightMatch) 1 else 0) + (if (bottomMatch) 1 else 0) + (if (leftMatch) 1 else 0)
    }

    /** Computes all combinations of flips/rotations possible for this tile, with no duplicates */
    fun Tile.permute(): List<Tile> {
        val originals = mutableListOf(this, rotateCW())
        return originals.plus(originals.map { it.flipVertical() })
            .plus(originals.map { it.flipHorizontal() })
            .plus(originals.map { it.rotateCW().rotateCW() })
    }

    /** Rotates a [Tile] clockwise */
    fun Tile.rotateCW(): Tile {
        // not efficient but w/e
        val newTile = MutableList(size) { "" }
        for (row in indices.reversed()) {
            newTile[row] = map { it[row] }.reversed().joinToString("")
        }
        return newTile
    }

    /** Flips a [Tile] horizontally, such that each row is reversed */
    fun Tile.flipHorizontal() = map { it.reversed() }

    /** Flips a [Tile] vertically, such that the row order is reversed */
    fun Tile.flipVertical() = reversed()

    /** Given a [Tile], return a [Border] representing the edges of the tile */
    fun Tile.borders() = Border(
        top = first(),
        left = map { it.first() }.joinToString(""),
        bottom = last(),
        right = map { it.last() }.joinToString("")
    )

    /**
     * The four edges around a [Tile]. The edges are:
     *
     * [top]: the entire top string
     * [left]: a string composed of the first character of each row, from top to bottom
     * [right]: a string composed of the last character of each row, from top to bottom
     * [bottom]: the entire bottom string
     */
    data class Border(val top: String, val left: String, val right: String, val bottom: String)
    data class Neighbors(val top: Tile?, val left: Tile?, val right: Tile?, val bottom: Tile?)

    fun List<List<Tile>>.getNeighbors(x: Int, y: Int): Neighbors {
        val top = getOrNull(y - 1)?.getOrNull(x)
        val left = getOrNull(y)?.getOrNull(x - 1)
        val right = getOrNull(y)?.getOrNull(x + 1)
        val bottom = getOrNull(y + 1)?.getOrNull(x)
        return Neighbors(
            top = if (top?.isNotEmpty() == true) top else null,
            left = if (left?.isNotEmpty() == true) left else null,
            right = if (right?.isNotEmpty() == true) right else null,
            bottom = if (bottom?.isNotEmpty() == true) bottom else null,
        )
    }

    fun Neighbors.toList(): List<Tile?> = mutableListOf(top, right, bottom, left)
}