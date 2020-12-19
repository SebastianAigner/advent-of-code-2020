package day11

import java.io.File

val outmap = File("inputs/day11.txt").readLines().mapIndexed { lineNumber, lineText ->
    lineText.mapIndexed { columnNumber, character ->
        character
    }
}

@ExperimentalStdlibApi
fun main() {
    println(outmap[0][0])

    val testMap = """
        L.LL.LL.LL
        LLLLLLL.LL
        L.L.L..L..
        LLLL.LL.LL
        L.LL.LL.LL
        L.LLLLL.LL
        ..L.L.....
        LLLLLLLLLL
        L.LLLLLL.L
        L.LLLLL.LL
""".trimIndent().lines().mapIndexed { lineNumber, lineText ->
        lineText.mapIndexed { columnNumber, character ->
            character
        }
    }

//    partOne()
//    partTwo()
    println(countInQueensMoves(testMap, 0, 0, 'L'))
    println(partTwo())
}

@ExperimentalStdlibApi
fun partOne() {
    var newMap = outmap
    var prevMap = outmap
    do {
        prevMap = newMap
        newMap = iterate(newMap)
    } while (newMap != prevMap)
    val stableOccupied = newMap.sumBy {
        it.count { it == '#' }
    }
    println(stableOccupied)
}

@ExperimentalStdlibApi
fun partTwo(mappp: List<List<Char>> = outmap) {
    var newMap = mappp
    var prevMap = mappp
    do {
        prevMap = newMap
        newMap = iteratePart2(newMap)
        println("----")
        println(newMap.joinToString("\n") { it.joinToString("") })
        println("----")
    } while (newMap != prevMap)
    val stableOccupied = newMap.sumBy {
        it.count { it == '#' }
    }
    println("part2 $stableOccupied")
}

@ExperimentalStdlibApi
fun iterate(map: List<List<Char>>): List<List<Char>> {
    //TODO DEEP COPY MAP
    return map.mapIndexed { row, rowContent ->
        rowContent.mapIndexed { column, character ->
            when (character) {
                '.' -> {
                    '.'
                } // do nothing on floor tiles
                'L' -> {
                    if (countInNeighborhood(map, column, row, '#') == 0) {
                        // there are no occupied neighbors
                        '#'
                    } else {
                        'L'
                    }
                } // check if no occupied seats are adjacent => occupied
                '#' -> {
                    if (countInNeighborhood(map, column, row, '#') >= 4) {
                        'L'
                    } else {
                        '#'
                    }
                } // check if >=4 seats are occupied => empty
                else -> error("unknown character $character")
            }
        }
    }
}

@ExperimentalStdlibApi
fun countInNeighborhood(map: List<List<Char>>, x: Int, y: Int, char: Char): Int {
    val neighbors = buildList {
        for (offsetY in -1..1) {
            for (offsetX in -1..1) {
                if (offsetX == 0 && offsetY == 0) continue
                add(offsetX to offsetY)
            }
        }
    }
    println(neighbors)
    return neighbors.mapNotNull {
        val charAt = map.getOrNull(y + it.first)?.getOrNull(x + it.second)
        charAt
    }.also(::println).count {
        it == char
    }
}


@ExperimentalStdlibApi
fun iteratePart2(map: List<List<Char>>): List<List<Char>> {
    return map.mapIndexed { row, rowContent ->
        rowContent.mapIndexed { column, character ->
            when (character) {
                '.' -> {
                    '.'
                } // do nothing on floor tiles
                'L' -> {
                    if (countInQueensMoves(map, column, row, '#') == 0) {
                        // there are no occupied neighbors
                        '#'
                    } else {
                        'L'
                    }
                } // check if no occupied seats are adjacent => occupied
                '#' -> {
                    if (countInQueensMoves(map, column, row, '#') >= 5) {
                        'L'
                    } else {
                        '#'
                    }
                } // check if >=4 seats are occupied => empty
                else -> error("unknown character $character")
            }
        }
    }
}


@OptIn(ExperimentalStdlibApi::class)
fun countInQueensMoves(map: List<List<Char>>, x: Int, y: Int, char: Char): Int {
    val directionDeltas = buildList {
        for (offsetY in -1..1) {
            for (offsetX in -1..1) {
                if (offsetX == 0 && offsetY == 0) continue
                add(offsetX to offsetY)
            }
        }
    }
    var sumsum = 0
    for ((dy, dx) in directionDeltas) {
        var xOff = 0
        var yOff = 0
        while (true) {
            xOff += dx
            yOff += dy
            val charAt = map.getOrNull(y + yOff)?.getOrNull(x + xOff)
            if (charAt == null) { // found border of the map in this direction, no need
                break
            }
            if (charAt == char) {
                sumsum += 1
                break
            }
            if (charAt == '.') {
                //we continue looking
                continue
            }
            break // we're looking at a chair of the wrong type
        }
    }
    return sumsum
}