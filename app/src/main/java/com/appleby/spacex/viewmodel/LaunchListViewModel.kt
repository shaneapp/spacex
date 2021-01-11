package com.appleby.spacex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appleby.spacex.model.LaunchResult
import com.appleby.spacex.repository.SpaceXRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class LaunchListViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _result: MutableLiveData<LaunchResult> = MutableLiveData()
    val result: LiveData<LaunchResult> = _result

    fun requestPastLaunches() {
        SpaceXRepo.service
            .getPastLaunches()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ launchResponse ->

                launchResponse.body()?.let {
                    if (launchResponse.isSuccessful) {
                        _result.value = LaunchResult.Success(it)
                    }
                } ?: run {
                    _result.value = LaunchResult.Failure
                }

            }, {
                _result.value = LaunchResult.Failure
            }).addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}