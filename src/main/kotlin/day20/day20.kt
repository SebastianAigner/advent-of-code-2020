package day20

import java.io.File
import kotlin.math.sqrt
import kotlin.system.exitProcess

data class Tile(val id: Int, val content: List<String>) {
    fun flipX(): Tile {
        return copy(content = this.content.reversed())
    }

    fun flipY(): Tile {
        return copy(content = content.map { it.reversed() })
    }

    fun rot90(): Tile {
        val newLines = mutableListOf<String>()
        repeat(this.content.count()) {
            newLines += ""
        }
        for (line in this.content.withIndex()) {
            for (char in line.value.withIndex()) {
                newLines[char.index] = "${char.value}" + newLines[char.index]
            }
        }
        return copy(content = newLines)
    }

    fun rot180(): Tile {
        return this.rot90().rot90()
    }

    fun rot270(): Tile {
        return this.rot180().rot90()
    }

    fun identity(): Tile {
        return this
    }


    val rotationOperations: List<(Tile) -> Tile> = listOf(
        Tile::identity,
        Tile::rot90,
        Tile::rot180,
        Tile::rot270
    )

    val flipOperations: List<(Tile) -> Tile> = listOf(
        Tile::identity,
        Tile::flipX,
        Tile::flipY
    )

    fun matchesBottomConnector(other: Tile): Boolean {
        val bottom = this.content.last()
        val otherTop = other.content.first()
        return bottom == otherTop
    }

    fun matchesTopConnector(other: Tile): Boolean {
        val top = this.content.first()
        val otherBottom = other.content.last()
        return top == otherBottom
    }

    fun matchesRightConnector(other: Tile): Boolean {
        val right = this.content.map { it.last() }.joinToString("")
        val otherLeft = other.content.map { it.first() }.joinToString("")
        return right == otherLeft
    }

    fun matchesLeftConnector(other: Tile): Boolean {
        val left = this.content.map { it.first() }.joinToString("")
        val otherRight = other.content.map { it.last() }.joinToString("")
        return left == otherRight
    }

    val allOperationPermutations = run {
        val permuts = mutableListOf<(Tile) -> Tile>()
        for (flipOp in flipOperations) {
            for (rotOp in rotationOperations) {
                permuts.add(compose(rotOp, flipOp))
            }
        }
        permuts.toList()
    }

    fun inAllPermutations(): List<Tile> {
        return allOperationPermutations.map {
            it(this)
        }
    }

    // goes through all permutation and sees if one of them is fine with the neighborhood
    // i.e. matches the top and left connectors.
    // this assumes a flood fill from top-left to top-right, then to the bottom.
    fun matchesInNeighborhood(top: Tile?, left: Tile?): List<Tile> {
        val matchingPermutation = inAllPermutations().filter { currentPermutation ->
            val isTopOk = top == null || currentPermutation.matchesTopConnector(top)
            val isLeftOk = left == null || currentPermutation.matchesLeftConnector(left)
            isTopOk && isLeftOk
        }
        return matchingPermutation
    }
}

val tiles = File("inputs/day20.txt").readText().trim().split("\n\n").map {
    val lines = it.lines()
    val id = lines.first().removePrefix("Tile ").removeSuffix(":").toInt()
    val tileContents = lines.drop(1)
    val tile = Tile(id, tileContents)
    //println(tile)
    tile
}


fun compose(a: (Tile) -> Tile, b: (Tile) -> Tile): (Tile) -> Tile {
    return {
        a(b(it))
    }
}

@ExperimentalStdlibApi
fun main() {
    newFill(Grid(mapOf()), tiles)
    println(length)
}

fun newFill(grid: Grid, candidateTiles: List<Tile>) {
//    println(candidateTiles.map { it.id }.joinToString(", "))
//    println(grid.content.map { it.value.id }.joinToString())
    val firstOpenSquare = grid.getFirstOpenCoordinate()
//    println("First open at $firstOpenSquare")
    if (firstOpenSquare == null || candidateTiles.count() == 0) {
        println(grid)
        grid.getCornerValues()
        exitProcess(0)
        return
    }
    // let's find things that fit.
    val above = grid.getAbove(firstOpenSquare)
    val left = grid.getLeft(firstOpenSquare)
    for (candidate in candidateTiles) {
        val fittingPermutationsForCandidate = candidate.matchesInNeighborhood(above, left)
        for (permutatedTile in fittingPermutationsForCandidate) {
            // they fit, so we put them in the grid, and run the whole show again
            val newGrid = grid.add(firstOpenSquare, permutatedTile)
            // but first we remove the candidate
            val newCandidates = candidateTiles.filter { it.id != permutatedTile.id }
            newFill(newGrid, newCandidates)
        }
    }
}

fun floodFill(grid: Grid, candidateTiles: List<Tile>) {
    println("${candidateTiles.count()} remaining")

    val open = grid.getFirstOpenCoordinate()
    println(open)
    if (open == null || candidateTiles.count() == 0) {
        println(grid)
        grid.getCornerValues()
        System.out.flush()
        return // the grid has been filled.
    }
    open.let { openSquare ->
        val above = grid.getAbove(openSquare)
        val left = grid.getLeft(openSquare)
        // let's see if we can find a tile that matches here.
        for (candidate in candidateTiles) {
            val matches = candidate.matchesInNeighborhood(above, left)
            // matches is the number of permutations which fit this place
            for (workingMatch in matches) {
                // recursively flood fill the rest with each working match (there shouldn't be too many?)
                val newGrid = grid.add(openSquare, workingMatch)
                val newCandidates = candidateTiles.filter { it.id != workingMatch.id }
                floodFill(newGrid, newCandidates)
            }
        }
        if (candidateTiles.count() == 0) {
            println("no matching candidate tiles at all. fuck?")
        }
    }
}

data class Vec2(val x: Int, val y: Int)

val length = sqrt(tiles.count().toDouble()).toInt()

data class Grid(val content: Map<Vec2, Tile>) {
    fun add(v: Vec2, tile: Tile): Grid {
        val mut = content.toMutableMap()
        mut[v] = tile
        return Grid(mut)
    }

    fun getAbove(v: Vec2): Tile? {
        return content[Vec2(v.x, v.y - 1)]
    }

    fun getLeft(v: Vec2): Tile? {
        return content[Vec2(v.x - 1, v.y)]
    }

    fun getFirstOpenCoordinate(): Vec2? {
        for (y in 0 until length) {
            for (x in 0 until length) {
//                println(content[Vec2(x, y)])
                if (content[Vec2(x, y)] == null) {
                    return Vec2(x, y)
                }
            }
        }
        return null
    }

    fun getCornerValues() {
        println(content[Vec2(0, 0)])
        println(content[Vec2(0, length - 1)])
        println(content[Vec2(length - 1, 0)])
        println(content[Vec2(length - 1, length - 1)])
    }
}