package day5

import java.io.File

val tickets = File("inputs/day5.txt").readLines().map(::parseTicket).sorted()

fun main() {
    println(tickets.last())
    println(tickets.windowed(2).first { it[0] + 1 != it[1] }[0] + 1)
}

// Row & Col are binary-encoded with F,L=0 and B,R=1
fun parseTicket(t: String): Int {
    val row = t.take(7)
        .replace("F", "0")
        .replace("B", "1")
        .toInt(2)

    val col = t.takeLast(3)
        .replace("L", "0")
        .replace("R", "1")
        .toInt(2)
    return row * 8 + col
}