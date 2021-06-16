package com.test.mappytest.fizzbuzz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding2.widget.RxTextView
import com.test.mappytest.R
import com.test.mappytest.databinding.ActivityMainBinding
import com.test.mappytest.statistics.StatisticsActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.Function5

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var editTextIntegerOneStream: Flowable<CharSequence>
    private lateinit var editTextIntegerTwoStream: Flowable<CharSequence>
    private lateinit var editTextIntegerRangeLimitSteam: Flowable<CharSequence>
    private lateinit var editTextStringOneStream: Flowable<CharSequence>
    private lateinit var editTextStringTwoStream: Flowable<CharSequence>

    private lateinit var binding: ActivityMainBinding

    private lateinit var fizzBuzzViewModel: FizzBuzzViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

        fizzBuzzViewModel = ViewModelProvider(this).get(FizzBuzzViewModel::class.java)

        setupUi()
        observe()
    }

    private fun observe() {
        fizzBuzzViewModel.processorOutputLiveData.observe(this, Observer { pairResult ->

            if (pairResult.first != null) {
                //binding.textViewResult.text = pairResult.first
                binding.textViewResult.loadData(pairResult.first.toString(), "text/html", "UTF-8")
            }

            if (pairResult.second != null) {
                if (pairResult.second is InvalidInputException) {
                    binding.buttonProcess.isEnabled = true
                    //binding.textViewResult.text = resources.getText(R.string.result_hint).toString()
                    binding.textViewResult.loadData(
                        resources.getText(R.string.result_hint).toString(), "text/html", "UTF-8"
                    )
                    Toast.makeText(
                        this,
                        String.format(getString(R.string.invalid_inputs), pairResult.second),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

        })

        fizzBuzzViewModel.isProcessingCompleted.observe(this, Observer
        {
            binding.buttonProcess.isEnabled = it
        })
    }

    private fun setupUi() {
        binding.buttonProcess.setOnClickListener {
            try {
                fizzBuzzViewModel.insertRequest(
                    binding.editTextTextIntegerOne.text.toString().toInt(),
                    binding.editTextTextIntegerTwo.text.toString().toInt(),
                    binding.editTextTextRangeLimit.text.toString().toInt(),
                    binding.editTextStringOne.text.toString(),
                    binding.editTextStringTwo.text.toString()
                )

                fizzBuzzViewModel.process(
                    binding.editTextTextIntegerOne.text.toString().toInt(),
                    binding.editTextTextIntegerTwo.text.toString().toInt(),
                    binding.editTextTextRangeLimit.text.toString().toInt(),
                    binding.editTextStringOne.text.toString(),
                    binding.editTextStringTwo.text.toString()
                )

                //binding.textViewResult.text = getString(R.string.processing_label)
                binding.textViewResult.loadData(
                    getString(R.string.processing_label), "text/html", "UTF-8"
                )
                binding.buttonProcess.isEnabled = false

            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    String.format(getString(R.string.invalid_inputs), e.message),
                    Toast.LENGTH_LONG
                )
                    .show()
            }

            hideKeyboard()
        }

        binding.buttonCancelProcessing.setOnClickListener {
            fizzBuzzViewModel.cancelProcessing()
            //binding.textViewResult.setText(getString(R.string.result_hint))
            binding.textViewResult.loadData(
                getString(R.string.result_hint), "text/html", "UTF-8"
            )
            binding.buttonProcess.isEnabled = true
        }

        binding.buttonStatistics.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
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
                    isInputsNotEmpty(t1, t2, t3, t4, t5)
            }
        ).subscribe()
    }

    private fun isInputsNotEmpty(
        t1: CharSequence,
        t2: CharSequence,
        t3: CharSequence,
        t4: CharSequence,
        t5: CharSequence
    ) = t1.isNotEmpty() && t2.isNotEmpty() && t3.isNotEmpty() && t4.isNotEmpty() && t5.isNotEmpty()

    private fun hideKeyboard() {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}