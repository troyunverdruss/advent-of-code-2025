package dev.yort

import java.io.File

object Day12 {
    fun part1() {
        val inputBlocks = File("inputs/Day12.txt").readText().split("\n\n")
        val shapes = inputBlocks.subList(0, 6).map { PresentShape.parseBlock(it) }
        val regions = inputBlocks[6].lines().filter { it.isNotBlank() }.map { Region.parseLine(it) }

        val x = 0
    }

    data class PresentShape(val shape: Map<Point, Char>) {
        companion object {
            fun parseBlock(block: String): PresentShape {
                val lines = block.lines()
                return PresentShape(
                    parseGrid(lines.subList(1, lines.size))
                )
            }
        }
    }

    data class Region(val x: Long, val y: Long, val presentRequirements: List<Int>) {
        companion object {
            fun parseLine(line: String): Region {
                val x = line.split("x")[0].replace(":", "").toLong()
                val y = line.split("x")[1].split(":")[0].toLong()
                val presentRequirements = line.split(":")[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }
                return Region(x, y, presentRequirements)
            }
        }
    }
}
