package day6

import java.io.File

val groups = File("inputs/day6.txt")
    .readText()
    .split("\n\n")

// Treat whole group as a set to remove duplicates
fun partOne() {
    val count = groups
        .map { it.replace("\n", "").toSet() }
        .sumBy { it.count() }
    println(count)
}

// Build per-group intersection
fun partTwo() {
    val groupsWithCharSets = groups.map {
        it.split("\n").map { it.toSet() }
    }

    val sum = groupsWithCharSets.sumBy { group ->
        group.reduce(Set<Char>::intersect).count()
    }
    println(sum)
}

fun main() {
    partOne()
    partTwo()
}