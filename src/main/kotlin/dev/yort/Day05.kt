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
        val rangesSortedByFirst = processInputRanges(rawInputs).sortedBy { it.first }

        val processedRanges = mutableListOf<LongRange>()

        var currRange = rangesSortedByFirst.first()
        (1..rangesSortedByFirst.lastIndex).forEach { index ->
            val range = rangesSortedByFirst[index]
            if (currRange.contains(range.first) && currRange.contains(range.last)) {
                // Nothing to do here because it's already fully enclosed
            } else if (currRange.contains(range.first)) {
                currRange.last = range.last
            } else {
                processedRanges.add(currRange.toLongRange())
                currRange = range
            }
        }
        processedRanges.add(currRange.toLongRange())

        val countOfFreshIngredients = processedRanges.sumOf { it.last - it.first + 1 }
        println("Day 5, Part 2: There are [${countOfFreshIngredients}] possible fresh ingredients")
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

    private fun processInputRanges(rawInputs: List<Pair<String, String?>>): List<MutableRange> =
        rawInputs.filter { it.second != null }.map { MutableRange(it.first.toLong(), it.second!!.toLong()) }

    private fun processInputIngredients(rawInputs: List<Pair<String, String?>>): List<Long> =
        rawInputs.filter { it.second == null }.map { it.first.toLong() }

    private class MutableRange(var first: Long, var last: Long) {
        fun contains(v: Long) = v in first..last
        fun toLongRange() = first..last
    }
}
