package com.test.mappytest.fizzbuzz

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.test.mappytest.model.IntegersInput
import com.test.mappytest.model.Request
import com.test.mappytest.model.StringsInput
import com.test.mappytest.statistics.RequestRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.spyk
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock


@RunWith(JUnit4::class)
class FizzBuzzViewModelTest {
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var cut: FizzBuzzViewModel

    @Mock
    var defaultFizzBuzzProcessor: FizzBuzzProcessor = spyk()

    @Mock
    var requestRepository: RequestRepository = mockkClass(RequestRepository::class)



    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }

        cut = FizzBuzzViewModel(defaultFizzBuzzProcessor, requestRepository)
    }

    @Test
    fun `processing result Success`() {

        val defaultFizzBuzzProcessor = spyk<FizzBuzzProcessor>()

        // Arrange
        every {
            defaultFizzBuzzProcessor.processOutput(
                IntegersInput(1, 2, 3),
                StringsInput("Fi", "Bu")
            )
        } answers {
            Observable.just("1", "2")
        }

        every {
            requestRepository.updateCompleted(
                Request(
                    IntegersInput(1, 2, 3),
                    StringsInput("Fi", "Bu")
                )
            )
        } answers {
            Single.just(1)
        }

        val processedResult = MutableLiveData<Pair<String?, Throwable?>>()
        processedResult.value = Pair("Fi,FiBu,Fi,", null)

        val observer = Observer<Pair<String?, Throwable?>> {}
        cut.processorOutputLiveData.observeForever(observer)

        try {
            // Act
            cut.process(1, 2, 3, "Fi", "Bu")

            // Assert
            Assert.assertEquals(processedResult.value, cut.processorOutputLiveData.value)
        } finally {
            cut.processorOutputLiveData.removeObserver(observer)
        }
    }


    @Test
    fun `processing result fail`() {
        // Arrange
        val processedResult = MutableLiveData<Pair<String?, Throwable?>>()
        processedResult.value = Pair(null, InvalidInputException("input One or input Two are greater then limit"))

        val observer = Observer<Pair<String?, Throwable?>> {}
        cut.processorOutputLiveData.observeForever(observer)

        try {
            // Act
            cut.process(2, 3, 1, "Fi", "Bu")

            // Assert
            Assert.assertEquals(processedResult.value, cut.processorOutputLiveData.value)
        } finally {
            cut.processorOutputLiveData.removeObserver(observer)
        }
    }


}