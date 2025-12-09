package dev.yort

import dev.yort.Day08.buildCircuits
import dev.yort.Day08.buildCircuitsUntilAllConnected
import dev.yort.Day08.computeProduct
import dev.yort.Day08.parsePoints
import dev.yort.Day08.processIntoPairsWithDistance
import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertEquals

class Day08Test {

    val lines = """
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689
        """.trimIndent().lines()

    @Test
    fun part1Example() {
        val points = parsePoints(lines)
        val pairsWithDistance = processIntoPairsWithDistance(points)

        val circuits = buildCircuits(pairsWithDistance, numberToTake = 10)
        assertEquals(40, computeProduct(circuits))
    }

    @Test
    fun part2Example() {
        val points = parsePoints(lines)
        val pairsWithDistance = processIntoPairsWithDistance(points)

        val product = buildCircuitsUntilAllConnected(pairsWithDistance, points.size)
        assertEquals(25272, product)
    }
}
