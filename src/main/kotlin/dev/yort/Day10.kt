package dev.yort

import java.io.File
import kotlin.math.min

object Day10 {
    fun part1() {
        val specs = File("inputs/Day10.txt").readLines().map { (MachineSpec.parseLine(it)) }

        val sumOfMinButtonPresses = specs.sumOf { solveIndicatorMinButtonPresses(it) }

        println("Day 10, Part 1: The sum of the min number of button presses to reach the required indicator state is [$sumOfMinButtonPresses]")
    }

    fun part2() {
        val specs = File("inputs/Day10.txt").readLines().map { (MachineSpec.parseLine(it)) }

        val sumOfMinButtonPresses = specs.mapIndexed { index, spec ->
            println("starting: $index")
            val result = solveJoltageMinButtonPressesWithSearching(index, spec)
            println("  result: $result")
            result
        }.sum()

        println("Day 10, Part 1: The sum of the min number of button presses to reach the required joltage state is [$sumOfMinButtonPresses]")
    }

    fun solveIndicatorMinButtonPresses(spec: MachineSpec): Long {
        val startingState = IndicatorState.build(spec.indicatorDiagram.map { false }, 0)
        val toVisit = mutableListOf<IndicatorState>()
        val toVisitSet = mutableSetOf<List<Boolean>>()
        val visited = mutableSetOf<List<Boolean>>()

        toVisit.add(startingState)
        toVisitSet.add(startingState.indicators)

        while (toVisit.isNotEmpty()) {
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

    fun solveJoltageMinButtonPressesWithSorted(index: Int, spec: MachineSpec): Long {
        println("Starting $index")
        println("Buttons: ${spec.buttons}")
        val remainingJoltages = spec.joltages.toMutableList()
        var buttonPresses = 0L

        while (remainingJoltages.any { it > 0 }) {
            println("Starting joltages: $remainingJoltages")
            val indexedJoltages = remainingJoltages
                .mapIndexed { index, joltage -> IndexedJoltage(index, joltage) }

            val targetJoltage = indexedJoltages.filter { remainingJoltages[it.index] > 0 }.minBy { it.joltage }
            println("Targeting: $targetJoltage")

            val buttons = spec.buttons
                .filter { it.contains(targetJoltage.index.toLong()) }
                .filterNot { it.any { index -> remainingJoltages[index.toInt()] == 0L } }
            val bestButton = buttons.maxBy { scoreButton(targetJoltage.index, remainingJoltages, it) }
            println("Picked best button: $bestButton")

            val presses = min(
                targetJoltage.joltage,
                bestButton.filterNot { it == targetJoltage.index.toLong() }.minOfOrNull { remainingJoltages[it.toInt()] }
                    ?: Long.MAX_VALUE
            )
            buttonPresses += presses
            println("Pressing it $presses times")

            bestButton.forEach { index ->
                remainingJoltages[index.toInt()] -= presses
            }
            println("Ending joltages: $remainingJoltages")
        }

        return buttonPresses
    }

    fun scoreButton(targetIndex: Int, remainingJoltages: List<Long>, button: List<Long>): Long {
        return button.filterNot { it == targetIndex.toLong() }.sumOf { remainingJoltages[it.toInt()] }
    }

    fun solveJoltageMinButtonPressesWithSearching(index: Int, spec: MachineSpec): Long {
//        println("starting: $index")
        val startingState = JoltageState.build(spec.indicatorDiagram.map { 0 }, 0)
        val toVisit = mutableListOf<JoltageState>()
        val toVisitSet = mutableSetOf<List<Long>>()
        val visited = mutableSetOf<List<Long>>()

        toVisit.add(startingState)
        toVisitSet.add(startingState.joltages)

        while (toVisit.isNotEmpty()) {
            val curr = toVisit.removeFirst()
            toVisitSet.remove(curr.joltages)
            visited.add(curr.joltages)

            if (curr.joltages == spec.joltages) {
                return curr.steps
            }

            val targetJoltage = curr.joltages
                .mapIndexed { index, joltage -> IndexedJoltage(index, joltage) }
                .filter { ij -> ij.joltage != spec.joltages[ij.index] }
                .minBy { ij -> spec.joltages[ij.index] }

            val joltagesToAvoid = curr.joltages
                .mapIndexed { index, joltage -> IndexedJoltage(index, joltage) }
                .filter { ij ->
                    spec.joltages[ij.index] == ij.joltage
                }.map { ij -> ij.index.toLong() }
                .toSet()
//            if (spec.joltages.mapIndexed { index, j -> curr.joltages[index] == j }.count { it } == 3) {
//                println("hi")
//            }

            val buttonsToTry = spec.buttons
                .filter { button -> button.contains(targetJoltage.index.toLong()) }
                .filterNot { button ->
                    joltagesToAvoid.any { j -> button.contains(j) }
                }
            buttonsToTry
                .sortedByDescending { it.size }
                .forEach { button ->
                    var anyOver = false
                    val next = curr.joltages.mapIndexed { index, joltage ->
                        if (button.contains(index.toLong())) {
                            if (joltage + 1 > spec.joltages[index]) anyOver = true
                            joltage + 1
                        } else {
                            joltage
                        }
                    }
                    if (!anyOver && !toVisitSet.contains(next) && !visited.contains(next)) {
                        toVisit.add(JoltageState.build(next, curr.steps + 1))
                        toVisitSet.add(next)
                    }
                }
        }

        error("Never found a combo that was able to create the desired joltage state")
    }

    private data class IndexedJoltage(val index: Int, val joltage: Long)
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
