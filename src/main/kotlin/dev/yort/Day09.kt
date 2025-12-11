package dev.yort

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day09 {
    fun part1() {
        val redTilePoints = File("inputs/Day09.txt").readLines().map { Point.fromString(it) }

        val areas = redTilePoints.subList(0, redTilePoints.lastIndex).flatMapIndexed { index, p1 ->
            redTilePoints.subList(index + 1, redTilePoints.size).map { p2 ->
                val area = Rectangle(p1, p2).area()
//              println("Corners [$p1] [$p2] = [$area]")
                area

            }
        }

        val largestArea = areas.max()
        println("Day 9, Part 1: The largest square with red tiles at opposite corners is [$largestArea]")
    }

    fun part2() {
        val inputLines = File("inputs/Day09.txt").readLines()

        val corners = computeRedTilePoints(inputLines)
        val edges = (corners + listOf(corners.first())).windowed(2).map { Edge(Point(it[0].x, it[0].y), Point(it[1].x, it[1].y)) }

        val allRectangles = corners.subList(0, corners.lastIndex).flatMapIndexed { index, p1 ->
            corners.subList(index + 1, corners.size).map { p2 ->
                Rectangle(p1, p2)
            }
        }

        val rectanglesWithoutIntersections = allRectangles.filterNot { rect ->
            var intersects = false
            for (edge in edges) {
                val edgeCrossesHorizontally = edge.maxx() > rect.minx() && edge.minx() < rect.maxx()
                val edgeCrossesVertically = edge.maxy() > rect.miny() && edge.miny() < rect.maxy()
                if (edgeCrossesVertically && edgeCrossesHorizontally) {
                    intersects = true
                }
            }

            intersects
        }
        val largestArea = rectanglesWithoutIntersections.maxOf { it.area() }

//
//
//        val redTilePoints = computeRedTilePoints(inputLines)
//
//
//        // Find the leftmost, topmost point
//        val miny = redTilePoints.minOf { it.y }
//        val startingPoint = redTilePoints.filter { it.y == miny }.minBy { it.x }
//
//        val startingPointIndex = redTilePoints.indexOf(startingPoint)
//        val reorderedRedTilePoints = redTilePoints.subList(startingPointIndex, redTilePoints.size) + redTilePoints.subList(0, startingPointIndex)
//        val redTilePointsSet = redTilePoints.toSet()
//
//        val outsidePoints = findOutsidePoints(reorderedRedTilePoints, redTilePointsSet)
//
////        val grid = mutableMapOf<Point, Char>()
////        (0L..8).forEach { y ->
////            (0L..13).forEach { x ->
////                grid[Point(x, y)] = '.'
////            }
////        }
////        redTilePoints.forEach { grid[it] = '#' }
////        outsidePoints.forEach { grid[it] = 'o' }
////        printGrid(grid)
//
//        val rectangles = computeRectangles(redTilePoints)
//
//        val filteredRectangles = filterRectangles(rectangles, outsidePoints)
//
//        val lines = computeLineSegments(redTilePoints)
//        val linesAsRects = lines.map { Rectangle(it.p1, it.p2) }
//
//        val candidates = rectangles.toMutableSet()
//        rectangles.subList(0, rectangles.lastIndex).forEach { rect ->
//            linesAsRects.forEach { edge ->
//                if (edge.p1 == rect.p1 || edge.p1 == rect.p2 || edge.p2 == rect.p1 || edge.p2 == rect.p2) {
//                    // Skip it
//                } else {
//                    if (intersects(rect, edge)) {
//                        candidates.remove(rect)
//                    }
//                }
//            }
//        }
//
//        val combosByDistance = redTilePoints.subList(0, redTilePoints.lastIndex).flatMapIndexed { index, p1 ->
//            redTilePoints.subList(index + 1, redTilePoints.size).map { p2 ->
//                Pair(Pair(p1, p2), distance(p1, p2))
//            }
//        }
////
//
////
////        val validRectangles = filterRectangles(rectangles, lines)
////
////        val sortedRectangles = validRectangles.sortedByDescending { it.area() }
////        val rectsToArea = sortedRectangles.map { it to it.area() }
////        val largestArea = candidates.maxOf { it.area() }
//        // too low: 1206639344, too high: 1618956096
//
//        // not right: 1465767840
//
//        // ?? 4623743592
        println("Day 9, Part 2: The largest square with red tiles at opposite corners containing only green tiles is [$largestArea]")
    }

    fun findOutsidePoints(reorderedRedTilePoints: List<Point>, redTilePointsSet: Set<Point>): MutableSet<Point> {
        val outsidePoints = mutableSetOf<Point>()

        var direction = Direction.UP
        (reorderedRedTilePoints + listOf(reorderedRedTilePoints.first())).windowed(2).forEach { window ->
            val curr = window[0]
            val next = window[1]

            if (curr.y == next.y && curr.x < next.x) {
                if (direction == Direction.UP) {
                    listOf(Point.LEFT, Point.UP, Point.UP_LEFT).map { curr + it }.forEach {
                        if (!redTilePointsSet.contains(it)) outsidePoints.add(it)
                    }
                } else if (direction == Direction.DOWN) {
                    listOf(Point.UP_RIGHT).map { curr + it }.forEach {
                        if (!redTilePointsSet.contains(it)) outsidePoints.add(it)
                    }
                } else {
                    error("huh?")
                }
                direction = Direction.RIGHT
            } else if (curr.y == next.y && curr.x > next.x) {
                if (direction == Direction.UP) {
                    listOf(Point.DOWN_LEFT).map { curr + it }.forEach {
                        if (!redTilePointsSet.contains(it)) outsidePoints.add(it)
                    }
                } else if (direction == Direction.DOWN) {
                    listOf(Point.DOWN, Point.RIGHT, Point.DOWN_RIGHT).map { curr + it }.forEach {
                        if (!redTilePointsSet.contains(it)) outsidePoints.add(it)
                    }
                } else {
                    error("huh?")
                }
                direction = Direction.LEFT
            } else if (curr.x == next.x && curr.y < next.y) {
                if (direction == Direction.RIGHT) {
                    listOf(Point.UP, Point.RIGHT, Point.UP_RIGHT).map { curr + it }.forEach {
                        if (!redTilePointsSet.contains(it)) outsidePoints.add(it)
                    }
                } else if (direction == Direction.LEFT) {
                    listOf(Point.DOWN_RIGHT).map { curr + it }.forEach {
                        if (!redTilePointsSet.contains(it)) outsidePoints.add(it)
                    }
                } else {
                    error("huh?")
                }
                direction = Direction.DOWN
            } else if (curr.x == next.x && curr.y > next.y) {
                if (direction == Direction.RIGHT) {
                    listOf(Point.UP_LEFT).map { curr + it }.forEach {
                        if (!redTilePointsSet.contains(it)) outsidePoints.add(it)
                    }
                } else if (direction == Direction.LEFT) {
                    listOf(Point.DOWN, Point.LEFT, Point.DOWN_LEFT).map { curr + it }.forEach {
                        if (!redTilePointsSet.contains(it)) outsidePoints.add(it)
                    }
                } else {
                    error("huh?")
                }
                direction = Direction.UP
            } else {
                error("huh?")
            }
        }
        return outsidePoints
    }

    fun computeRedTilePoints(inputLines: List<String>): List<Point> = inputLines.map { Point.fromString(it) }

    fun filterRectangles(rectangles: List<Rectangle>, points: Collection<Point>): List<Rectangle> =
        rectangles.filter { rectangle ->
            points.none { point ->
                rectangle.containsPoint(point)
            }
        }

    fun computeRectangles(redTilePoints: List<Point>): List<Rectangle> =
        redTilePoints.subList(0, redTilePoints.lastIndex).flatMapIndexed { index, p1 ->
            redTilePoints.subList(index + 1, redTilePoints.size).map { p2 ->
                Rectangle(p1, p2)
            }
        }

    fun computeLineSegments(redTilePoints: List<Point>): List<Edge> =
        (redTilePoints + redTilePoints[0]).windowed(2).map {
            val p1 = it[0]
            val p2 = it[1]
            if (p1.x == p2.x && p1.y <= p2.y) {
                Edge(p1, p2)
            } else {
                Edge(p2, p1)
            }
        }

    fun doesLineIntersectRectangle(edge: Edge, rectangle: Rectangle): Boolean {


        return if (edge.isHorizontal() && edge.p1.y > rectangle.miny() && edge.p1.y < rectangle.maxy() && edge.p1.x <= rectangle.maxx() && edge.p2.x >= rectangle.minx()) {
            true
        } else if (edge.isVertical() && edge.p1.x > rectangle.minx() && edge.p1.x < rectangle.maxx() && edge.p1.y <= rectangle.maxy() && edge.p2.y >= rectangle.miny()) {
            true
        } else {
            false
        }
    }

    fun intersects(rect1: Rectangle, rect2: Rectangle): Boolean {
        val isLeft = rect1.maxx() < rect2.minx()
        val isRight = rect1.minx() > rect2.maxx()
        val isAbove = rect1.maxy() < rect2.miny()
        val isBelow = rect1.miny() > rect2.maxy()

        return !(isLeft || isRight || isAbove || isBelow)
    }


    data class Rectangle(val p1: Point, val p2: Point) {
        fun area(): Long = (abs(maxx() - minx()) + 1) * (abs(maxy() - miny()) + 1)
        fun minx() = min(p1.x, p2.x)
        fun maxx() = max(p1.x, p2.x)
        fun miny() = min(p1.y, p2.y)
        fun maxy() = max(p1.y, p2.y)

        fun containsPoint(point: Point): Boolean =
            point.x >= minx() && point.x <= maxx() && point.y >= miny() && point.y <= maxy()
    }

    enum class Direction { RIGHT, DOWN, LEFT, UP }

    data class Edge(val p1: Point, val p2: Point) {
        init {
            if (p1.x != p2.x && p1.y != p2.y) {
                error("Lines should run up/down or left/right")
            }
        }

        fun isHorizontal() = p1.y == p2.y
        fun isVertical() = p1.x == p2.x

        fun minx() = min(p1.x, p2.x)
        fun maxx() = max(p1.x, p2.x)
        fun miny() = min(p1.y, p2.y)
        fun maxy() = max(p1.y, p2.y)
    }
}
