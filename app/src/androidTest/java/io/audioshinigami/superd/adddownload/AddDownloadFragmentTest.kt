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

package io.audioshinigami.superd.adddownload

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import io.audioshinigami.superd.R
import org.junit.Test


class AddDownloadFragmentTest {


    @Test
    fun urlFromBundleIsDisplayedInEditText(){
        // Arrange: setup navController and fragment scenario
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        // Act: pass url to bundle
        val sampleUrl = "https://twitter.com/_SJPeace55667_/status/1280374462542745605?s=19"
        val bundle = AddDownloadFragmentArgs(sampleUrl).toBundle()
        // create fragment scenerio for AddDownloadFragment
        val scenario = launchFragmentInContainer(bundle, R.style.AppTheme) {
            AddDownloadFragment().also { fragment ->

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

        // Assert: edit text has the same url pass to bundle
        onView( withId(R.id.id_edit_geturl) ).check( matches( withText(sampleUrl)) )
    }

    @Test
    fun forTwitterUrl_showLoadingTextViewAndProgressbar(){
        // Arrange: setup navController and fragment scenario
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        val sampleUrl = "https://twitter.com/_SJPeace55667_/status/1280374462542745605?s=19"
        val bundle = AddDownloadFragmentArgs(sampleUrl).toBundle()
        // create fragment scenerio for AddDownloadFragment
        val scenario = launchFragmentInContainer(bundle, R.style.AppTheme) {
            AddDownloadFragment().also { fragment ->

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

        // Act: click queue button
        onView( withId(R.id.id_btn_send_url) ).perform( click() )

        // Assert: Loading textView and Progressbar is show
        onView( withId(R.id.loading_progress_bar) ).check( matches( isDisplayed() ))
        onView( withId(R.id.loading_textview) ).check( matches( isDisplayed() ))
    }

}