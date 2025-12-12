package dev.yort

import java.io.File

object Day11 {

    private var isPart2 = false

    fun part1() {
        val nodes = File("inputs/Day11.txt").readLines().map { Node.parseString(it) }

        val you = nodes.find { it.id == "you" }!!
        val target = "out"
        val nodesById = nodes.associateBy { it.id }

        val memo = mutableMapOf<String, Long>()
        val paths = findPaths(you, target, setOf("you"), nodesById, memo)

        println("Day 11, Part 1: Number of paths from 'you' to 'out' is [${paths}]")
    }

    fun part2() {
        isPart2 = true
        val nodes = File("inputs/Day11.txt").readLines().map { Node.parseString(it) }


        val svrToFftPathCount = countPaths("svr", "fft", nodes)
        val fftToDacPathCount = countPaths("fft", "dac", nodes)
        val dacToOutPathCount = countPaths("dac", "out", nodes)
        val totalPaths = svrToFftPathCount * fftToDacPathCount * dacToOutPathCount
        println("Day 11, Part 2: Number of paths from 'svr' to 'out' via both 'fft' and 'dac' is [$totalPaths]")
    }

    private fun countPaths(start: String, target: String, nodes: List<Node>): Long {
        val nodesById = nodes.associateBy { it.id }

        val startNode = nodes.find { it.id == start }!!

        val memo = mutableMapOf<String, Long>()
        val paths = findPaths(startNode, target, setOf(startNode.id), nodesById, memo)

        return paths
    }

    private fun findPaths(curr: Node, target: String, visited: Set<String>, nodesById: Map<String, Node>, memo: MutableMap<String, Long>): Long {
        val memoVal = memo[curr.id]
        if (memoVal != null) {
//            println("At node: [${curr.id}]. Current path: [$path] Would have returned [$memoVal]")
            return memoVal
        }

        // Do I need to worry about cycles in the graph?

        val paths = curr.connections.sumOf { nextId ->
            if (target != "out" && nextId == "out") {
                0L
            } else if (visited.contains(nextId)) {
                println("Not counting because we ran into: [$nextId]")
                0L
            } else if (nextId == target) {
                1
            } else {
                findPaths(nodesById[nextId]!!, target, visited + nextId, nodesById, memo)
            }
        }
//        println("At node: [${curr.id}]. Current path: [$path] Storing [$paths]")
        memo[curr.id] = paths //.filter { it.isNotEmpty() }

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
