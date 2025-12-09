package dev.yort

import java.io.File

object Day08 {
    fun part1() {
        val lines = File("inputs/Day08.txt").readLines()

        val points = parsePoints(lines)
        val pairsWithDistance = processIntoPairsWithDistance(points)

        val circuits = buildCircuits(pairsWithDistance, numberToTake = 1000)

//        println("Found [${circuits.size}] distinct circuits")
        val productOfTop3Sizes = computeProduct(circuits)
        println("Day 8, Part 1: Product of largest 3 circuits is [$productOfTop3Sizes]")
    }

    fun part2() {
        val lines = File("inputs/Day08.txt").readLines()

        val points = parsePoints(lines)
        val pairsWithDistance = processIntoPairsWithDistance(points)

        val productOfXCoordinates = buildCircuitsUntilAllConnected(pairsWithDistance, points.size)
        println("Day 8, Part 2: Product of X coordinates for points that join into one network: [$productOfXCoordinates]")
    }

    fun parsePoints(lines: List<String>): List<Point> =
        lines.map { it.split(",") }.map { Point(it[0].toLong(), it[1].toLong(), it[2].toLong()) }

    fun processIntoPairsWithDistance(points: List<Point>): List<PairWithDistance> =
        (0..<points.lastIndex).flatMap { pointer1 ->
            ((pointer1 + 1)..points.lastIndex).map { pointer2 ->
                val a = points[pointer1]
                val b = points[pointer2]
                PairWithDistance(a, b, distance(a, b))
            }
        }


    fun buildCircuits(pairsWithDistance: List<PairWithDistance>, numberToTake: Int): MutableList<MutableSet<Point>> {
        val circuits = mutableListOf<MutableSet<Point>>()
        val sortedPairs = pairsWithDistance.sortedBy { it.distance }
        sortedPairs.take(numberToTake).forEach { pairWithDistance ->
            val a = pairWithDistance.a
            val b = pairWithDistance.b

            if (circuits.isEmpty()) {
                circuits.add(mutableSetOf(a, b))
            } else {
                val circuitsMatchingA = circuits.filter { it.contains(a) }
                val circuitsMatchingB = circuits.filter { it.contains(b) }

                if (circuitsMatchingA.size > 1 || circuitsMatchingB.size > 1) {
                    error("Something went wrong [$circuitsMatchingA], [$circuitsMatchingB]")
                }

                if (circuitsMatchingA.isEmpty() && circuitsMatchingB.isEmpty()) {
                    circuits.add(mutableSetOf(a, b))
                } else {

                    if (circuitsMatchingA.isNotEmpty() && circuitsMatchingB.isEmpty()) {
                        circuitsMatchingA.first().add(a)
                        circuitsMatchingA.first().add(b)
                    } else if (circuitsMatchingA.isEmpty()) {
                        circuitsMatchingB.first().add(a)
                        circuitsMatchingB.first().add(b)
                    } else if (circuitsMatchingA.first() == circuitsMatchingB.first()) {
                        // noop, both are already in here
                    } else {
                        // join two existing circuits together
                        circuitsMatchingA.first().addAll(circuitsMatchingB.first())
                        circuits.remove(circuitsMatchingB.first())
                    }
                }
            }
        }
        return circuits
    }

    fun buildCircuitsUntilAllConnected(pairsWithDistance: List<PairWithDistance>, distinctPoints: Int): Long {
        val circuits = mutableListOf<MutableSet<Point>>()
        val sortedPairs = pairsWithDistance.sortedBy { it.distance }
        var largestCountOfCircuits = 0

        sortedPairs.forEach { pairWithDistance ->
            val a = pairWithDistance.a
            val b = pairWithDistance.b

            if (circuits.isEmpty()) {
                circuits.add(mutableSetOf(a, b))
            } else {
                val circuitsMatchingA = circuits.filter { it.contains(a) }
                val circuitsMatchingB = circuits.filter { it.contains(b) }

                if (circuitsMatchingA.size > 1 || circuitsMatchingB.size > 1) {
                    error("Something went wrong [$circuitsMatchingA], [$circuitsMatchingB]")
                }

                if (circuitsMatchingA.isEmpty() && circuitsMatchingB.isEmpty()) {
                    circuits.add(mutableSetOf(a, b))
                } else {

                    if (circuitsMatchingA.isNotEmpty() && circuitsMatchingB.isEmpty()) {
                        circuitsMatchingA.first().add(a)
                        circuitsMatchingA.first().add(b)
                    } else if (circuitsMatchingA.isEmpty()) {
                        circuitsMatchingB.first().add(a)
                        circuitsMatchingB.first().add(b)
                    } else if (circuitsMatchingA.first() == circuitsMatchingB.first()) {
                        // noop, both are already in here
                    } else {
                        // join two existing circuits together
                        circuitsMatchingA.first().addAll(circuitsMatchingB.first())
                        circuits.remove(circuitsMatchingB.first())
                    }
                }

                if (circuits.size > largestCountOfCircuits) largestCountOfCircuits = circuits.size

                if (circuits.size == 1 && circuits[0].size == distinctPoints) {
                    // Return the product of the X coordinates when the graph joins up into a single circuit
                    return a.x * b.x
                }
            }
        }

        error("Never joined into one circuit")
    }


    fun computeProduct(circuits: MutableList<MutableSet<Point>>): Long =
        circuits.sortedByDescending { it.size }.take(3).fold(1L) { acc, s -> acc * s.size }

    data class PairWithDistance(val a: Point, val b: Point, val distance: Double)
}
