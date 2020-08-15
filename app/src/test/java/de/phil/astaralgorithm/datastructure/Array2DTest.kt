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

}