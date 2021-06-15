package com.test.mappytest.data

import androidx.room.*
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

    @Query("UPDATE RequestEntity SET completed = completed + 1 WHERE integerOne LIKE :integerOne AND integerTwo LIKE :integerTwo AND `limit` LIKE :limit AND stringOne LIKE :stringOne AND stringTwo LIKE :stringTwo")
    fun updateCompleted(
        integerOne: Int,
        integerTwo: Int,
        limit: Int,
        stringOne: String,
        stringTwo: String
    ): Single<Int>

    @Query("UPDATE RequestEntity SET hits = hits + 1 WHERE integerOne LIKE :integerOne AND integerTwo LIKE :integerTwo AND `limit` LIKE :limit AND stringOne LIKE :stringOne AND stringTwo LIKE :stringTwo")
    fun updateHits(
        integerOne: Int,
        integerTwo: Int,
        limit: Int,
        stringOne: String,
        stringTwo: String
    ): Int


    @Transaction
    fun insertOrUpdateHits(requestEntity: RequestEntity) {
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
            updateHits(
                requestEntity.integerOne,
                requestEntity.integerTwo,
                requestEntity.limit,
                requestEntity.stringOne,
                requestEntity.stringTwo
            )
    }


    @Query("SELECT * FROM RequestEntity ORDER BY completed,hits DESC LIMIT 1")
    fun getMostFrequentRequest(): Single<RequestEntity>

}