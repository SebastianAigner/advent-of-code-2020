package day8

import java.io.File

val defaultInput = File("inputs/day8.txt").readLines().map {
    val (instr, value) = it.split(" ")
    Instruction(instr, value.toInt())
}

data class Instruction(var opcode: String, val value: Int)

fun executeCode(instructions: List<Instruction>): Boolean {
    var ip = 0
    var acc = 0
    val encounteredIndices = mutableListOf<Int>()
    while (true) {
        if (ip in encounteredIndices) {
            println(acc)
            return false
        }
        encounteredIndices += ip
        println(acc)
        val currInst = instructions.getOrNull(ip) ?: return true // program terminates
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

fun main() {
    println("terminates=" + executeCode(defaultInput))
    repeat(defaultInput.size) {
        val thisInput = defaultInput.toList().map { it.copy() }
        val instructionAtIndex = thisInput[it]
        if (instructionAtIndex.opcode == "jmp") {
            instructionAtIndex.opcode = "nop"
        } else if (instructionAtIndex.opcode == "nop") {
            instructionAtIndex.opcode = "jmp"
        }
        if (executeCode(thisInput)) {
            println("DONE")
            return
        }
    }
}