package com.appleby.spacex

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.appleby.spacex.model.LaunchResult
import com.appleby.spacex.repository.SpaceXRepo
import com.appleby.spacex.viewmodel.LaunchListViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK

class LaunchTests {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val schedulerRule = RxImmediateSchedulerRule()

    lateinit var launchViewModel: LaunchListViewModel

    @MockK
    lateinit var observer: Observer<LaunchResult>

    val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkObject(SpaceXRepo)

        mockWebServer.start()

        launchViewModel = spyk(LaunchListViewModel())
        launchViewModel.result.observeForever(observer)
    }

    @Test
    fun testSuccessfulResponseRefreshesData() {
        // given
        val mockLaunchRequest = MockResponse().setResponseCode(HTTP_OK)
        mockWebServer.enqueue(mockLaunchRequest)

        val mockLaunchResponse = MockResponse()
            .setResponseCode(HTTP_OK)
            .setBody(successResponse)
        mockWebServer.enqueue(mockLaunchResponse)

        // when
        launchViewModel.requestPastLaunches()

        // then
        verify(exactly = 1) { launchViewModel.requestPastLaunches() }
        verify(exactly = 0) { observer.onChanged(LaunchResult.Failure) }
        verify { observer.onChanged(any()) }
    }

    @Test
    fun testInvalidResponse() {
        // given
        val mockResponse = MockResponse()
            .setResponseCode(HTTP_OK)
            .setBody("invalid json")
        mockWebServer.enqueue(mockResponse)

        // when
        launchViewModel.requestPastLaunches()

        // then
        verify { observer.onChanged(any()) }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    val successResponse = "[{\"fairings\":{\"reused\":false,\"recovery_attempt\":false,\"recovered\":false,\"ships\":[]},\"links\":{\"patch\":{\"small\":\"https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png\",\"large\":\"https://images2.imgbox.com/40/e3/GypSkayF_o.png\"},\"reddit\":{\"campaign\":null,\"launch\":null,\"media\":null,\"recovery\":null},\"flickr\":{\"small\":[],\"original\":[]},\"presskit\":null,\"webcast\":\"https://www.youtube.com/watch?v=0a_00nJ_Y88\",\"youtube_id\":\"0a_00nJ_Y88\",\"article\":\"https://www.space.com/2196-spacex-inaugural-falcon-1-rocket-lost-launch.html\",\"wikipedia\":\"https://en.wikipedia.org/wiki/DemoSat\"},\"static_fire_date_utc\":\"2006-03-17T00:00:00.000Z\",\"static_fire_date_unix\":1142553600,\"tbd\":false,\"net\":false,\"window\":0,\"rocket\":\"5e9d0d95eda69955f709d1eb\",\"success\":false,\"details\":\"Engine failure at 33 seconds and loss of vehicle\",\"crew\":[],\"ships\":[],\"capsules\":[],\"payloads\":[\"5eb0e4b5b6c3bb0006eeb1e1\"],\"launchpad\":\"5e9e4502f5090995de566f86\",\"auto_update\":true,\"failures\":[{\"time\":33,\"altitude\":null,\"reason\":\"merlin engine failure\"}],\"flight_number\":1,\"name\":\"FalconSat\",\"date_utc\":\"2006-03-24T22:30:00.000Z\",\"date_unix\":1143239400,\"date_local\":\"2006-03-25T10:30:00+12:00\",\"date_precision\":\"hour\",\"upcoming\":false,\"cores\":[{\"core\":\"5e9e289df35918033d3b2623\",\"flight\":1,\"gridfins\":false,\"legs\":false,\"reused\":false,\"landing_attempt\":false,\"landing_success\":null,\"landing_type\":null,\"landpad\":null}],\"id\":\"5eb87cd9ffd86e000604b32a\"},{\"fairings\":{\"reused\":false,\"recovery_attempt\":false,\"recovered\":false,\"ships\":[]},\"links\":{\"patch\":{\"small\":\"https://images2.imgbox.com/4f/e3/I0lkuJ2e_o.png\",\"large\":\"https://images2.imgbox.com/be/e7/iNqsqVYM_o.png\"},\"reddit\":{\"campaign\":null,\"launch\":null,\"media\":null,\"recovery\":null},\"flickr\":{\"small\":[],\"original\":[]},\"presskit\":null,\"webcast\":\"https://www.youtube.com/watch?v=Lk4zQ2wP-Nc\",\"youtube_id\":\"Lk4zQ2wP-Nc\",\"article\":\"https://www.space.com/3590-spacex-falcon-1-rocket-fails-reach-orbit.html\",\"wikipedia\":\"https://en.wikipedia.org/wiki/DemoSat\"},\"static_fire_date_utc\":null,\"static_fire_date_unix\":null,\"tbd\":false,\"net\":false,\"window\":0,\"rocket\":\"5e9d0d95eda69955f709d1eb\",\"success\":false,\"details\":\"Successful first stage burn and transition to second stage, maximum altitude 289 km, Premature engine shutdown at T+7 min 30 s, Failed to reach orbit, Failed to recover first stage\",\"crew\":[],\"ships\":[],\"capsules\":[],\"payloads\":[\"5eb0e4b6b6c3bb0006eeb1e2\"],\"launchpad\":\"5e9e4502f5090995de566f86\",\"auto_update\":true,\"failures\":[{\"time\":301,\"altitude\":289,\"reason\":\"harmonic oscillation leading to premature engine shutdown\"}],\"flight_number\":2,\"name\":\"DemoSat\",\"date_utc\":\"2007-03-21T01:10:00.000Z\",\"date_unix\":1174439400,\"date_local\":\"2007-03-21T13:10:00+12:00\",\"date_precision\":\"hour\",\"upcoming\":false,\"cores\":[{\"core\":\"5e9e289ef35918416a3b2624\",\"flight\":1,\"gridfins\":false,\"legs\":false,\"reused\":false,\"landing_attempt\":false,\"landing_success\":null,\"landing_type\":null,\"landpad\":null}],\"id\":\"5eb87cdaffd86e000604b32b\"}]"

}