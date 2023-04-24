package com.cosine.library.injector

import java.util.function.Supplier
import kotlin.reflect.KClass

open class Injector {

    private val mappings = mutableSetOf<QualifierData<*>>()

    inline fun <reified T : Any> inject(): T {
        return inject0(T::class)
    }

    inline fun <reified T : Any> insert(supplier: Supplier<T>) {
        return insert0(T::class, supplier)
    }

    @Suppress("unchecked_cast")
    fun <T : Any> inject0(clazz: KClass<T>): T {
        return mappings.firstOrNull {
            it.qualifier == clazz.qualifiedName
        }?.run { instance as T } ?: throw RuntimeException("${clazz.qualifiedName}을 찾을 수 없습니다.")
    }

    fun <T : Any> insert0(clazz: KClass<T>, supplier: Supplier<T>) {
        mappings.add(QualifierData.of(clazz, supplier))
    }

    data class QualifierData<T>(
        val qualifier: String,
        private val supplier: Supplier<T>
    ) {

        val instance by lazy { supplier.get() }

        override fun toString(): String = qualifier

        companion object {
            fun <T : Any> of(clazz: KClass<T>, supplier: Supplier<T>): QualifierData<T> = QualifierData(clazz.qualifiedName!!, supplier)
        }
    }
}