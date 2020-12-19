package day16

import java.io.File

val inputPassages = File("inputs/day16.txt").readText().split("\n\n")

fun main() {
    val (rawRules, rawYourTicket, rawNearbyTickets) = inputPassages
    val rules = rawRules.lines().map {
        val (name, groupedRules) = it.split(": ")
        val separatedRules = groupedRules.split(" or ")
        val rangedRules = separatedRules.map {
            val (min, max) = it.split("-")
            min.toInt()..max.toInt()
        }
        name to rangedRules
    }.toMap()
    val nearbyTickets = rawNearbyTickets.lines().also(::println).drop(1).filter { it.isNotBlank() }
        .map { it.split(",").map { it.toInt() } }
    val errorRate = nearbyTickets.sumBy { singleTicket ->
        singleTicket.sumBy { single ->
            if (isNumberTotallyInvalid(single, rules)) {
                single
            } else
                0
        }
    }
    println("part1 $errorRate")
    val part2Tickets = nearbyTickets.filter { singleTicket ->
        val issues = singleTicket.sumBy { single ->
            if (isNumberTotallyInvalid(single, rules)) {
                single
            } else
                0
        }
        issues == 0 //todo: replace copy-paste solution with none/any filtering instead
    }
}

fun isNumberTotallyInvalid(i: Int, r: Map<String, List<IntRange>>): Boolean {
    val allValidRanges = r.values.flatten()
    return allValidRanges.none { i in it }
}