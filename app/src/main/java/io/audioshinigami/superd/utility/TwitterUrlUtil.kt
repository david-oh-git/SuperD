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

/**
 *  Utility for handling various forms of twitter urls
 *  - download url from Twitter's stream API response
 *  - extracting id value from url
 *  - also parsing string from android OS share Intent
 */

object TwitterUrlUtil {

    fun isTwitterUrl(url: String): Boolean = url.contains("https://twitter.com/", ignoreCase = true)

    // share intent verbose info asides the url , eg the tweet itself
    fun extractTwitterUrlFromShareIntent(intentTwitterText: String): String {
        return  Regex("""https://twitter.com/.*""").find(intentTwitterText)?.value?.trim() ?: ""
    }

    // parse url data from twitter API and return just the direct download link
    fun extractDownloadUrl(url: String) = Regex("""https://.*\.[mM][pP]4""").find(url)?.value?.trim()

    /**
     * get id [Long]value from the twitter url
     *  see sample url:
     *  https://twitter.com/_USERNAME/status/_ID_?s=19
     *  https://twitter.com/_blablabla_/status/1280374462542745605?s=19
     */
    fun getId(twitterUrl: String) = twitterUrl.split("/")[5].split("?")[0].trim().toLong()
}