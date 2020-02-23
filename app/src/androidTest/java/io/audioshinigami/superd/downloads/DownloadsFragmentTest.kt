package io.audioshinigami.superd.downloads

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import io.audioshinigami.superd.R
import io.audioshinigami.superd.ServiceLocator
import io.audioshinigami.superd.data.source.FakeAndroidTestRepository
import io.audioshinigami.superd.zdata.source.FileInfoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class DownloadsFragmentTest {

    private lateinit var repository: FileInfoRepository

    @Before
    fun initRepository(){
        repository = FakeAndroidTestRepository()
        ServiceLocator.fileInfoRepository = repository
    }

    @After
    fun cleanUpDb(){
        ServiceLocator.resetRepository()
    }

    @Test
    fun clickFabButton_navigateToDownloadFragment(){
        // Arrange : Home screen
        val scenario = launchFragmentInContainer<DownloadsFragment>(Bundle(), R.style.AppTheme )
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // Act : click on fab button
        onView(withId(R.id.add_download_fab) ).perform( click())

        // Assert : confirm we navigate to AddDownload screen
        verify(navController).navigate(
            R.id.action_downloadsFragment_to_getUrlFragment
        )
    }
}