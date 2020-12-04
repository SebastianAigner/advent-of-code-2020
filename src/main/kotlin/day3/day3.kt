package day3

import java.io.File

val rows = File("inputs/day3.txt").readLines()

fun String.getRepeating(idx: Int) = this[idx % this.length]

val Char.treeCount get() = if (this == '#') 1 else 0

fun main() {
    partOne()
    partTwo()
}

fun partOne() {
    println(evaluateSlope(3, 1))
}

fun partTwo() {
    val slopes = listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)

    val res = slopes.fold(1L) { acc, pair ->
        acc * evaluateSlope(pair.first, pair.second)
    }
    println(res)
}

fun evaluateSlope(right: Int, down: Int): Int {
    var idx = 0
    var trees = 0

    for (r in rows.chunked(down) { it.first() }) {
        trees += r.getRepeating(idx).treeCount
        idx += right
    }
    return trees
}
