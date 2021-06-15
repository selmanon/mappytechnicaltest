package com.test.mappytest.statistics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.test.mappytest.R
import com.test.mappytest.databinding.ActivityStatsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatsBinding
    private lateinit var statisticViewModel: StatisticViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater);
        setContentView(binding.root)

        statisticViewModel = ViewModelProvider(this).get(StatisticViewModel::class.java)

        observe()
    }

    private fun observe() {
        statisticViewModel.showStatistics()
        statisticViewModel.statistics.observe(this, Observer { requestEntity ->
            binding.textViewIntegerOne.text = String.format(
                resources.getString(
                    R.string.integer_one_label
                ), requestEntity.integerOne.toString()
            )
            binding.textViewIntegerTwo.text = String.format(
                resources.getString(
                    R.string.integer_two_label
                ), requestEntity.integerTwo.toString()
            )

            binding.textViewRangeLimit.text = String.format(
                resources.getString(
                    R.string.limit_label
                ), requestEntity.limit.toString()
            )

            binding.textViewStringOne.text = String.format(
                resources.getString(
                    R.string.string_one_label
                ), requestEntity.stringOne
            )
            binding.textViewStringTwo.text = String.format(
                resources.getString(
                    R.string.string_two_label
                ), requestEntity.stringTwo
            )

            binding.textViewCompletedRequest.text = String.format(
                resources.getString(
                    R.string.completed_request_label
                ), requestEntity.completed.toString()
            )
            binding.textViewNumberOfHits.text = String.format(
                resources.getString(
                    R.string.number_of_hits_label
                ), requestEntity.hits.toString()
            )

            binding.progressBarCompletedRequests.max = requestEntity.hits
            binding.progressBarCompletedRequests.progress = requestEntity.completed
        })
    }
}