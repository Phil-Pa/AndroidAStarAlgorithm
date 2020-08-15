package de.phil.astaralgorithm.datastructure

import org.junit.Assert.*
import org.junit.Test

class Array2DTest {

    @Test
    fun testGet() {

        val array = Array2D<Int>(3, 8)
        array.set(2, 4, 9)

        assertEquals(9, array.get(2, 4))
        assertEquals(24, array.length)
    }

    @Test
    fun testGetIndex() {
        var array = Array2D<Int>(4, 4)

        assertEquals(10, array.getIndex(2, 2))
        assertEquals(11, array.getIndex(3, 2))

        array = Array2D(2, 7)
        assertEquals(5, array.getIndex(1, 2))
    }

}