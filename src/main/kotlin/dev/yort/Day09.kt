package dev.yort

import java.io.File
import kotlin.math.abs

object Day09 {
    fun part1() {
        val redTilePoints = File("inputs/Day09.txt").readLines().map { Point.fromString(it) }

        val areas = redTilePoints.subList(0, redTilePoints.lastIndex).flatMapIndexed { index, p1 ->
            redTilePoints.subList(index + 1, redTilePoints.size).map { p2 ->
                if (p1 == p2) {
                    1L
                } else {
                    val area = (abs(p2.x - p1.x) + 1) * (abs(p2.y - p1.y) + 1)
//                    println("Corners [$p1] [$p2] = [$area]")
                    area
                }
            }
        }

        val largestArea = areas.max()
        println("Day 9, Part 1: The largest square with red tiles at opposite corners is [$largestArea]")
    }
}
