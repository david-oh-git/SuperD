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

package io.audioshinigami.superd.data.source

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.data.sucessful
import io.audioshinigami.superd.di.component.DaggerTestAppComponent
import io.audioshinigami.superd.di.component.TestAppComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat


@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class TwitterApiRepositoryTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @Inject
    lateinit var ioDispather: CoroutineDispatcher
    @Inject
    lateinit var twitterSource: TwitterSource

    lateinit var twitterRepository: TwitterApiRepository

    @Before
    fun init(){
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        val component: TestAppComponent = DaggerTestAppComponent.factory().create(appContext)

        component.inject(this)

        twitterRepository = TwitterApiRepoImpl(twitterSource,ioDispather)
    }

    @Test
    fun getDownloadUrl_verifyChannelReturnValueSuccess() = runBlockingTest {
        // Arrange: create fake id
        val id = 8363635372L // even id returns Success result for [FakeTwitterSource]
        val numberOfTweetMedia = 6 //

        // Act: call get id
        twitterRepository.getDownloadUrls(id)
        val result = twitterRepository.allMediaChannel.receive()

        // Assert: confirm result value
        assertThat(result, notNullValue() )
        assertThat(true, `is`(result.sucessful))
        assertThat(numberOfTweetMedia, `is`( (result as Result.Success).data.size))
    }

    @Test
    fun getDownloadUrl_verifyChannelReturnValueError() = runBlockingTest {
        // Arrange: create fake id
        val id = 83636353727L // even id returns Error result for [FakeTwitterSource]
        val errorMessage = "Error getting url"

        // Act: call get id
        twitterRepository.getDownloadUrls(id)
        val result = twitterRepository.allMediaChannel.receive()

        // Assert: confirm result value
        assertThat(result, notNullValue() )
        assertThat(false, `is`(result.sucessful))
        assertThat(errorMessage, `is`( (result as Result.Error).exception.message ))
    }
}