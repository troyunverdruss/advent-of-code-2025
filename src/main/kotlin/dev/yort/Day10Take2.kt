package dev.yort

import dev.yort.Day10.MachineSpec
import java.io.File
import java.util.LinkedList

object Day10Take2 {
    fun run() {
        val specs = File("inputs/Day10.txt").readLines().map { (MachineSpec.parseLine(it)) }

        val sumOfMinButtonPresses = specs.map { solveMachine(it) }

        println("Day 10, Part 1: The sum of the min number of button presses to reach the required joltage state is [$sumOfMinButtonPresses]")
    }

    fun solveMachine(spec: MachineSpec) {
        val stack = LinkedList<State>()
        val memo = mutableMapOf<State, Long?>()
        stack.push(State.build(spec.joltages, 0, spec.buttons))

        while (stack.isNotEmpty()) {
            val curr = requireNotNull(stack.pop())
            if (memo.containsKey(curr)) {
                continue
            }
            if (curr.unpressedButtons.isEmpty() && !curr.targetReached()) {
                memo[curr] = null
            } else if (curr.targetReached()) {
                memo[curr] = curr.presses
            } else if (!curr.valid()) {
                memo[curr] = null
            } else {
                val button = curr.unpressedButtons.first()

                // Save the current state so we can come back here
                if (curr.unpressedButtons.size > 1) {
                    val unpressedButtons = curr.unpressedButtons.subList(1, curr.unpressedButtons.size)
                    stack.push(State.build(curr.joltages, curr.presses, unpressedButtons))
                }
                // Saved

                val newJoltages = pressButton(curr.joltages, button)
                stack.push(State.build(newJoltages, curr.presses + 1, spec.buttons))
            }
        }

        val x = 0;
    }

    private data class State(val joltages: List<Long>) {
        var presses: Long = -1
        var unpressedButtons: List<List<Long>> = emptyList()

        fun targetReached() = joltages.all { it == 0L }
        fun valid() = joltages.all { it >= 0 }

        companion object {
            fun build(remaining: List<Long>, presses: Long, unpressedButtons: List<List<Long>>): State {
                val state = State(remaining)
                state.presses = presses
                state.unpressedButtons = unpressedButtons

                return state
            }
        }
    }


    fun pressButton(joltages: List<Long>, button: List<Long>): List<Long> {
        val nextJoltages = joltages.toMutableList()
        button.forEach { index -> nextJoltages[index.toInt()] -= 1 }
        return nextJoltages
    }
}
