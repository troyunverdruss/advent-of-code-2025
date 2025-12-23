package dev.yort

import kotlin.test.Test

class Day12Test {
    @Test
    fun `verify rotate90`() {
        val lines = """
            ABC
            DEF
            GHI
        """.trimIndent().lines()
        val grid = parseGrid(lines)
        printGrid(grid)
        val present = Day12.PresentShape(grid)

        val rotate90 = present.rotate90()
        printGrid(rotate90)

        val flipVertically = present.flipVertically()
        printGrid(flipVertically)

        val flipHorizontally = present.flipHorizontally()
        printGrid(flipHorizontally)
    }
}
