package com.test.mappytest.fizzbuzz

import com.test.mappytest.model.IntegersInput
import com.test.mappytest.model.StringsInput
import io.reactivex.Observable
import javax.inject.Inject


class FizzBuzzProcessor @Inject constructor() {

    fun processOutput(integerInput: IntegersInput, stringsInput: StringsInput): Observable<String> {
        if (integerInput.integerOne > integerInput.limit || integerInput.integerTwo > integerInput.limit) {
            return Observable.error(InvalidInputException())
        } else {

            val output: StringBuilder = StringBuilder()
            val stingInput = stringsInput.stringOne + stringsInput.stringTwo

            return Observable
                .range(1, integerInput.limit)
                .take(10000)
                .concatMap { element ->
                    if (element.rem(integerInput.integerOne) == 0 && element.rem(integerInput.integerTwo) == 0) {
                        output.append(stingInput)
                    } else if (element.rem(integerInput.integerOne) == 0) {
                        output.append(stringsInput.stringOne)
                    } else if (element.rem(integerInput.integerTwo) == 0) {
                        output.append(stringsInput.stringTwo)
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