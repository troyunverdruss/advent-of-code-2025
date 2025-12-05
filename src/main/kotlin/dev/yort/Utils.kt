package dev.yort

data class Point(val x: Long, val y: Long) {
    operator fun plus(other: Point): Point {
        return Point(this.x + other.x, this.y + other.y)
    }

    fun adjacentNeighbors(): List<Point> {
        return ADJACENT.map { this + it }
    }

    companion object {
        val DIRECTLY_ADJACENT = listOf(
            Point(0, -1), // up
            Point(0, 1), // down
            Point(-1, 0), // left
            Point(1, 0), // right
        )
        val DIAGONALLY_ADJACENT = listOf(
            Point(-1, -1), // up left
            Point(1, -1), // up right
            Point(-1, 1), // down left
            Point(1, 1), // down right
        )

        val ADJACENT = DIRECTLY_ADJACENT + DIAGONALLY_ADJACENT
    }
}
