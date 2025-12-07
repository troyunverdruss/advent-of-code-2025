package dev.yort

object Day07 {
    fun part1() {
        val grid = loadGridFromFile("inputs/Day07.txt").toMutableMap()
        val start = findGridEntry(grid = grid, value = 'S')

        val toTrace = mutableSetOf(start.key)
        val visited = mutableSetOf<Point>()

        val splitAt = mutableSetOf<Point>()

//        printGrid(grid)
        while (toTrace.isNotEmpty()) {
            var curr = toTrace.asSequence().first()
            toTrace.remove(curr)

            while (true) {
                curr += Point.DOWN
                visited.add(curr)

                if (grid[curr] == null) {
                    break
                } else if (grid[curr] == '^') {
                    splitAt.add(curr)
                    if (!visited.contains(curr + Point.LEFT)) toTrace.add(curr + Point.LEFT)
                    if (!visited.contains(curr + Point.RIGHT)) toTrace.add(curr + Point.RIGHT)
                    break
                }
                else {
                    grid[curr] = '|'
                }
            }
        }

//        printGrid(grid)
        println("Day 7, Part 1: The tachyon beam was split [${splitAt.size}] times")
    }
}
