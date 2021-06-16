package com.test.mappytest

import com.test.mappytest.fizzbuzz.FizzBuzzProcessor
import com.test.mappytest.fizzbuzz.InvalidInputException
import com.test.mappytest.model.IntegersInput
import com.test.mappytest.model.StringsInput
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FizzBuzzProcessorTest : TestCase() {

    private val fizzBuzzProcessor: FizzBuzzProcessor =
        FizzBuzzProcessor()


    @Test
    fun `given all inputs are valid then processOutput success`() {
        // arrange
        val integerInput = IntegersInput(3, 5, 16)
        val stringInput = StringsInput("fizz", "buzz")
        val expectedOutput =
            StringBuilder("1,2,fizz,4,buzz,fizz,7,8,fizz,buzz,11,fizz,13,14,fizzbuzz,16,")
        // act
        val output = fizzBuzzProcessor.processOutput(integerInput, stringInput)

        // assert
        output.lastOrError().test().assertResult(expectedOutput.toString()).assertComplete()

    }

    @Test
    fun `given integer one input are not valid then processOutput throw Exception`() {
        val integerInput = IntegersInput(17, 5, 16)
        val stringInput = StringsInput("fizz", "buzz")


        val processOutput = fizzBuzzProcessor.processOutput(integerInput, stringInput)

        processOutput.lastOrError().test().assertError(InvalidInputException::class.java)

    }
}