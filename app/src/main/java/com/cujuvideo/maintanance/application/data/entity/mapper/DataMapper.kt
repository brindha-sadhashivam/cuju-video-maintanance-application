package com.cujuvideo.maintanance.application.data.entity.mapper

/**
 * DataMapper to transform the entity to the data model
 */
abstract class DataMapper<E, D> {

    /**
     * Transforms the entity to the data model
     */
    abstract fun transform(e: E): D

    /**
     * Transforms the list of entities to the list of data models
     */
    open fun transform(e: List<E>): List<D> {
        if (e.isEmpty()) return emptyList()
        val result = mutableListOf<D>()
        e.forEach { entity ->
            result.add(transform(entity))
        }
        return result
    }
}