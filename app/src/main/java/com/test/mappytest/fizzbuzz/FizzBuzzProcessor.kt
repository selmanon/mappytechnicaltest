package com.test.mappytest.fizzbuzz

import com.test.mappytest.model.IntegersInput
import com.test.mappytest.model.StringInput
import io.reactivex.Observable
import javax.inject.Inject


class FizzBuzzProcessor @Inject constructor() {

    fun processOutput(integerInput: IntegersInput, stringInput: StringInput): Observable<String> {
        if (integerInput.integerOne > integerInput.limit || integerInput.integerTwo > integerInput.limit) {
            return Observable.error(InvalidInputException())
        } else {

            val output: StringBuilder = StringBuilder()

            return Observable
                .range(1, integerInput.limit)
                .concatMap { element ->
                    if (element.rem(integerInput.integerOne) == 0 && element.rem(integerInput.integerTwo) == 0) {
                        output.append(stringInput.stringOne + stringInput.stringTwo)
                    } else if (element.rem(integerInput.integerOne) == 0) {
                        output.append(stringInput.stringOne)
                    } else if (element.rem(integerInput.integerTwo) == 0) {
                        output.append(stringInput.stringTwo)
                    } else {
                        output.append(element.toString())
                    }

                    output.append(LIMITATOR)

                    return@concatMap Observable.just(output.toString())
                }

        }
    }

    companion object {
        const val LIMITATOR = ","
    }

}