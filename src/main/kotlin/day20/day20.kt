package day20

import java.io.File

val tiles = File("inputs/day20.txt").readText().trim().split("\n\n").map {
    val lines = it.lines()
    val id = lines.first().removePrefix("Tile ").removeSuffix(":").toInt()
    val tileContents = lines.drop(1)
    id to tileContents
}.toMap()

fun List<String>.flipX(): List<String> {
    return this.reversed()
}

fun List<String>.flipY(): List<String> {
    return this.map { it.reversed() }
}

@ExperimentalStdlibApi
fun List<String>.rot90(): List<String> {
    val newLines = mutableListOf<String>()
    repeat(this.count()) {
        newLines += ""
    }
    for (line in this.withIndex()) {
        for (char in line.value.withIndex()) {
            newLines[char.index] = "${char.value}" + newLines[char.index]
        }
    }
    return newLines
}

@ExperimentalStdlibApi
fun List<String>.rot180(): List<String> {
    return this.rot90().rot90()
}

@ExperimentalStdlibApi
fun List<String>.rot270(): List<String> {
    return this.rot90().rot90().rot90()
}

fun List<String>.identity(): List<String> {
    return this
}

@OptIn(ExperimentalStdlibApi::class)
val rotationOperations: List<(List<String>) -> List<String>> = listOf(
    List<String>::identity,
    List<String>::rot90,
    List<String>::rot180,
    List<String>::rot270
)

val flipOperations: List<(List<String>) -> List<String>> = listOf(
    List<String>::identity,
    List<String>::flipX,
    List<String>::flipY
)

fun compose(a: (List<String>) -> List<String>, b: (List<String>) -> List<String>): (List<String>) -> List<String> {
    return {
        a(b(it))
    }
}

fun List<String>.matchesBottomConnector(other: List<String>): Boolean {
    val bottom = this.last()
    val otherTop = other.first()
    return bottom == otherTop
}

fun List<String>.matchesTopConnector(other: List<String>): Boolean {
    val top = this.first()
    val otherBottom = other.last()
    return top == otherBottom
}

fun List<String>.matchesRightConnector(other: List<String>): Boolean {
    val right = this.map { it.last() }.joinToString("")
    val otherLeft = other.map { it.first() }.joinToString("")
    return right == otherLeft
}

fun List<String>.matchesLeftConnector(other: List<String>): Boolean {
    val left = this.map { it.first() }.joinToString("")
    val otherRight = other.map { it.last() }.joinToString("")
    return left == otherRight
}

val allOperationPermutations: List<(List<String>) -> List<String>> = run {
    val permuts = mutableListOf<(List<String>) -> List<String>>()
    for (rotOp in rotationOperations) {
        for (flipOp in flipOperations) {
            permuts.add(compose(rotOp, flipOp))
        }
    }
    permuts.toList()
}

fun List<String>.inAllPermuts(): List<List<String>> {
    return allOperationPermutations.map {
        it(this)
    }
}

typealias Square = List<String>

@ExperimentalStdlibApi
fun main() {

}

