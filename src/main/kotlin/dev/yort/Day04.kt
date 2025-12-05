package dev.yort

import java.io.File

object Day04 {
    fun part1() {
        val grid = loadGridFromFile()

        val rollsWithFewerThan4Neighbors = grid.filter { it.value == '@' }
            .count { entry ->
                val countOfAdjacentRolls = entry.key.adjacentNeighbors().count { grid[it] == '@' }
//                if (countOfAdjacentRolls < 4) {
//                    println("point [${entry.key}] = neighbor count: [$countOfAdjacentRolls]")
//                }
                countOfAdjacentRolls < 4
            }

        println("Day 4, Part 1: There are [$rollsWithFewerThan4Neighbors] with fewer than 4 neighbors")
    }

    fun part2() {
        val grid = loadGridFromFile()

        val currentGrid = grid.toMutableMap()
        var totalRollsRemoved = 0

        while (true) {
            val changes = currentGrid.filter { it.value == '@' }
                .mapNotNull { entry ->
                    if (entry.key.adjacentNeighbors().count { currentGrid[it] == '@' } < 4) {
                        entry.key to 'x'
                    } else {
                        null
                    }
                }

            changes.forEach { change -> currentGrid[change.first] = change.second }

            totalRollsRemoved += changes.size

            if (changes.isEmpty()) {
                break
            }
        }

        println("Day 4, Part 2: It's possible to remove a total of [$totalRollsRemoved] rolls")
    }

    private fun loadGridFromFile(): Map<Point, Char> {
        val inputLines = File("inputs/Day04.txt").readLines().map { line ->
            line.toCharArray().toList()
        }
        val grid = (0..inputLines.lastIndex).flatMap { y ->
            (0..<inputLines[y].size).map { x ->
                Point(x.toLong(), y.toLong()) to inputLines[y][x]
            }
        }.toMap()
        return grid
    }
}
