package dev.yort

import java.io.File

object Day05 {
    fun part1() {
        val rawInputs = readRawInputs()
        val ranges = processInputRanges(rawInputs)
        val ingredients = processInputIngredients(rawInputs)

        val freshIngredients = ingredients.count { i -> ranges.any { r -> r.contains(i) } }

        println("Day 5, Part 1: There are [$freshIngredients] fresh ingredients")
    }

    fun part2() {
        val rawInputs = readRawInputs()
        val ranges = processInputRanges(rawInputs)

        val naiveNums = ranges.sumOf { it.last - it.first + 1 }
        println(naiveNums)

    }

    private fun readRawInputs(): List<Pair<String, String?>> {
        val rawInputs = File("inputs/Day05.txt").readLines().mapNotNull { line ->
            if (line.contains("-")) {
                val parts = line.split("-", limit = 2)
                Pair(parts[0], parts[1])
            } else if (line.isEmpty()) {
                // skip it
                null
            } else {
                Pair(line, null)
            }
        }
        return rawInputs
    }

    private fun processInputRanges(rawInputs: List<Pair<String, String?>>): List<LongRange> =
        rawInputs.filter { it.second != null }.map { LongRange(it.first.toLong(), it.second!!.toLong()) }

    private fun processInputIngredients(rawInputs: List<Pair<String, String?>>): List<Long> =
        rawInputs.filter { it.second == null }.map { it.first.toLong() }
}
