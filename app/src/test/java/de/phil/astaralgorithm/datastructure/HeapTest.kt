package de.phil.astaralgorithm.datastructure

import org.junit.Assert.*
import org.junit.Test
import kotlin.random.Random
import kotlin.system.measureNanoTime

class TestHeapItem(private val id: Int) : HeapItem<TestHeapItem> {

    override var heapIndex = 0
    override fun compareTo(other: HeapItem<TestHeapItem>): Int {
        val otherItem = other as TestHeapItem
        return id.compareTo(otherItem.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestHeapItem

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + heapIndex
        return result
    }


}

class HeapTest {

    @Test
    fun test() {
        val size = 1000000
        val randomIndex = Random.nextInt(size)

        var randomItem: TestHeapItem? = null

        val heap = Heap<TestHeapItem>(size)
        for (i in 0 until size) {
            val item = TestHeapItem(i)
            if (randomIndex == i)
                randomItem = item
            heap.add(item)
        }

        val nanos = measureNanoTime {
            if (randomItem == null)
                fail("random item is null")
            else
                assertTrue(randomItem in heap)
        }

        // contains of 1 millions items takes less than 50 microseconds
        assertTrue(nanos < 50000)
        println(nanos)
    }

}