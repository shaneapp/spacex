package com.appleby.spacex.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.appleby.spacex.R
import com.appleby.spacex.networkmodel.Launch
import com.appleby.spacex.viewmodel.LaunchListViewModel
import com.appleby.spacex.model.LaunchResult

class MainActivity : AppCompatActivity() {

    private val viewModel: LaunchListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.result.observe(this, Observer {
            when (it) {
                is LaunchResult.Success -> updateRecyclerView(it.launches)
                is LaunchResult.Failure -> Toast.makeText(this, "Api Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.requestPastLaunches()
    }

    private fun updateRecyclerView(launchList: List<Launch>) {
        Log.i("SHANE", launchList.toString())
    }

}