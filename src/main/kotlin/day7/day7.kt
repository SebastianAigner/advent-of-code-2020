package day7

import java.io.File

val rules = File("inputs/day7.txt").readLines().map {
    val (key, value) = it.split(" contain ")
    key.removeSuffix(" bags") to value.removeSuffix(".").split(", ").map {
        it.split(" ").drop(1).dropLast(1).joinToString(" ")
    }
}

val rules2 = File("inputs/day7.txt").readLines().map {
    val (key, value) = it.split(" contain ")
    key.removeSuffix(" bags") to value.removeSuffix(".").split(", ").map {
        val (number, colorModifier, colorName) = it.split(" ")
        val num = if (number == "no") 0 else number.toInt()
        BagDescription(num.toInt(), "$colorModifier $colorName")
    }
}.toMap()

data class BagDescription(val number: Int, val color: String)

fun main() {
    partOne()
    println(rules2)
    partTwo()
}

fun partOne() {
    foo(rules, listOf("shiny gold"), 0)
}

fun partTwo() {
    val shinyGold = rules2.getValue("shiny gold")
    println(shinyGold)

    fun addBags(bags: List<BagDescription>): Int {
        val directBagNums = bags.sumBy { it.number }
        val childBags = bags.sumBy {
            if (it.number == 0) {
                0
            } else {
                it.number * addBags(rules2.getValue(it.color))
            }
        }
        return directBagNums + childBags
    }

    println(addBags(shinyGold))
}

fun foo(restCandidates: List<Pair<String, List<String>>>, directHolderColors: List<String>, acc: Int) {
    val nextDegreeHolders = restCandidates.filter { it.second.any { it in directHolderColors } }
    val remCandidates = restCandidates - nextDegreeHolders

    println(nextDegreeHolders.count())
    if (nextDegreeHolders.count() == 0) {
        println("done! $acc")
        return
    }
    foo(remCandidates, nextDegreeHolders.map { it.first }, acc + nextDegreeHolders.count())
}
