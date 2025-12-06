package dev.yort

import java.io.File

object Day06 {
    fun part1() {
        val splitLines = File("inputs/Day06.txt").readLines().map { it.split("""\s+""".toRegex()) }
        val digitLines = splitLines.subList(0, splitLines.size - 1).map { l -> l.filter { it.isNotBlank() }.map { v -> v.toLong() } }
        val operators = splitLines[splitLines.lastIndex].filter { it.isNotBlank() }

        val checksumOfHomework = (0..digitLines.first().lastIndex).sumOf { index ->
            val digits = digitLines.map { it[index] }
            val operator = operators[index]
//            println("digits: [$digits], operator: [$operator]")
            val result = when (operator) {
                "+" -> digits.fold(accStart(operator)) { acc, v -> acc + v }
                "*" -> digits.fold(accStart(operator)) { acc, v -> acc * v }
                else -> error("Unknown operator")
            }
//            println("result: [$x]")
            result
        }

        println("Day 6, Part 1: the checksum for the homework is [$checksumOfHomework]")
    }

    fun part2() {
        val rawLines = File("inputs/Day06.txt").readLines()
        val digitData = rawLines.subList(0, rawLines.lastIndex)
        val operators = rawLines[rawLines.lastIndex].split("""\s+""".toRegex()).filter { it.isNotBlank() }

        val separatorIndexes =
            listOf(-1) + (0..<rawLines[0].length).mapNotNull { index ->
                if (rawLines.map { it[index] }.all { it == ' ' }) {
                    index
                } else {
                    null
                }
            } + (rawLines[0].length)

        val columnRanges = separatorIndexes.windowed(2).map { ((it[0] + 1)..<it[1]).reversed() }

        val checksum = columnRanges.mapIndexed { index, range ->
            val operator = operators[index]
            var acc = accStart(operator)
            range.forEach { columnIndex ->
                val number = digitData.map { it[columnIndex] }.filter { it != ' ' }.joinToString("").toLong()
//                println("number: [$number]")
                when (operator) {
                    "+" -> acc += number
                    "*" -> acc *= number
                    else -> error("Unknown operator")
                }
            }
//            println("tally [$tally]")
            acc
        }.sum()

        println("Day 6, Part 2: the checksum for RTL math is [$checksum]")
    }

    private fun accStart(operator: String) =
        when (operator) {
            "+" -> 0L
            "*" -> 1L
            else -> error("Unknown operator")
        }
}
