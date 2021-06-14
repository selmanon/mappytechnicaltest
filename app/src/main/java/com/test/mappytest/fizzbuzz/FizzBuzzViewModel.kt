package com.test.mappytest.fizzbuzz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.mappytest.model.IntegerInput
import com.test.mappytest.model.Request
import com.test.mappytest.model.StringInput
import com.test.mappytest.statistics.RequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class FizzBuzzViewModel @Inject constructor(
    private val fizzBuzzProcessor: FizzBuzzProcessor,
    private val requestRepository: RequestRepository
) :
    ViewModel() {
    private var disposables: CompositeDisposable = CompositeDisposable()
    private var processOutputDisposable: Disposable? = null

    private var _processorOutputLiveData: MutableLiveData<Pair<String?, Throwable?>> =
        MutableLiveData()

    val processorOutputLiveData: LiveData<Pair<String?, Throwable?>>
        get() = _processorOutputLiveData

    fun process(integerOne: Int, integerTwo: Int, limit: Int, stringOne: String, stingTwo: String) {
        processOutputDisposable = fizzBuzzProcessor.processOutput(
            IntegerInput(integerOne, integerTwo, limit),
            StringInput(stringOne, stingTwo)
        )
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.io())
            .doOnComplete {
                requestRepository.insert(
                    Request(
                        IntegerInput(integerOne, integerTwo, limit),
                        StringInput(stringOne, stingTwo)
                    )
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _processorOutputLiveData.value = Pair(it, null)
            }, {
                _processorOutputLiveData.value = Pair(null, it)
            })

        disposables.add(processOutputDisposable!!)
    }

    fun cancelProcessing() {
        if (processOutputDisposable != null)
            processOutputDisposable!!.dispose()
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}