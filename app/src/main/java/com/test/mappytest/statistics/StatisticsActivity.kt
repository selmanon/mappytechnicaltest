package com.test.mappytest.statistics

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding2.widget.RxTextView
import com.test.mappytest.R
import com.test.mappytest.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.Function5

@AndroidEntryPoint
class StatisticsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fizzBuzzViewModel = ViewModelProvider(this).get(FizzBuzzViewModel::class.java)

        setupUi()
        observe()
    }

    private fun observe() {
        fizzBuzzViewModel.processorOutputLiveData.observe(this, Observer { pairResult ->
            if (pairResult.first != null) {
                binding.textViewResult.text = pairResult.first.toString()
            }

            if (pairResult.second != null) {
                if (pairResult.second is InvalidInputException) {
                    Toast.makeText(this, getString(R.string.invalid_inputs), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun setupUi() {
        binding.buttonProcess.setOnClickListener {
            fizzBuzzViewModel.process(
                binding.editTextTextIntegerOne.text.toString().toInt(),
                binding.editTextTextIntegerTwo.text.toString().toInt(),
                binding.editTextTextRangeLimit.text.toString().toInt(),
                binding.editTextStringOne.text.toString(),
                binding.editTextStringTwo.text.toString()
            )

            binding.textViewResult.text = getString(R.string.processing_label)

            hideKeyboard()
        }

        binding.buttonCancelProcessing.setOnClickListener {
            fizzBuzzViewModel.cancelProcessing()
            binding.textViewResult.text = getString(R.string.result_hint)
        }

        editTextIntegerOneStream =
            RxTextView.textChanges(binding.editTextTextIntegerOne)
                .toFlowable(BackpressureStrategy.DROP).share()

        editTextIntegerTwoStream =
            RxTextView.textChanges(binding.editTextTextIntegerTwo)
                .toFlowable(BackpressureStrategy.DROP).share()

        editTextIntegerRangeLimitSteam =
            RxTextView.textChanges(binding.editTextTextRangeLimit)
                .toFlowable(BackpressureStrategy.DROP).share()

        editTextStringOneStream =
            RxTextView.textChanges(binding.editTextStringOne)
                .toFlowable(BackpressureStrategy.DROP).share()

        editTextStringTwoStream =
            RxTextView.textChanges(binding.editTextStringTwo)
                .toFlowable(BackpressureStrategy.DROP).share()

        Flowable.combineLatest(
            editTextIntegerOneStream,
            editTextIntegerTwoStream,
            editTextIntegerRangeLimitSteam,
            editTextStringOneStream,
            editTextStringTwoStream,
            Function5 { t1: CharSequence, t2: CharSequence, t3: CharSequence, t4: CharSequence, t5: CharSequence ->
                binding.buttonProcess.isEnabled =
                    t1.isNotEmpty() && t2.isNotEmpty() && t3.isNotEmpty() && t4.isNotEmpty() && t5.isNotEmpty()
            }
        ).subscribe()
    }

    private fun hideKeyboard() {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}