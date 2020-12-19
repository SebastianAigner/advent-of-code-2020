package day12

import java.io.File
import kotlin.math.absoluteValue

val instructions = File("inputs/day12.txt").readLines().map {
    it.first() to it.drop(1).toInt()
}

enum class Heading(val deg: Int, val xOff: Int, val yOff: Int) {
    NORTH(0, 0, 1),
    EAST(90, 1, 0),
    SOUTH(180, 0, -1),
    WEST(270, -1, 0);

    fun turn(amount: Int): Heading {
        var newDegrees = (this.deg + amount) % 360
        if (newDegrees < 0) {
            newDegrees += 360
        }
        println(newDegrees)
        println(this)
        return Heading.values().first { it.deg == newDegrees }
    }
}

fun main() {
    val instructions = """
        F10
        N3
        F7
        R90
        F11
    """.trimIndent().lines().map {
        it.first() to it.drop(1).toInt()
    }
    partOne()
    partTwo()
}

private fun partOne() {
    var x = 0
    var y = 0
    var heading = Heading.EAST
    for ((inst, value) in instructions) {
        println("$inst $value")
        when (inst) {
            'N' -> {
                y += value
            }
            'S' -> {
                y -= value
            }
            'E' -> {
                x += value
            }
            'W' -> {
                x -= value
            }
            'L' -> {
                heading = heading.turn(-value)
            }
            'R' -> {
                heading = heading.turn(value)
            }
            'F' -> {
                x += heading.xOff * value
                y += heading.yOff * value
            }
        }
    }
    println(x.absoluteValue + y.absoluteValue)
}

fun partTwo(instrs: List<Pair<Char, Int>> = instructions) {
    var x = 0L
    var y = 0L
    var wayX = 10L
    var wayY = 1L
    for ((inst, value) in instrs) {
        println("$inst $value")
        when (inst) {
            'N' -> {
                wayY += value
            }
            'S' -> {
                wayY -= value
            }
            'E' -> {
                wayX += value
            }
            'W' -> {
                wayX -= value
            }
            'L' -> {
                //counter clockwise rotation
                repeat(value / 90) {
                    val x2 = -wayY
                    val y2 = wayX
                    wayX = x2
                    wayY = y2
                }
            }
            'R' -> {
                //clockwise rotation
                repeat(value / 90) {
                    val x2 = wayY
                    val y2 = -wayX
                    wayX = x2
                    wayY = y2
                }
            }
            'F' -> {
                x += wayX * value
                y += wayY * value
            }
        }
        println("$x $y waypoint $wayX $wayY")
    }
    println(x.absoluteValue + y.absoluteValue)
}