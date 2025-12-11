package dev.yort

import dev.yort.Day10.solveMinButtonPresses
import kotlin.test.Test
import org.junit.jupiter.api.Assertions.*

class Day10Test {
    @Test
    fun example1() {
        val spec = Day10.MachineSpec.parseLine("[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}")
        val steps = solveMinButtonPresses(spec)
        assertEquals(2, steps)
    }

    @Test
    fun example2() {
        val spec = Day10.MachineSpec.parseLine("[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}")
        val steps = solveMinButtonPresses(spec)
        assertEquals(3, steps)
    }

    @Test
    fun example3() {
        val spec = Day10.MachineSpec.parseLine("[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}")
        val steps = solveMinButtonPresses(spec)
        assertEquals(2, steps)
    }

}
