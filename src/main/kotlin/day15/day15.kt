package day15

import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@ExperimentalTime
fun main() {
    val previousTurns = mutableListOf(0, 5, 4, 1, 10, 14, 7)
    println(measureTimedValue { run(previousTurns, 2020) })
    println(measureTimedValue { run(previousTurns, 30_000_000) })
}

fun run(input: List<Int>, repeats: Int): Int {
    val previousTurns = input.toMutableList()
    val previouslyEncounteredNumbers = input.toMutableSet()
    repeat(repeats - previousTurns.count()) {
        if (it % 1_000_000 == 0) println("$it / 30M done")
        val lastNumber = previousTurns.last()
        previousTurns += if (lastNumber !in previousTurns.dropLast(1)) {
            // number has not been spoken before
            0
        } else {
            // number has been spoken before
            val lastPreviouslySpoken = previousTurns.dropLast(1).indexOfLast { it == lastNumber }
            previousTurns.lastIndex - lastPreviouslySpoken
        }
//        println(previousTurns.withIndex().joinToString { "[${it.index+1}] ${it.value}"})
    }
    return (previousTurns.last())
}