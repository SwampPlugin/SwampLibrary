package com.cosine.library.util

class InfinityList<E>(
    private val base: List<E>,
    private val limit: Int
) {

    private val maxSize = base.size
    private var offset = 0

    fun getCurrentList(): List<E> {
        if(offset >= maxSize) offset = 0
        val currentOffset = offset++
        var max = currentOffset + limit
        if(max > maxSize) max = maxSize

        val subList = base.subList(currentOffset, max).toMutableList()

        val nextElementSize = limit - subList.size
        for(i in 0 until nextElementSize)
            subList.add(getElement(i))

        return subList
    }

    private fun getElement(index: Int): E {
        return if(base.size <= index) base[index - base.size] else base[index]
    }

}