package com.test.mappytest

import com.test.mappytest.fizzbuzz.FizzBuzzProcessor
import com.test.mappytest.fizzbuzz.InvalidInputException
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
        val integerInput = FizzBuzzProcessor.IntegerInput(3, 5, 16)
        val stringInput = FizzBuzzProcessor.StringInput("fizz", "buzz")
        val expectedOutput = listOf(
            "1", "2",
            "fizz",
            "4",
            "buzz",
            "fizz",
            "7",
            "8",
            "fizz",
            "buzz",
            "11",
            "fizz",
            "13",
            "14",
            "fizzbuzz",
            "16"
        )
        // act
        val output = fizzBuzzProcessor.processOutput(integerInput, stringInput)

        // assert
        assertEquals(
            expectedOutput, output
        )

    }

    @Test(expected = InvalidInputException::class)
    fun `given integer one input are not valid then processOutput throw Exception`() {
        val integerInput = FizzBuzzProcessor.IntegerInput(17, 5, 16)
        val stringInput = FizzBuzzProcessor.StringInput("fizz", "buzz")


        fizzBuzzProcessor.processOutput(integerInput, stringInput)
    }
}