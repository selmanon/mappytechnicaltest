package com.test.mappytest.statistics

import android.app.Application
import com.test.mappytest.data.FizzBuzzDatabase
import com.test.mappytest.data.RequestDao
import com.test.mappytest.data.entitites.RequestEntity
import com.test.mappytest.model.Request
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RequestRepository @Inject constructor(application: Application) {
    private var requestDao: RequestDao

    init {
        val db = FizzBuzzDatabase.getInstance(application)
        requestDao = db.requestDao()
    }

    fun insertOrUpdateHits(request: Request) = Single.fromCallable {
        requestDao.insertOrUpdateHits(
            RequestEntity(
                integerOne = request.integersInput.integerOne,
                integerTwo = request.integersInput.integerTwo,
                limit = request.integersInput.limit,
                stringOne = request.stringsInput.stringOne,
                stringTwo = request.stringsInput.stringTwo,
                completed = 0,
                hits = 1
            )
        )
    }.subscribeOn(Schedulers.io())


    fun updateCompleted(request: Request) =
        requestDao.updateCompleted(
            integerOne = request.integersInput.integerOne,
            integerTwo = request.integersInput.integerTwo,
            limit = request.integersInput.limit,
            stringOne = request.stringsInput.stringOne,
            stringTwo = request.stringsInput.stringTwo
        )


    fun mostFrequentRequest() = requestDao.getMostFrequentRequest()

}