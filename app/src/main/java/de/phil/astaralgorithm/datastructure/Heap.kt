package de.phil.astaralgorithm.datastructure

class Heap<T : HeapItem<T>>(val maxHeapSize: Int) {

    private val items = Array<HeapItem<T>?>(maxHeapSize) { null }

    var size = 0

    fun add(item: HeapItem<T>) {
        item.heapIndex = size
        items[size] = item
        sortUp(item)
        size++
    }

    @Suppress("UNCHECKED_CAST")
    fun removeFirst(): T {
        val firstItem = items[0] ?: throw IllegalStateException("cant remove item when heap is empty")
        size--
        val lastItem = items[size] ?: throw IllegalStateException("items at index $size is null")
        lastItem.heapIndex = 0
        items[0] = lastItem
        sortDown(lastItem)
        return firstItem as T
    }

    fun updateItem(item: HeapItem<T>) {
        sortUp(item)
    }

    operator fun contains(item: HeapItem<T>) = items[item.heapIndex] == item

    private fun sortDown(item: HeapItem<T>) {
        while (true) {
            val childLeftIndex = item.heapIndex * 2 + 1
            val childRightIndex = item.heapIndex * 2 + 2
            if (childLeftIndex < size) {
                var swapIndex = childLeftIndex
                if (childRightIndex < size) {
                    val leftItem = items[childLeftIndex] ?: throw IllegalStateException("left item is null")
                    val rightItem = items[childRightIndex] ?: throw IllegalStateException("right item is null")

                    if (leftItem < rightItem)
                        swapIndex = childRightIndex
                }
                val swapItem = items[swapIndex] ?: throw IllegalStateException("swap item is null")
                if (item < swapItem)
                    swap(item, swapItem)
                else
                    return
            } else
                return
        }
    }

    private fun sortUp(item: HeapItem<T>) {
        var parentIndex = (item.heapIndex - 1) / 2
        while (true) {
            val parentItem = items[parentIndex] ?: throw IllegalStateException("parent item is null")
            if (item > parentItem)
                swap(item, parentItem)
            else
                break
            parentIndex = (item.heapIndex - 1) / 2
        }
    }

    private fun swap(itemA: HeapItem<T>, itemB: HeapItem<T>) {
        val itemAIndex = itemA.heapIndex
        val itemBIndex = itemB.heapIndex
        items[itemAIndex] = itemB
        items[itemBIndex] = itemA
        itemA.heapIndex = itemBIndex
        itemB.heapIndex = itemAIndex
    }

}