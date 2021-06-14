package com.test.mappytest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.mappytest.data.entitites.RequestEntity
import io.reactivex.Single

@Dao
interface RequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(requestEntity: RequestEntity)

    @Query("SELECT * from RequestEntity WHERE integerOne LIKE :integerOne AND integerTwo LIKE :integerTwo AND `limit` LIKE :limit AND stringOne LIKE :stringOne AND stringTwo LIKE :stringTwo")
    fun getRequestByPrimaryKey(
        integerOne: Int,
        integerTwo: Int,
        limit: Int,
        stringOne: String,
        stringTwo: String
    ): RequestEntity?

    @Query("UPDATE RequestEntity SET occurrence = occurrence + 1 WHERE integerOne LIKE :integerOne AND integerTwo LIKE :integerTwo AND `limit` LIKE :limit AND stringOne LIKE :stringOne AND stringTwo LIKE :stringTwo")
    fun updateOccurrence(
        integerOne: Int,
        integerTwo: Int,
        limit: Int,
        stringOne: String,
        stringTwo: String
    )

    fun insertOrUpdate(requestEntity: RequestEntity) {
        val requestFromDatabase = getRequestByPrimaryKey(
            requestEntity.integerOne,
            requestEntity.integerTwo,
            requestEntity.limit,
            requestEntity.stringOne,
            requestEntity.stringTwo
        )
        if (requestFromDatabase == null)
            insert(requestEntity)
        else
            updateOccurrence(
                requestEntity.integerOne,
                requestEntity.integerTwo,
                requestEntity.limit,
                requestEntity.stringOne,
                requestEntity.stringTwo
            )
    }

    @Query("SELECT * FROM RequestEntity")
    fun getAll(): Single<List<RequestEntity>>

}