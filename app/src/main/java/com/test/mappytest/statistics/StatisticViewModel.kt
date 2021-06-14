package com.test.mappytest.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.mappytest.model.IntegerInput
import com.test.mappytest.model.Request
import com.test.mappytest.model.StringInput
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class StatisticViewModel @Inject constructor(private val requestRepository: RequestRepository) :
    ViewModel() {

    private var _statistics: MutableLiveData<List<Request>> = MutableLiveData()
    val statistics: LiveData<List<Request>>
        get() {
            return _statistics
        }

    private val compositeDisposable = CompositeDisposable()


    fun showStatistics() {
        compositeDisposable.add(
            requestRepository.getAll()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    _statistics.value = it.map {
                        Request(
                            IntegerInput(
                                it.integerOne,
                                it.integerTwo,
                                it.limit
                            ),
                            StringInput(
                                it.stringOne,
                                it.stringTwo
                            )
                        )
                    }
                }, {

                })
        )
    }
}