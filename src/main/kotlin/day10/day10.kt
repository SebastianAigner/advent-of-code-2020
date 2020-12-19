package day10

import java.io.File

val adapters = File("inputs/day10.txt").readLines().map { it.toInt() }.sorted()

fun main() {
    val withOutletAndBuiltIn = listOf(0) + adapters + listOf(adapters.last() + 3)
    val differences = withOutletAndBuiltIn.zipWithNext { a, b -> b - a }
    println(differences.count { it == 1 } * differences.count { it == 3 })
}