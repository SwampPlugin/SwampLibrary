package com.cosine.library.kommand

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SubKommand(
    val argument: String,
    val description: String = "",
    val isOp: Boolean = false,
    val hide: Boolean = false,
    val async: Boolean = false,
    val helpPriority: Int = 100
)

