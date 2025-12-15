package dev.yort

import dev.yort.Day10Take2.solveMachine
import kotlin.test.Test

class Day10Take2Test {
    @Test
    fun example1p2() {
        val spec = Day10.MachineSpec.parseLine("[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}")
        val steps = solveMachine(spec)
//        assertEquals(10, steps)
    }

//    @Test
//    fun example2p2() {
//        val spec = Day10.MachineSpec.parseLine("[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}")
//        val steps =solveMachine( spec)
//        assertEquals(12, steps)
//    }
//
//    @Test
//    fun example3p2() {
//        val spec = Day10.MachineSpec.parseLine("[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}")
//        val steps = solveMachine( spec)
//        assertEquals(11, steps)
//    }

}
