package dev.yort

import java.io.File

object Day10 {
    fun part1() {
        val specs = File("inputs/Day10.txt").readLines().map { (MachineSpec.parseLine(it)) }

        val sumOfMinButtonPresses = specs.sumOf { solveMinButtonPresses(it) }

        println("Day 10, Part 1: The sum of the min number of button presses is [$sumOfMinButtonPresses]")
    }

    fun solveMinButtonPresses(spec: MachineSpec): Long {
        val startingState = State.build(spec.indicatorDiagram.map { false }, 0)
        val toVisit = mutableListOf<State>()
        val toVisitSet = mutableSetOf<List<Boolean>>()
        val visited = mutableSetOf<List<Boolean>>()

        toVisit.add(startingState)
        toVisitSet.add(startingState.indicatorState)

        while(toVisit.isNotEmpty()) {
            val curr = toVisit.removeFirst()
            toVisitSet.remove(curr.indicatorState)
            visited.add(curr.indicatorState)

            if (curr.indicatorState == spec.indicatorDiagram) {
                return curr.steps
            }

            spec.buttons.forEach { button ->
                val next = curr.indicatorState.mapIndexed {index, indicator ->
                    if (button.contains(index.toLong())) {
                        !indicator
                    } else {
                        indicator
                    }
                }
                if (!toVisitSet.contains(next) && !visited.contains(next)) {
                    toVisit.add(State.build(next, curr.steps + 1))
                    toVisitSet.add(next)
                }
            }
        }

        error("Never found a combo that was able to create the desired indicator state")
    }

    private data class State(val indicatorState: List<Boolean>) {
        var steps: Long = -1
        companion object {
            fun build(indicatorState: List<Boolean>, steps: Long): State {
                val state = State(indicatorState)
                state.steps = steps
                return state
            }
        }
    }

    data class MachineSpec(val indicatorDiagram: List<Boolean>, val buttons: List<List<Long>>, val joltages: List<Long>) {
        companion object {

            fun parseLine(line: String): MachineSpec {
                val indicatorDiagram =
                    line.split("]").first().replace("[", "").map {
                        when (it) {
                            '.' -> false
                            '#' -> true
                            else -> error("Unknown indicator symbol: [$it]")
                        }
                    }
                val buttons =
                    line.split("]")[1]
                        .split("{").first()
                        .split(" ")
                        .filter { it.isNotEmpty() }
                        .map { it.replace("(", "").replace(")", "") }
                        .map { it.split(",") }
                        .map { strings -> strings.map { it.toLong() } }

                val joltages =
                    line.split("{")[1]
                        .replace("}", "")
                        .split(",")
                        .map { it.toLong() }

                return MachineSpec(indicatorDiagram, buttons, joltages)
            }
        }
    }
}
