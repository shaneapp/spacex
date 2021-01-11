package com.appleby.spacex.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.appleby.spacex.R
import com.appleby.spacex.networkmodel.Launch
import com.appleby.spacex.viewmodel.LaunchListViewModel
import com.appleby.spacex.model.LaunchResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: LaunchListViewModel by viewModels()

    private val launchListAdapter = LaunchListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.result.observe(this, Observer {
            when (it) {
                is LaunchResult.Success -> updateRecyclerView(it.launches)
                is LaunchResult.Failure -> networkFailure()
            }
            viewswitcher.displayedChild = 1
        })

        rvLaunches.layoutManager = LinearLayoutManager(this)
        rvLaunches.adapter = launchListAdapter
    }

    override fun onResume() {
        super.onResume()

        viewswitcher.displayedChild = 0
        viewModel.requestPastLaunches()
    }

    private fun networkFailure() {
        Toast.makeText(this, "Api Failure", Toast.LENGTH_SHORT).show()
    }

    private fun updateRecyclerView(launchList: List<Launch>) {
        launchListAdapter.updateData(launchList)
    }

}