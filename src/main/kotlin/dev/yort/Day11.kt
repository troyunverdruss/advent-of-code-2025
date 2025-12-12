package dev.yort

import java.io.File

object Day11 {
    fun part1() {
        val nodes = File("inputs/Day11.txt").readLines().map { Node.parseString(it) }

        val you = nodes.find { it.id == "you" }!!
        val target = "out"
        val nodesById = nodes.associateBy { it.id }

        val memo = mutableMapOf<String, Long>()
        val numberOfPaths = findPaths(you, target, nodesById, memo)

        println("Day 11, Part 1: Number of paths from 'you' to 'out' is [$numberOfPaths]")
    }

    private fun findPaths(curr: Node, target: String, nodesById: Map<String, Node>, memo: MutableMap<String, Long>): Long {
        val memoVal = memo[curr.id]
        if (memoVal != null) {
            return memoVal
        }

        // Do I need to worry about cycles in the graph?

        val paths = curr.connections.sumOf { nextId ->
            if (nextId == target) {
                1
            } else {
                findPaths(nodesById[nextId]!!, target, nodesById, memo)
            }
        }
        memo[curr.id] = paths


        return memo[curr.id]!!
    }


    private data class State(val id: String) {
        var steps: Long = -1

        companion object {
            fun build(id: String, steps: Long): State {
                val state = State(id)
                state.steps = steps
                return state
            }
        }
    }

    data class Node(val id: String, val connections: List<String>) {
        companion object {
            fun parseString(line: String): Node {
                val id = line.split(":")[0]
                val connections = line.split(":")[1].split(" ").filter { it.isNotBlank() }
                return Node(id, connections)
            }
        }
    }
}
