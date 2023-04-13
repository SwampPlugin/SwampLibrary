package com.cosine.library.extension

fun <K, V> Map<K, V>.clone(): MutableMap<K, V> = toMutableMap()

fun <T> List<T>.clone(): MutableList<T> = toMutableList()

fun <T> Set<T>.clone(): MutableSet<T> = toMutableSet()