package com.test.mappytest.model

import javax.inject.Inject


data class Request @Inject constructor(val integerInput: IntegerInput, val stringInput: StringInput)
