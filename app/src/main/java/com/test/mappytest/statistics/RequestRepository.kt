package com.test.mappytest.statistics

import android.app.Application
import com.test.mappytest.data.FizzBuzzDatabase
import com.test.mappytest.data.RequestDao
import com.test.mappytest.data.entitites.RequestEntity
import com.test.mappytest.model.Request
import io.reactivex.Observable
import javax.inject.Inject

class RequestRepository @Inject constructor(application: Application) {
    private var requestDao: RequestDao

    init {
        val db = FizzBuzzDatabase.getInstance(application)
        requestDao = db.requestDao()
    }

    fun insertOrUpdateHits(request: Request) =
        requestDao.insertOrUpdateHits(
            RequestEntity(
                integerOne = request.integersInput.integerOne,
                integerTwo = request.integersInput.integerTwo,
                limit = request.integersInput.limit,
                stringOne = request.stringInput.stringOne,
                stringTwo = request.stringInput.stringTwo,
                completed = 1,
                hits = 1
            )
        )




    fun updateCompleted(request: Request) {
        requestDao.updateCompleted(
            integerOne = request.integersInput.integerOne,
            integerTwo = request.integersInput.integerTwo,
            limit = request.integersInput.limit,
            stringOne = request.stringInput.stringOne,
            stringTwo = request.stringInput.stringTwo

        )
    }

    fun mostFrequentRequest() = requestDao.getMostFrequentRequest()

}