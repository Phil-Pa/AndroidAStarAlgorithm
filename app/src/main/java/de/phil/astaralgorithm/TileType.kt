package de.phil.astaralgorithm

enum class TileType {

    OPEN_SET,
    CLOSED_SET,
    WALL,
    WALKABLE,
    PATH,
    START,
    END;

    companion object {
        private val map = values().associateBy(TileType::ordinal)
        fun fromInt(type: Int) = map[type] ?: error("")
    }

}