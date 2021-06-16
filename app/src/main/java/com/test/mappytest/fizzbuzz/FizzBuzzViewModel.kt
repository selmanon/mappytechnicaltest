package com.test.mappytest.fizzbuzz

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.mappytest.model.IntegersInput
import com.test.mappytest.model.Request
import com.test.mappytest.model.StringsInput
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

    companion object {
        val TAG: String = FizzBuzzViewModel::class.java.simpleName
    }

    private var disposables: CompositeDisposable = CompositeDisposable()
    private var processOutputDisposable: Disposable? = null

    private var _processorOutputLiveData: MutableLiveData<String> =
        MutableLiveData()
    val processorOutputLiveData: LiveData<String>
        get() = _processorOutputLiveData

    private var _processingError = MutableLiveData<Boolean>(false)
    val processingError: LiveData<Boolean>
        get() = _processingError


    private var _isProcessingCompleted: MutableLiveData<Boolean> =
        MutableLiveData(false)
    val isProcessingCompleted: LiveData<Boolean>
        get() = _isProcessingCompleted


    fun process(integerOne: Int, integerTwo: Int, limit: Int, stringOne: String, stingTwo: String) {
        val integerInput = IntegersInput(integerOne, integerTwo, limit)
        val stringInput = StringsInput(stringOne, stingTwo)
        val request = Request(integerInput, stringInput)

        processOutputDisposable =
            fizzBuzzProcessor
                .processOutput(integerInput, stringInput)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .doOnComplete {
                    requestRepository.updateCompleted(request).subscribe()
                    _isProcessingCompleted.postValue(true)
                }
                .lastElement()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _processorOutputLiveData.value = it },
                    { _processingError.value = true })

        disposables.add(processOutputDisposable!!)
    }

    fun insertRequest(
        integerOne: Int,
        integerTwo: Int,
        limit: Int,
        stringOne: String,
        stingTwo: String
    ) {
        val integerInput = IntegersInput(integerOne, integerTwo, limit)
        val stringInput = StringsInput(stringOne, stingTwo)
        val insertDisposable =
            requestRepository.insertOrUpdateHits(Request(integerInput, stringInput))
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.i(TAG, "insertRequest with success $it")
                }, {
                    Log.i(TAG, "error insertRequest" + it.message)
                })

        disposables.add(insertDisposable)
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