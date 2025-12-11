package dev.yort

import java.io.File
import kotlin.math.pow
import kotlin.math.sqrt

data class Point(val x: Long, val y: Long, val z: Long = 0) {
    operator fun plus(other: Point): Point {
        return Point(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    fun adjacentNeighbors(): List<Point> {
        return ADJACENT.map { this + it }
    }

    companion object {
        val UP = Point(0, -1)
        val DOWN = Point(0, 1)
        val LEFT = Point(-1, 0)
        val RIGHT = Point(1, 0)
        val UP_LEFT = UP + LEFT
        val UP_RIGHT = UP + RIGHT
        val DOWN_LEFT = DOWN + LEFT
        val DOWN_RIGHT = DOWN + RIGHT

        val DIRECTLY_ADJACENT = listOf(UP, DOWN, LEFT, RIGHT)

        val DIAGONALLY_ADJACENT = listOf(
            UP_LEFT,
            UP_RIGHT,
            DOWN_LEFT,
            DOWN_RIGHT,
        )

        val ADJACENT = DIRECTLY_ADJACENT + DIAGONALLY_ADJACENT

        fun fromString(line: String): Point {
            val parts = line.split(",")
            if (parts.size != 2 && parts.size != 3) error("Unknown number of coordinates: [$parts]")

            return Point(
                x = parts[0].toLong(),
                y = parts[1].toLong(),
                z = if (parts.size == 3) {
                    parts[2].toLong()
                } else {
                    0L
                }
            )
        }
    }
}

fun distance(a: Point, b: Point): Double =
    sqrt(
        (b.x.toDouble() - a.x.toDouble()).pow(2.0) + (b.y.toDouble() - a.y.toDouble()).pow(2.0) + (b.z.toDouble() - a.z.toDouble()).pow(2.0)
    )


fun loadGridFromFile(filename: String): Map<Point, Char> {
    val lines = File(filename).readLines()
    return parseGrid(lines)
}

fun parseGrid(lines: List<String>): Map<Point, Char> {
    val input = lines.map { line ->
        line.trim().toCharArray().toList()
    }

    val grid = (0..input.lastIndex).flatMap { y ->
        (0..<input[y].size).map { x ->
            Point(x.toLong(), y.toLong()) to input[y][x]
        }
    }.toMap()
    return grid
}

fun findGridEntry(grid: Map<Point, Char>, value: Char): Map.Entry<Point, Char> =
    grid.filter { it.value == value }.entries.first()

fun printGrid(grid: Map<Point, Char>) {
    var minx = 0L
    var miny = 0L
    var maxx = 0L
    var maxy = 0L
    grid.forEach {
        if (it.key.x < minx) minx = it.key.x else if (it.key.x > maxx) maxx = it.key.x
        if (it.key.y < miny) miny = it.key.y else if (it.key.y > maxy) maxy = it.key.y
    }
    (miny..maxy).forEach { y ->
        (minx..maxx).forEach { x ->
            print(grid[Point(x, y)])
        }
        println()
    }
}
