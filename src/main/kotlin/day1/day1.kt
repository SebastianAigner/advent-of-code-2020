package day1

import java.io.File

val numbers = File("inputs/day1.txt").readLines().map(String::toInt)

fun main() {
    partOne()
    partTwo()
}

fun partOne() {
    for (first in numbers) {
        for (second in numbers) {
            if (first + second == 2020) println(first * second)
        }
    }
}

fun partTwo() {
    for (first in numbers) {
        for (second in numbers) {
            if (first + second >= 2020) continue
            for (third in numbers) {
                if (first + second + third == 2020) println(first * second * third)
            }
        }
    }
}