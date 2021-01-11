package com.appleby.spacex.model

import com.appleby.spacex.networkmodel.Launch

sealed class LaunchResult {
    data class Success(val launches: List<Launch>) : LaunchResult()
    object Failure : LaunchResult()
}