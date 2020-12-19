package day9

import java.io.File

val numbers = File("inputs/day9.txt").readLines().map { it.toLong() }

fun main() {
    val offendingNumber = numbers
        .windowed(26)
        .asSequence()
        .mapNotNull {
            // 25 numbers are candidates
            // last number is what we're checking
            val hasDiff = hasDifferingSumIn(it.last(), it.dropLast(1))
            if (!hasDiff) {
                return@mapNotNull it.last()
            }
            null
        }.first()
    println(offendingNumber)

    val numsWithoutOffending: List<Long> = numbers - offendingNumber
    for (i in 1..numsWithoutOffending.size) {
        println("checking window size $i")
        val theseWindows = numsWithoutOffending.windowed(i)
        for (window in theseWindows) {
            val sumOfWindow = window.reduce { acc, l -> acc + l }
            if (sumOfWindow == offendingNumber) {
                println(window.minOf { it } + window.maxOf { it })
                return
            }
        }
    }
}

fun hasDifferingSumIn(number: Long, candidates: List<Long>): Boolean {
    for (candidate in candidates) {
        val otherCandidate = number - candidate
        if (otherCandidate < 0 || otherCandidate == candidate) continue
        if (otherCandidate in candidates) {
            // we have ensured that we're not accidentally getting ourself back in the previous condition
            println("$number is $candidate + $otherCandidate")
            return true
        }
    }
    return false
}