package de.phil.astaralgorithm

import org.junit.Assert.*
import org.junit.Test

class TileTest {

    @Test
    fun testTileEquals() {
        val tile1 = Tile(TileType.WALL,0, 0, 4)
        val tile2 = Tile(TileType.PATH, 0, 0, 7)

        assertEquals(tile1, tile2)
        assertTrue(tile1 == tile2)
        assertFalse(tile1 != tile2)
    }

}