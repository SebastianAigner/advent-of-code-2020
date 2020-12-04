package day4

import java.io.File

val passports = File("inputs/day4.txt").readText().split("\n\n").map {
    it.replace("\n", " ").trim()
}

val requiredFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid" /*"cid"*/)

fun main() {
    partOne()
    partTwo()
}

fun String.hasAllRequiredFields(): Boolean {
    val fieldOnlyPassport = this.split(" ").map {
        it.takeWhile { it != ':' }
    }
    return fieldOnlyPassport.containsAll(requiredFields)
}

fun partOne() {
    println(passports.count { it.hasAllRequiredFields() })
}

fun partTwo() {
    val allFieldPassports = passports.filter { it.hasAllRequiredFields() }
    println(allFieldPassports.count { it.hasValidValues() })
}

fun String.isDigits(n: Int): Boolean {
    return this.matches("""\d{$n}""".toRegex())
}

private fun String.hasValidValues(): Boolean {
    val dataMap = this.split(" ").map {
        val (key, value) = it.split(":")
        key to value
    }.toMap()

    val evaluatedFields = with(dataMap) {
        listOf(
            validate("byr") { it.isDigits(4) && it.toInt() in 1920..2002 },
            validate("iyr") { it.isDigits(4) && it.toInt() in 2010..2020 },
            validate("eyr") { it.isDigits(4) && it.toInt() in 2020..2030 },
            validate("hgt") {
                it.endsWith("cm") && it.removeSuffix("cm").toInt() in 150..193
                        || it.endsWith("in") && it.removeSuffix("in").toInt() in 59..76
            },
            validate("hcl") { it matches """#[0-9a-f]{6}""".toRegex() },
            validate("ecl") { it in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth") },
            validate("pid") { it.isDigits(9) }
        )
    }
    return !evaluatedFields.any { valid -> !valid }
}

fun Map<String, String>.validate(key: String, validator: (String) -> Boolean): Boolean {
    return validator(this.getValue(key))
}