package dev.yort

import java.io.File

object Day02 {
    fun part1() {
        val ranges = readRangesFromFile()

        val sumOfInvalidIds = ranges.flatMap { range ->
            (range.min..range.max).map { v ->
                // Number is invalid if the first half is the same as the back half
                // If it's invalid, return the number, otherwise return 0 so it's easy to sum
                val vStr = v.toString()
                if (vStr.length % 2 == 0) {
                    val a = vStr.take(vStr.length / 2)
                    val b = vStr.substring(vStr.length / 2, vStr.length)
                    if (a == b) {
                        v
                    } else {
                        0
                    }
                } else {
                    0
                }
            }
        }.sum()

        println("Day 2, Part 1: sum of invalid IDs: [$sumOfInvalidIds]")
    }

    fun part2() {
        val ranges = readRangesFromFile()

        val sumOfValidIds = ranges.flatMap { range ->
            (range.min..range.max).flatMap { v ->
                // Number is invalid if the entire number is made up of a repeated sequence: 11, 222, 3434, 123123123
                val vStr = v.toString()

                (1..vStr.length / 2).map { subStrLength ->
                    if (vStr.length % subStrLength == 0) {
                        val nTimes = vStr.length / subStrLength
                        val a = vStr.take(subStrLength)
                        if (a.repeat(nTimes) == vStr) {
//                            println("found invalid ID: [$v]")
                            v
                        } else {
                            0
                        }
                    } else {
                        0
                    }
                }
            }.toSet() // filter out dupes within a given range, so we don't get 2222 twice, once for "2, 4 times", once for "22, 2 times"
        }.sum()

        println("Day 2, Part 2: sum of newly invalid IDs: [$sumOfValidIds]")
    }

    private fun readRangesFromFile(): List<Range> =
        File("inputs/Day02.txt")
            .readLines()[0]
            .split(",")
            .map { entry ->
                val parts = entry.split("-")
                Range(parts[0].toLong(), parts[1].toLong())
            }


    private data class Range(val min: Long, val max: Long)
}
