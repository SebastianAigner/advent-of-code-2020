package day2

import java.io.File

val entries = File("inputs/day2.txt").readLines().map {
    val (rules, password) = it.split(": ")
    rules to password
}

data class Rule(val a: Int, val b: Int, val letter: Char) {
    companion object {
        fun fromString(s: String): Rule {
            val (rangeText, letterText) = s.split(" ")
            val (minText, maxText) = rangeText.split("-")
            val letter = letterText.first()
            val min = minText.toInt()
            val max = maxText.toInt()
            return Rule(min, max, letter)
        }
    }

    fun validatePartOne(password: String): Boolean {
        return password.count { it == letter } in a..b
    }

    fun validatePartTwo(password: String): Boolean {
        val isFirstPosition = password.getOrNull(a - 1) == letter
        val isSecondPosition = password.getOrNull(b - 1) == letter
        if (isFirstPosition || isSecondPosition) {
            if (!(isFirstPosition && isSecondPosition)) {
                return true
            }
        }
        return false
    }
}

fun main() {
    entries.count { Rule.fromString(it.first).validatePartOne(it.second) }.run(::println)
    entries.count { Rule.fromString(it.first).validatePartTwo(it.second) }.run(::println)
}