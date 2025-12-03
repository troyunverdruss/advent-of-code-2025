package dev.yort

import java.io.File

object Day01 {
    const val POSITIONS = 100

    fun part1() {
        val rotations = readRotationsFromFile()

        var currentPosition = 50
        var zeroCounter = 0

        rotations.forEach { rotation ->
            currentPosition = when (rotation.direction) {
                "L" -> (currentPosition - rotation.distance) % POSITIONS
                "R" -> (currentPosition + rotation.distance) % POSITIONS
                else -> throw RuntimeException("Unknown direction: ${rotation.direction}")
            }
//            println("Processed rotation [$rotation], current position is [$currentPosition].")
            if (currentPosition == 0) {
                zeroCounter += 1
            }
        }

        println("Number of times the dial pointed at position 0: $zeroCounter")
    }

    fun part2() {
        val rotations = readRotationsFromFile()

        val dial = Dial()
        var zeroCounter = 0

        rotations.forEach { rotation ->
            val clickedOntoZero = dial.rotateAndCountZero(rotation)
            zeroCounter += clickedOntoZero
            println("Processed rotation [$rotation], current position is [${dial.position}], clicked onto zero [$clickedOntoZero] times, zero counter: [$zeroCounter].")
        }

        println("Number of times the dial clicked onto position 0: $zeroCounter")
    }

    private fun readRotationsFromFile(): List<Rotation> =
        File("inputs/Day01.txt").readLines().map { Rotation.fromInput(it) }

    private data class Rotation(val direction: String, val distance: Int) {
        companion object {
            fun fromInput(input: String) = Rotation(
                direction = input.get(0).toString(),
                distance = input.substring(1).toInt()
            )
        }
    }

    private data class Dial(var position: Int = 50) {
        fun rotateAndCountZero(rotation: Rotation): Int {
            var zeroCounter = 0
            repeat(rotation.distance) {
                when (rotation.direction) {
                    "L" -> position--
                    "R" -> position++
                    else -> throw RuntimeException("Unknown direction: ${rotation.direction}")
                }
                if (position < 0) position += POSITIONS
                if (position >= POSITIONS) position -= POSITIONS
                if (position == 0) zeroCounter++
            }
            return zeroCounter
        }
    }
}
