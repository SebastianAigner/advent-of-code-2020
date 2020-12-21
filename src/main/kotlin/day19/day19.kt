package day19

import java.io.File


val rules = File("inputs/day19.txt").readLines().takeWhile { it.isNotBlank() }.sorted().map {
    val (id, allRules) = it.split(": ")

    id.toInt() to allRules
}.toMap()

val testStrings = File("inputs/day19.txt").readLines().takeLastWhile { it.isNotBlank() }

fun main() {
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
        val (first, second) = singleRule.split(" ")
        return "${parseSingleRule(first)}${parseSingleRule(second)}"
    }
    if (singleRule.contains("\"")) {
        // it's a single-char rule
        return singleRule.removeSurrounding("\"")
    }
    // it's an alias rule
    return parseSingleRule(this[singleRule.toInt()]!!)
}