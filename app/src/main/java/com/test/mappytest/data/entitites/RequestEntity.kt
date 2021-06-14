package com.test.mappytest.data.entitites

import androidx.room.Entity

@Entity(primaryKeys = ["integerOne", "integerTwo", "limit", "stringOne", "stringTwo"])
class RequestEntity(
    val integerOne: Int,
    val integerTwo: Int,
    val limit: Int,
    val stringOne: String,
    val stringTwo: String,
    val occurrence: Int
)
