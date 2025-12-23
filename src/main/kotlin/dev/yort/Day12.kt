package dev.yort

import java.io.File

object Day12 {
    fun part1() {
        val inputBlocks = File("inputs/Day12.txt").readText().split("\n\n")
        val shapes = inputBlocks.subList(0, 6).map { PresentShape.parseBlock(it) }
        val regions = inputBlocks[6].lines().filter { it.isNotBlank() }.map { Region.parseLine(it) }

        val plausible = regions.count { region -> isPlausible(shapes, region) }
        val guaranteed = regions.count { region -> isGuaranteed(shapes, region) }

        if (plausible == guaranteed) {
            println("Day 12, Part 1: There are [$plausible] valid regions")
        } else {
            TODO("actual np complete not solved yet")
        }

//        // Figure out all possible shapes
//        val allShapes = shapes.flatMap { shape ->
//            val shapes = mutableListOf<PresentShape>()
//            val r1 = PresentShape(shape.rotate90())
//            val r2 = PresentShape(r1.rotate90())
//            val r3 = PresentShape(r2.rotate90())
//            shapes.addAll(listOf(shape, r1, r2, r3))
//
//            val fh0 = PresentShape(shape.flipHorizontally())
//            val fh1 = PresentShape(fh0.flipHorizontally())
//            val fh2 = PresentShape(fh1.flipHorizontally())
//            val fh3 = PresentShape(fh2.flipHorizontally())
//            shapes.addAll(listOf(fh0, fh1, fh2, fh3))
//
//            val fv0 = PresentShape(shape.flipVertically())
//            val fv1 = PresentShape(fv0.flipVertically())
//            val fv2 = PresentShape(fv1.flipVertically())
//            val fv3 = PresentShape(fv2.flipVertically())
//            shapes.addAll(listOf(fv0, fv1, fv2, fv3))
//
//            shapes
//        }.distinct()
//
//        val x = 0
    }

    fun isPlausible(shapes: List<PresentShape>, region: Region): Boolean {
        val totalNeededSquares = region.presentRequirements.mapIndexed { index, req ->
            shapes[index].occupiedSquares * req
        }.fold(0L) { acc, value -> acc + value }

        return totalNeededSquares <= region.x * region.y
    }

    fun isGuaranteed(shapes: List<PresentShape>, region: Region): Boolean {
        val min = region.presentRequirements.sum() * 9 // <= region.x * region.y
        val maxNeededSquares = region.presentRequirements.mapIndexed { index, req ->
            9 * req
        }.fold(0L) { acc, value -> acc + value }

        return maxNeededSquares <= region.x * region.y
    }

    data class PresentShape(val shape: Map<Point, Char>) {
        val occupiedSquares = shape.count { it.value == '#' }

        fun rotate90(): Map<Point, Char> {
            val gmm = findMinMax(shape)
            val newShape = mutableMapOf<Point, Char>()
            (gmm.miny..gmm.maxy).forEach { y ->
                (gmm.minx..gmm.maxx).forEach { x ->
                    newShape[Point(gmm.maxx - y, x)] = shape[Point(x, y)]!!
                }
            }
            return newShape
        }

        fun flipVertically(): Map<Point, Char> {
            val gmm = findMinMax(shape)
            val newShape = mutableMapOf<Point, Char>()
            (gmm.miny..gmm.maxy).forEach { y ->
                (gmm.minx..gmm.maxx).forEach { x ->
                    newShape[Point(x, gmm.maxy - y)] = shape[Point(x, y)]!!
                }
            }
            return newShape
        }

        fun flipHorizontally(): Map<Point, Char> {
            val gmm = findMinMax(shape)
            val newShape = mutableMapOf<Point, Char>()
            (gmm.miny..gmm.maxy).forEach { y ->
                (gmm.minx..gmm.maxx).forEach { x ->
                    newShape[Point(gmm.maxx - x, y)] = shape[Point(x, y)]!!
                }
            }
            return newShape
        }

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
