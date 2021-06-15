package com.test.mappytest.model

import javax.inject.Inject


data class Request @Inject constructor(val integersInput: IntegersInput, val stringInput: StringInput)
