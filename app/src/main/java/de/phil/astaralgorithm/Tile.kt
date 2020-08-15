package de.phil.astaralgorithm

import de.phil.astaralgorithm.datastructure.HeapItem

class Tile(var type: TileType, val gridX: Int, val gridY: Int, override var heapIndex: Int = 0) :
    HeapItem<Tile> {

    lateinit var parent: Tile

    var gCost = 0
    var hCost = 0

    val isWalkable: Boolean
        get() = type != TileType.WALL && type != TileType.PATH

    private val fCost: Int
        get() = gCost + hCost

    override fun compareTo(other: HeapItem<Tile>): Int {
        val tile = other as Tile
        var compare = fCost.compareTo(tile.fCost)
        if (compare == 0)
            compare = hCost.compareTo(tile.hCost)
        return -compare
    }

    override fun equals(other: Any?): Boolean {

        if (other is Tile) {
            return gridX == other.gridX && gridY == other.gridY
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + gridX
        result = 31 * result + gridY
        result = 31 * result + heapIndex
        result = 31 * result + gCost
        result = 31 * result + hCost
        return result
    }
}