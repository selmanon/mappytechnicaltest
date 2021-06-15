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
    fun insert(requestEntity: RequestEntity): Single<Long>

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
    ): Single<Int>


    fun insertOrUpdateHits(requestEntity: RequestEntity): Single<Long> {
        val requestFromDatabase = getRequestByPrimaryKey(
            requestEntity.integerOne,
            requestEntity.integerTwo,
            requestEntity.limit,
            requestEntity.stringOne,
            requestEntity.stringTwo
        )
        if (requestFromDatabase == null) {
            return insert(requestEntity)
        } else {
            return updateHits(
                requestEntity.integerOne,
                requestEntity.integerTwo,
                requestEntity.limit,
                requestEntity.stringOne,
                requestEntity.stringTwo
            ).map { 1L }
        }
    }


    @Query("SELECT * FROM RequestEntity ORDER BY completed,hits DESC LIMIT 1")
    fun getMostFrequentRequest(): Single<RequestEntity>

}