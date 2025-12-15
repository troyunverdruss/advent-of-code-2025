package dev.yort

import dev.yort.Day10.solveIndicatorMinButtonPresses
import dev.yort.Day10.solveJoltageMinButtonPressesWithSearching
import dev.yort.Day10.solveJoltageMinButtonPressesWithSorted
import dev.yort.Day10Part2Take4.solveMachineZ3
import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertEquals

class Day10Test {
    @Test
    fun example1() {
        /*
start with closest target value, filter for buttons that include that
score the options based on other buttons and the delta they have with their targets, pick the *largest*:
(0,2) => 4
(0,1) => 5

do target times of that button
(0,1) * 3 => {3,3,0,0}

find next closest non-zero value, which is target [1] (5-3 = 2)
find buttons and score them, exclude buttons with [0] in them:
(1,3) => 7
# exclude (0,1)

 do target times of that button
 (1,3) * 2 => {3,5,0,2}

 next target is [2] (4-0 = 4)
 buttons:
 (2) => 0
 (2,3) => 4 (7-2)
 # exclude (0,2)

do (target times - (min of all the other deltas)) of that button
(2,3) * 4 => {3,5,4,6}

(3) once => {3,5,4,7}



         */
        val spec = Day10.MachineSpec.parseLine("[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}")
        val steps = solveIndicatorMinButtonPresses(spec)
        assertEquals(2, steps)
    }
    /*
    {0,0,0,0,0}

    start with [2]
    (0,2,3,4) => 7+7+2=16**
    (2,3) => 3
    (0,1,2) => 12
    (1,2,3,4) => 5+7+2=14







     */


    @Test
    fun example2() {
        val spec = Day10.MachineSpec.parseLine("[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}")
        val steps = solveIndicatorMinButtonPresses(spec)
        assertEquals(3, steps)
    }

    @Test
    fun example3() {
        val spec = Day10.MachineSpec.parseLine("[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}")
        val steps = solveIndicatorMinButtonPresses(spec)
        assertEquals(2, steps)
    }

    @Test
    fun example1p2() {
        val spec = Day10.MachineSpec.parseLine("[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}")
        val steps = solveMachineZ3(spec)
        assertEquals(10, steps)
    }

    @Test
    fun z3Testing() {
        val spec = Day10.MachineSpec.parseLine("[..] (0,1) (1) (0,2) {2,5,1}")
        val steps = solveJoltageMinButtonPressesWithSearching(5, spec)
        assertEquals(4, steps)
    }

    @Test
    fun example2p2() {
        val spec = Day10.MachineSpec.parseLine("[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}")
        val steps = solveMachineZ3(spec)
        assertEquals(12, steps)
    }

    @Test
    fun example3p2() {
        val spec = Day10.MachineSpec.parseLine("[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}")
        val steps = solveMachineZ3(spec)
        assertEquals(11, steps)
    }

    @Test
    fun inputTest() {
        val spec = Day10.MachineSpec.parseLine("[.####.#.] (3,4,5,7) (2,4,5,6,7) (1,4,7) (1,3,4,7) (1,2,3,4,5,7) (7) (1,2,3,6) (0,1,3,6,7) {4,59,39,250,242,220,26,250}")
        val steps = solveJoltageMinButtonPressesWithSorted(0, spec)
        assertEquals(0, steps)
    }
}
