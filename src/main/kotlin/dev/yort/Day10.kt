package dev.yort

import java.io.File

object Day10 {
    fun part1() {
        val specs = File("inputs/Day10.txt").readLines().map { (MachineSpec.parseLine(it)) }

        val sumOfMinButtonPresses = specs.sumOf { solveIndicatorMinButtonPresses(it) }

        println("Day 10, Part 1: The sum of the min number of button presses to reach the required indicator state is [$sumOfMinButtonPresses]")
    }

    fun part2() {
        val specs = File("inputs/Day10.txt").readLines().map { (MachineSpec.parseLine(it)) }

        val sumOfMinButtonPresses = specs.sumOf { solveJoltageMinButtonPresses(it) }

        println("Day 10, Part 1: The sum of the min number of button presses to reach the required joltage state is [$sumOfMinButtonPresses]")
    }

    fun solveIndicatorMinButtonPresses(spec: MachineSpec): Long {
        val startingState = IndicatorState.build(spec.indicatorDiagram.map { false }, 0)
        val toVisit = mutableListOf<IndicatorState>()
        val toVisitSet = mutableSetOf<List<Boolean>>()
        val visited = mutableSetOf<List<Boolean>>()

        toVisit.add(startingState)
        toVisitSet.add(startingState.indicators)

        while(toVisit.isNotEmpty()) {
            val curr = toVisit.removeFirst()
            toVisitSet.remove(curr.indicators)
            visited.add(curr.indicators)

            if (curr.indicators == spec.indicatorDiagram) {
                return curr.steps
            }

            spec.buttons.forEach { button ->
                val next = curr.indicators.mapIndexed { index, indicator ->
                    if (button.contains(index.toLong())) {
                        !indicator
                    } else {
                        indicator
                    }
                }
                if (!toVisitSet.contains(next) && !visited.contains(next)) {
                    toVisit.add(IndicatorState.build(next, curr.steps + 1))
                    toVisitSet.add(next)
                }
            }
        }

        error("Never found a combo that was able to create the desired indicator state")
    }

    private data class IndicatorState(val indicators: List<Boolean>) {
        var steps: Long = -1
        companion object {
            fun build(indicatorState: List<Boolean>, steps: Long): IndicatorState {
                val state = IndicatorState(indicatorState)
                state.steps = steps
                return state
            }
        }
    }

    fun solveJoltageMinButtonPresses(spec: MachineSpec): Long {
        val startingState = JoltageState.build(spec.indicatorDiagram.map { 0 }, 0)
        val toVisit = mutableListOf<JoltageState>()
        val toVisitSet = mutableSetOf<List<Long>>()
        val visited = mutableSetOf<List<Long>>()

        toVisit.add(startingState)
        toVisitSet.add(startingState.joltages)

        while(toVisit.isNotEmpty()) {
            val curr = toVisit.removeFirst()
            toVisitSet.remove(curr.joltages)
            visited.add(curr.joltages)

            if (curr.joltages == spec.joltages) {
                return curr.steps
            }

            spec.buttons.forEach { button ->
                val next = curr.joltages.mapIndexed { index, joltage ->
                    if (button.contains(index.toLong())) {
                        joltage + 1
                    } else {
                        joltage
                    }
                }
                if (!toVisitSet.contains(next) && !visited.contains(next)) {
                    toVisit.add(JoltageState.build(next, curr.steps + 1))
                    toVisitSet.add(next)
                }
            }
        }

        error("Never found a combo that was able to create the desired joltage state")
    }

    private data class JoltageState(val joltages: List<Long>) {
        var steps: Long = -1
        companion object {
            fun build(joltageState: List<Long>, steps: Long): JoltageState {
                val state = JoltageState(joltageState)
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
