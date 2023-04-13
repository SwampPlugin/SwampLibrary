package com.cosine.library.kommand

interface Castable<T> {
    fun cast(string: String?): T?
}