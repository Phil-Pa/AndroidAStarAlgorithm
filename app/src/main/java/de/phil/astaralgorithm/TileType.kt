package de.phil.astaralgorithm

enum class TileType {

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