package com.test.mappytest.statistics

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.mappytest.data.entitites.RequestEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(private val requestRepository: RequestRepository) :
    ViewModel() {

    companion object {
        val TAG: String = StatisticViewModel::class.java.simpleName
    }

    private var _statistics: MutableLiveData<RequestEntity> = MutableLiveData()
    val statistics: LiveData<RequestEntity>
        get() {
            return _statistics
        }

    private val compositeDisposable = CompositeDisposable()


    fun showStatistics() {
        compositeDisposable.add(
            requestRepository.mostFrequentRequest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _statistics.value = it
                },
                    {
                        Log.i(TAG, it.message.toString())
                    })
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}