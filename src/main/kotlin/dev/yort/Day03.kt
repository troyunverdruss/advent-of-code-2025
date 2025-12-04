package dev.yort

import java.io.File

object Day03 {
    fun part1() {
        val inputLines = File("inputs/Day03.txt").readLines().map { line ->
            line.map { c -> c.digitToInt() }
        }

        val sumOfJoltages = inputLines.sumOf { line ->
            val first = findEarliestMax(line, 0, line.size - 1)
            val second = findEarliestMax(line, first.index + 1, line.size)
            val joltage = "${first.digit}${second.digit}".toInt()
//            println(joltage)
            joltage
        }

        println("Day 3, Part 1: Sum of all joltages when using 2 digits: [$sumOfJoltages]")
    }

    fun part2() {
        val inputLines = File("inputs/Day03.txt").readLines().map { line ->
            line.map { c -> c.digitToInt() }
        }

        val joltageDigits = 12

        val sumOfJoltages = inputLines.sumOf { line ->
            val digitData = mutableListOf<MaxResult>()

            repeat(joltageDigits) {
                val result = findEarliestMax(
                    line = line,
                    startIndex = (digitData.lastOrNull()?.index?.plus(1)) ?: 0,
                    endIndex = line.size - (joltageDigits - digitData.size - 1)
                )
                digitData.add(result)
            }
            val joltage =digitData.map { it.digit }.joinToString("").toLong()
//            println(joltage)
            joltage
        }

        println("Day 3, Part 2: Sum of all joltages when using 12 digits: [$sumOfJoltages]")
    }

    private fun findEarliestMax(line: List<Int>, startIndex: Int = 0, endIndex: Int = line.size): MaxResult {
        var highestValue = -1
        var highestValueIndex = -1

        (startIndex..<endIndex).forEach { index ->
            if (line[index] > highestValue) {
                highestValue = line[index]
                highestValueIndex = index
            }
        }
        return MaxResult(digit = highestValue, index = highestValueIndex)
    }

    private data class MaxResult(val digit: Int, val index: Int)
}
