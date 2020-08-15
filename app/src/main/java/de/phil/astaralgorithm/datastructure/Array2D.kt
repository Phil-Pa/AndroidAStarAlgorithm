package de.phil.astaralgorithm.datastructure

class Array2D<T>(val sizeX: Int, val sizeY: Int) {

    val length = sizeX * sizeY

    private val items = Array<Any?>(length) { null }

    @Suppress("UNCHECKED_CAST")
    fun get(x: Int, y: Int): T {
        return items[y * sizeX + x] as T
    }

    fun set(x: Int, y: Int, item: T) {
        items[y * sizeX + x] = item
    }

    private var i = 0

    operator fun hasNext() = i < length

    @Suppress("UNCHECKED_CAST")
    operator fun next(): T {
        i++
        return items[i] as T
    }

    operator fun iterator() = this
}