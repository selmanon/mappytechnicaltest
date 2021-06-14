package com.test.mappytest.statistics

import android.app.Application
import com.test.mappytest.data.FizzBuzzDatabase
import com.test.mappytest.data.RequestDao
import com.test.mappytest.data.entitites.RequestEntity
import com.test.mappytest.model.Request
import javax.inject.Inject

class RequestRepository @Inject constructor(application: Application) {
    private var requestDao: RequestDao

    init {
        val db = FizzBuzzDatabase.getInstance(application)
        requestDao = db.requestDao()
    }

    fun insertOrUpdateCompleted(request: Request) {
        requestDao.insertOrUpdateCompleted(
            RequestEntity(
                integerOne = request.integerInput.integerOne,
                integerTwo = request.integerInput.integerTwo,
                limit = request.integerInput.limit,
                stringOne = request.stringInput.stringOne,
                stringTwo = request.stringInput.stringTwo,
                completed = 1,
                hits = 1
            )
        )
    }

    fun insertOrUpdateHits(request: Request) {
        requestDao.insertOrUpdateHits(
            RequestEntity(
                integerOne = request.integerInput.integerOne,
                integerTwo = request.integerInput.integerTwo,
                limit = request.integerInput.limit,
                stringOne = request.stringInput.stringOne,
                stringTwo = request.stringInput.stringTwo,
                completed = 1,
                hits = 1
            )
        )
    }

    fun mostFrequentRequest() = requestDao.getMostFrequentRequest()

}