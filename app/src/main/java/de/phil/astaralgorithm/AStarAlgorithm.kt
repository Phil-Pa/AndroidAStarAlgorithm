package de.phil.astaralgorithm

import de.phil.astaralgorithm.datastructure.Array2D
import de.phil.astaralgorithm.datastructure.Heap
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.sqrt
import kotlin.random.Random

class AStarAlgorithm(private val grid: Array2D<Tile>) {

    private val neighborBuffer = Array<Tile?>(8) { null }
    private lateinit var path: List<Tile>

    private val size by lazy {
        sqrt(grid.length.toFloat()).toInt()
    }

    private fun getNeighbors(tile: Tile, diagonal: Boolean): Int {
        var index = 0

        for (x in -1..1) {
            for (y in -1..1) {
                if (x == 0 && y == 0)
                    continue

                val checkX = tile.gridX + x
                val checkY = tile.gridY + y

                if (checkX < 0 || checkX >= size || checkY < 0 || checkY >= size)
                    continue

                if (diagonal)
                    neighborBuffer[index++] = grid.get(checkX, checkY)
                else {
                    val distX = abs(tile.gridX - checkX)
                    val distY = abs(tile.gridY - checkY)

                    if (hypot(distX.toFloat(), distY.toFloat()) <= 1)
                        neighborBuffer[index++] = grid.get(checkX, checkY)
                }
            }
        }

        return index
    }

    private fun findTile(type: TileType): Tile {
        //TODO: make iterator work
//        for (tile in grid)
//            if (tile.type == type)
//                return tile

        for (x in 0 until grid.sizeX)
            for (y in 0 until grid.sizeY) {
                val tile = grid.get(x, y)
                if (tile.type == type)
                    return tile
            }


        throw IllegalArgumentException("tile type $type not found in tiles")
    }

    fun findPath(diagonal: Boolean = true): List<Tile> {

        val startTile = findTile(TileType.START)
        val endTile = findTile(TileType.END)

        val openSet = Heap<Tile>(grid.length)
        val closedSet = Heap<Tile>(grid.length)
        openSet.add(startTile)

        while (openSet.size > 0) {
            val currentTile = openSet.removeFirst()
            closedSet.add(currentTile)

            if (currentTile == endTile) {
                retracePath(startTile, endTile)
                return path
            }

            val neighborsLength = getNeighbors(currentTile, diagonal)
            for (i in 0 until neighborsLength) {
                val neighbor = neighborBuffer[i]!!
                if (!neighbor.isWalkable || neighbor in closedSet)
                    continue

                val newMovementCostToNeighbour = currentTile.gCost + getDistance(currentTile, neighbor)
                if (newMovementCostToNeighbour >= neighbor.gCost && neighbor in openSet)
                    continue

                neighborBuffer[i]!!.gCost = newMovementCostToNeighbour
                neighbor.hCost = getDistance(neighbor, endTile)
                neighbor.parent = currentTile

                if (neighbor !in openSet)
                    openSet.add(neighbor)
                else
                    openSet.updateItem(neighbor)
            }
        }

        throw IllegalArgumentException("could not find path")
    }

    private fun retracePath(startTile: Tile, endTile: Tile) {
        val temp = mutableListOf<Tile>()
        var currentTile = endTile

        while (currentTile != startTile) {
            if (currentTile.type != TileType.START && currentTile.type != TileType.END)
                currentTile.type = TileType.PATH

            temp.add(currentTile)
            currentTile = currentTile.parent
        }

        temp.reverse()
        path = temp
    }

    companion object {

        fun createGrid(size: Int, percentWalls: Int): Array2D<Tile> {
            val grid = Array2D<Tile>(size, size)
            for (x in 0 until size) {
                for (y in 0 until size) {
                    val type = if (Random.nextInt(1, 101) <= percentWalls) TileType.WALL else TileType.WALKABLE
                    grid.set(x, y, Tile(type, x, y))
                }
            }

            grid.get(0, 0).type = TileType.START
            grid.get(grid.sizeX - 1, grid.sizeY - 1).type = TileType.END

            return grid
        }

        private fun getDistance(tileA: Tile, tileB: Tile): Int {
            val distX = abs(tileA.gridX - tileB.gridX)
            val distY = abs(tileA.gridY - tileB.gridY)

            if (distX > distY)
                return 14 * distY + 10 * (distX - distY)
            return 14 * distX + 10 * (distY - distX)
        }

    }
}