package dev.yort

import dev.yort.Day09.computeLineSegments
import dev.yort.Day09.computeRectangles
import dev.yort.Day09.computeRedTilePoints
import dev.yort.Day09.doesLineIntersectRectangle
import dev.yort.Day09.filterRectangles
import dev.yort.Day09.findOutsidePoints
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse

class Day09Test {
    val lines = """
        7,1
        11,1
        11,7
        9,7
        9,5
        2,5
        2,3
        7,3
    """.trimIndent().lines()

//    @Test
    fun `intersection test`() {
        val r = Day09.Rectangle(Point(2, 3), Point(9, 5))
        val l = Day09.Edge(Point(7, 1), Point(7, 3))
        val res = doesLineIntersectRectangle(l, r)
        assertFalse(res)
    }

//    @Test
    fun `missing bottom left rectangle`() {
        val inputLines = """
            1,0
            3,0
            3,10
            0,10
            0,9
            2,9
            2,8
            0,8
            0,7
            1,7
        """.trimIndent().lines()

        val redTilePoints = computeRedTilePoints(inputLines)
        val lines = computeLineSegments(redTilePoints)
        val rectangles = computeRectangles(redTilePoints)

//        val validRectangles = filterRectangles(rectangles, lines)

        val x=  0
    }

//    @Test
    fun `large interior-ish rectangle shouldn't be the solution`() {
//        val inputLines = """
//            0,0
//            10,0
//            10,1
//            1,1
//            1,9
//            10,9
//            10,10
//            0,10
//        """.trimIndent().lines()

//        val inputLines = """
//            0,0
//            20,0
//            20,6
//            16,6
//            16,2
//            12,2
//            12,17
//            16,17
//            16,14
//            20,14
//            20,20
//            0,20
//        """.trimIndent().lines()
val inputLines = """
1,5
3,5
3,8
7,8
7,5
9,5
9,10
11,10
11,3
6,3
6,7
4,7
4,1
13,1
13,12
1,12
        """.trimIndent().lines()

        val redTilePoints = computeRedTilePoints(inputLines)
        // Find the leftmost, topmost point
        val miny = redTilePoints.minOf { it.y }
        val startingPoint = redTilePoints.filter { it.y == miny }.minBy { it.x }

        val startingPointIndex = redTilePoints.indexOf(startingPoint)
        val reorderedRedTilePoints = redTilePoints.subList(startingPointIndex, redTilePoints.size) + redTilePoints.subList(0, startingPointIndex)
        val redTilePointsSet = redTilePoints.toSet()

        val outsidePoints = findOutsidePoints(reorderedRedTilePoints, redTilePointsSet)
        val rectangles = computeRectangles(redTilePoints)
        val filteredRectangles = filterRectangles(rectangles, outsidePoints)

        val grid = mutableMapOf<Point, Char>()
        (-2L..22).forEach { y ->
            (-2L..22).forEach { x ->
                grid[Point(x, y)] = '.'
            }
        }
        redTilePoints.forEach { grid[it] = '#' }
        outsidePoints.forEach { grid[it] = 'o' }
        printGrid(grid)

        val largestArea = filteredRectangles.maxOf { it.area() }

        assertEquals(72, largestArea)

//        val validRectangles = filterRectangles(rectangles, lines)
//        val sortedRectangles = validRectangles.sortedByDescending { it.area() }.map { it to it.area() }
        val x=  0
    }


}
