package day18

import java.io.File

val input = File("inputs/day18.txt").readLines()

enum class CurrentAction {
    MULTIPLY,
    ADD
}

fun main() {
    val res = input.fold(0L) { acc, str ->
        acc + parseFullExpression(str)
    }
    println(res)


    val res2 = input.fold(0L) { acc, str ->
        acc + parseFullExpressionPart2(str)
    }
    println(res2)
}

fun parseFullExpression(expr: String): Long {
    var res = expr
    do {
        res = parseOneLayer(res)
        println(res)
    } while (res.toLongOrNull() == null)
    return res.toLong()
}

fun parseFullExpressionPart2(expr: String): Long {
    var res = expr
    do {
        res = parsePart2Layer(res)
        println(res)
    } while (res.toLongOrNull() == null)
    return res.toLong()
}

fun parseOneLayer(oneExpr: String): String {

    fun evaluatePlainExpression(expr: String): Long {
        var acc = 0L
        var currentAction = CurrentAction.ADD
        for (elem in expr.split(" ")) {
            elem.toLongOrNull()?.let { integerValue ->
                when (currentAction) {
                    CurrentAction.MULTIPLY -> acc *= integerValue
                    CurrentAction.ADD -> acc += integerValue
                }
            }
            when (elem) {
                "+" -> {
                    currentAction = CurrentAction.ADD
                }
                "*" -> {
                    currentAction = CurrentAction.MULTIPLY
                }
            }
        }
        return acc
    }
    if (!oneExpr.contains("(")) {
        return evaluatePlainExpression(oneExpr).toString()
    }
    val parenExpressionWithoutChildren = """\((\d+( [+*] \d+)*)\)""".toRegex()
    return parenExpressionWithoutChildren.replace(oneExpr) {
        val myStr = it.destructured.component1()
        println("Evaluating expression $myStr")
        evaluatePlainExpression(myStr).toString()
    }
}

fun parsePart2Layer(oneExpr: String): String {

    fun evaluatePlainExpression(fakeExpr: String): Long {

        var realExpr = fakeExpr
        while (realExpr.contains("+")) {
            // we need to apply addition of two constants first
            val additionRegex = """(\d+) [+] (\d+)""".toRegex()
            realExpr = additionRegex.replace(realExpr) {
                val (a, b) = it.destructured
                (a.toLong() + b.toLong()).toString()
            }
            println("expr now $realExpr")
        }
        println("$realExpr only contains multiplications")
        return realExpr.split(" * ").map {
            it.toLong()
        }.fold(1L) { acc, l ->
            acc * l
        }
    }
    if (!oneExpr.contains("(")) {
        return evaluatePlainExpression(oneExpr).toString()
    }
    val parenExpressionWithoutChildren = """\((\d+( [+*] \d+)*)\)""".toRegex()
    return parenExpressionWithoutChildren.replace(oneExpr) {
        val myStr = it.destructured.component1()
        println("Evaluating expression $myStr")
        evaluatePlainExpression(myStr).toString()
    }
}