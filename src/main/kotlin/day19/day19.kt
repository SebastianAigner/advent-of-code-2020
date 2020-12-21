package day19

import java.io.File


val rules = File("inputs/day19.txt").readLines().takeWhile { it.isNotBlank() }.sorted().map {
    val (id, allRules) = it.split(": ")

    id.toInt() to allRules
}.toMap()

val testStrings = File("inputs/day19.txt").readLines().takeLastWhile { it.isNotBlank() }

fun main() {
    partOne()
    partTwo()
}

fun partTwo() {
    val pumpedRegexes = mutableListOf<Regex>()
    for (pump42 in 0..10) {
        for (pump4231 in 0..10) {
            val adjustedRules = rules
                .toMutableMap().apply {
                    val string42 = (0..pump42).map { "42" }.joinToString(" ")
                    set(8, string42 /* with [42] being pumped */)

                    val fortyTwos = (0..pump4231).map { "42" }
                    val thirtyOnes = (0..pump4231).map { "31" }
                    val x = (fortyTwos + thirtyOnes).joinToString(" ")
                    set(11, "42 31 | $x" /* with [42 31] being pumped*/)
                }.toMap()
            val aRegex = adjustedRules.parseSingleRule(adjustedRules[0]!!)
            println(aRegex)
            pumpedRegexes.add(aRegex.toRegex())
        }
    }
    println(testStrings.count { testString ->
        pumpedRegexes.any { it.matches(testString) }
    })
}

fun partOne() {
    val regex = rules.parseSingleRule(rules[0]!!)
    println(regex)
    val tester = regex.toRegex()
    println(testStrings.count {
        tester.matches(it)
    })
}

fun Map<Int, String>.parseSingleRule(singleRule: String): String {
    if (singleRule.contains("|")) {
        // we OR the left and right parts together
        val (left, right) = singleRule.split(" | ")
        return "(${parseSingleRule(left)}|${parseSingleRule(right)})"
    }
    if (singleRule.contains(" ")) {
        // it's a concat rule
        val rules = singleRule.split(" ")
        return rules.joinToString("") { parseSingleRule(it) }
    }
    if (singleRule.contains("\"")) {
        // it's a single-char rule
        return singleRule.removeSurrounding("\"")
    }
    // it's an alias rule
    return parseSingleRule(this[singleRule.toInt()]!!)
}