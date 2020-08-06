package de.phil.astaralgorithm.datastructure

interface HeapItem<T> : Comparable<HeapItem<T>> {

    var heapIndex: Int

}