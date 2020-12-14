package day8

import java.io.File

val input = File("inputs/day8.txt").readLines().map {
    val (instr, value) = it.split(" ")
    Instruction(instr, value.toInt())
}

data class Instruction(val opcode: String, val value: Int)

fun main() {
    var ip = 0
    var acc = 0
    val encounteredIndices = mutableListOf<Int>()
    while (true) {
        if (ip in encounteredIndices) {
            println(acc)
            break
        }
        encounteredIndices += ip
        val currInst = input[ip]
        when (currInst.opcode) {
            "nop" -> {
                ip++
            }
            "acc" -> {
                acc += currInst.value; ip++
            }
            "jmp" -> {
                ip += currInst.value
            }
        }
    }
}