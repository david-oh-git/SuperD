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

package io.audioshinigami.superd.utility

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test

/**
 * Test class for [TwitterUrlUtil]
 */

@ExperimentalCoroutinesApi
class TwitterUtilTest {

    @Test
    fun getId_returnsId() = runBlockingTest {
        // Arrange: create sample twitterurl
        val sampleUrl = "https://twitter.com/_SJPeace55667_/status/1280374462542745605?s=19"
        val expectedIdValue = 1280374462542745605L
        val secondSampleUrl = "https://twitter.com/_VALKlNG/status/1289899309844979712"
        val secondExpectedIdValue = 1289899309844979712L

        // Act: get id
        val id = TwitterUrlUtil.getId(sampleUrl)
        val secondId =  TwitterUrlUtil.getId(secondSampleUrl)

        // Assert:
        assertThat( expectedIdValue, `is`(id))
        assertThat( secondExpectedIdValue, `is`(secondId))
    }

    @Test
    fun isTwitterUrl_returnsTrue() = runBlockingTest {
        // Arrange: create sample url
        val url = "https://twitter.com/_SJPeace55667_/status/1280374462542745605?s=19"
        val expectedResult = true

        // Act: call isTwitterUrl with url
        val result = TwitterUrlUtil.isTwitterUrl(url)

        // Assert: assert expected value against result
        assertThat(expectedResult, `is`(result) )
    }

    @Test
    fun isTwitterUrl_returnsFalse() = runBlockingTest {
        // Arrange: create sample url
        val url = "https://google.com"
        val emptyString = ""
        val expectedResult = false

        // Act: call isTwitterUrl with url
        val result = TwitterUrlUtil.isTwitterUrl(url)
        val secondResult = TwitterUrlUtil.isTwitterUrl(emptyString)

        // Assert: assert expected value against result
        assertThat(expectedResult, `is`(result) )
        assertThat(expectedResult, `is`(secondResult) )
    }

    @Test
    fun getDownloadLinkFromApiResponse() = runBlockingTest {
        // Arrange: create various responses
        val responseUrl = "https://video.twimg.com/ext_tw_video/1000818082846334982/pu/vid/480x480/8nrrUYJegAWhRiVQ.mp4?tag=3"
        val expectUrlForResponseUrl = "https://video.twimg.com/ext_tw_video/1000818082846334982/pu/vid/480x480/8nrrUYJegAWhRiVQ.mp4"
        val anodaResponseUrl = "https://video.twimg.com/ext_tw_video/1000818082846334982/pu/vid/480x480/8nrrUYJegAWhRiVQ.mp4?tag=3"
        val expectedUrlForAnodaResponseUrl = "https://video.twimg.com/ext_tw_video/1000818082846334982/pu/vid/480x480/8nrrUYJegAWhRiVQ.mp4"

        // Act: call extractDownloadUrl
        val result = TwitterUrlUtil.extractDownloadUrl(responseUrl)
        val anodaResult = TwitterUrlUtil.extractDownloadUrl(anodaResponseUrl)

        // Assert: assert expected values against results
        assertThat( result, `is`(expectUrlForResponseUrl))
        assertThat( anodaResult, `is`(expectedUrlForAnodaResponseUrl))
    }

    @Test
    fun extractTwitterUrlFromShareIntent() = runBlockingTest {
        // Arrange: create response from shared Intent
        val thirdPartyAppResponse = "When I finally buy this iPhone, it’s over for all of you \uD83E\uDD32 https://t.co/zoPlSf4XCc\n" +
                "    \n" +
                "    https://twitter.com/_VALKlNG/status/1289899309844979712"
        val officialAppResponse = " https://twitter.com/almircolan/status/1289744460130050049?s=09"
        val secondThirdPartyAppResponse = "When I finally buy this iPhone, it’s over for all of you \uD83E\uDD32 https://t.co/zoPlSf4XCc\n" +
                "    \n" +
                "    https://twitter.com/_VALKlNG/status/1289899309844979712"
        val expectedThirdPartyValue = "https://twitter.com/_VALKlNG/status/1289899309844979712"
        val expectedOfficialAppValue = "https://twitter.com/almircolan/status/1289744460130050049?s=09"
        val expectedSecondThirdPartyAppValue = "https://twitter.com/_VALKlNG/status/1289899309844979712"

        // Act: call extractTwitterUrlFromShareIntent
        val thirdPartyAppResult = TwitterUrlUtil.extractTwitterUrlFromShareIntent(thirdPartyAppResponse)
        val officialAppResult = TwitterUrlUtil.extractTwitterUrlFromShareIntent(officialAppResponse)
        val secondThirdPartyAppResult = TwitterUrlUtil.extractTwitterUrlFromShareIntent(secondThirdPartyAppResponse)

        // Assert: assert expected values against results
        assertThat(expectedThirdPartyValue, `is`( thirdPartyAppResult) )
        assertThat(expectedOfficialAppValue, `is`( officialAppResult) )
        assertThat(expectedSecondThirdPartyAppValue, `is`( secondThirdPartyAppResult) )
    }
}