package dev.yort

import dev.yort.Day10.MachineSpec
import java.io.File

object Day10Take3 {
    fun run() {
        val specs = File("inputs/Day10.txt").readLines().map { (MachineSpec.parseLine(it)) }

        val sumOfMinButtonPresses = specs.mapIndexed { index, spec ->
            println("Starting on $index")
            solveMachine(spec)
        }

        println("Day 10, Part 2: The sum of the min number of button presses to reach the required joltage state is [$sumOfMinButtonPresses]")
    }

    fun solveMachine(spec: MachineSpec): Int {
        val result = solveStep(spec.joltages, 0, spec.buttons.sortedByDescending { it.size }, mutableMapOf())
        requireNotNull(result) { "Unable to find solution for machine: [$spec]" }
        return result
    }

    fun solveStep(joltages: List<Long>, steps: Int, buttons: List<List<Long>>, memo: MutableMap<List<Long>, Int?>): Int? {
        val memoKey = joltages
        if (joltages.all { it == 0L }) {
            memo[memoKey] = steps
            return steps
        }

        if (joltages.any { it < 0 }) {
            memo[memoKey] = null
            return null
        }

        val memoVal = memo[memoKey]
        if (memoVal != null) {
            return memoVal
        }

        val fewestSteps = buttons.mapNotNull { button ->
            val nextJoltages = pressButton(joltages, button)
            solveStep(nextJoltages, steps + 1, buttons, memo)
        }

        val result = if (fewestSteps.isNotEmpty()) {
            fewestSteps.min()
        } else {
            null
        }

        memo[memoKey] = result
        return memo[memoKey]
    }


    fun pressButton(joltages: List<Long>, button: List<Long>): List<Long> {
        val nextJoltages = joltages.toMutableList()
        button.forEach { index -> nextJoltages[index.toInt()] -= 1 }
        return nextJoltages
    }
}
