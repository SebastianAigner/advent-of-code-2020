package day3

import java.io.File

val rows = File("inputs/day3.txt").readLines()

fun String.getRepeating(idx: Int): Char {
    return this[idx % this.length]
}

val Char.isTree get() = this == '#'

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
        val slo = evaluateSlope(pair.first, pair.second)
        println(slo)
        acc * slo
    }
    println(res)
}

fun evaluateSlope(right: Int, down: Int): Int {
    var idx = 0
    var trees = 0
    for (r in rows.withIndex()) {
        if (r.index % down != 0) continue
        if (r.value.getRepeating(idx).isTree) trees++
        idx += right
    }
    return trees
}
