/*
 * MIT License
 *
 * Copyright (c) 2020  David Osemwota
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.audioshinigami.superd.downloads

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.audioshinigami.superd.MainActivity
import io.audioshinigami.superd.R
import io.audioshinigami.superd.data.source.FileInfoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class DownloadsFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var repository: FileInfoRepository

    @Test
    fun clickFabButton_navigateToDownloadFragment(){
        // Arrange : Home screen
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        // create fragment scenerio for DownloadFragment
        val scenario = launchFragmentInContainer(Bundle(), R.style.AppTheme) {
            DownloadsFragment().also { fragment ->

                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if( viewLifecycleOwner != null )
                        Navigation.setViewNavController( fragment.requireView(), navController)
                }
            }
        }
        navController.setGraph(R.navigation.navigation_graph)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Act : click on fab button
        onView(withId(R.id.add_download_fab) ).perform( click())

        // Assert : confirm we navigate to AddDownload screen
        assertThat(navController.currentDestination?.id, `is`(R.id.addDownloadFragment))
    }

}