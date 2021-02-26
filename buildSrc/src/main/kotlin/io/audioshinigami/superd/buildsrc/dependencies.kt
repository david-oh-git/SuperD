/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("SpellCheckingInspection")
package io.audioshinigami.superd.buildsrc

object Versions {
    const val ktlint = "0.40.0"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:4.1.2"
    const val benManesUpdatePlugin = "com.github.ben-manes:gradle-versions-plugin:0.36.0"
    const val jdkDesugar = "com.android.tools:desugar_jdk_libs:1.0.9"

    const val material = "com.google.android.material:material:1.3.0"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val fetch = "androidx.tonyodev.fetch2:xfetch2:3.1.6"
    const val twitterKit = "com.twitter.sdk.android:twitter:3.3.0@aar"
    const val spotless = "com.diffplug.spotless:spotless-plugin-gradle:5.6.1"
    const val multidex = "androidx.multidex:multidex:2.0.1"

    const val truth = "com.google.truth:truth:1.1.2"
    const val robolectric = "org.robolectric:robolectric:4.5.1"
    const val mockito = "org.mockito:mockito-core:3.8.0"
    const val mockitoDexMaker = "com.linkedin.dexmaker:dexmaker-mockito:2.28.0"
    const val mockk = "io.mockk:mockk:1.10.2"

    object Junit5 {
        private const val version = "5.7.0"
        const val vantage = "org.junit.vintage:junit-vintage-engine:$version"
        const val api = "org.junit.jupiter:junit-jupiter-api:$version"
        const val engine = "org.junit.jupiter:junit-jupiter-engine:$version"
        const val platform = "org.junit.platform:junit-platform-launcher:$version"
    }

    object Hamcrest {
        private const val version = "2.2"
        const val hamcrest = "org.hamcrest:hamcrest:$version"
        const val hamcrestLib = "org.hamcrest:hamcrest-library:$version"
    }

    object Accompanist {
        private const val version = "0.6.0"
        const val coil = "dev.chrisbanes.accompanist:accompanist-coil:$version"
        const val insets = "dev.chrisbanes.accompanist:accompanist-insets:$version"
    }

    object Dagger2 {
        private const val version = "2.32"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val testCompiler = "com.google.dagger:dagger-compiler:$version"
    }

    object Kotlin {
        private const val version = "1.4.31"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.4.2"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object OkHttp {
        private const val version = "4.9.0"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val logging = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object JUnit {
        private const val version = "4.13"
        const val junit = "junit:junit:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val palette = "androidx.palette:palette:1.0.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val sharedPreference = "androidx.preference:preference:1.1.1"

        const val coreKtx = "androidx.core:core-ktx:1.5.0-beta01"

        object Paging {
            private const val version = "2.1.2"
            const val paging = "androidx.paging:paging-runtime:$version"
        }

        object RecyclerView {
            private const val version = "1.1.0"
            const val recyclerView = "androidx.recyclerview:recyclerview:$version"
        }

        object Navigation {
            private const val version = "2.3.3"
            const val navSafeArgsPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
            const val navigationUi = "androidx.navigation:navigation-ui-ktx:$version"
            const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val navigationTesting = "androidx.navigation:navigation-testing:$version"
        }

        object Lifecycle {
            private const val version = "2.3.0"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val extentions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
        }

        object Test {
            private const val version = "1.3.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"
            const val junitKtx = "androidx.test.ext:junit-ktx:1.1.2-rc02"
            const val testMonitor = "androidx.test:monitor:1.3.0-rc02"
            const val fragmentTesting = "androidx.fragment:fragment-testing:1.2.5"
            const val coreTesting = "androidx.arch.core:core-testing:2.1.0"

            object Ext {
                private const val version = "1.1.2-rc01"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
        }

        object Room {
            private const val version = "2.2.6"
            const val runtime = "androidx.room:room-runtime:$version"
            const val ktx = "androidx.room:room-ktx:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val test = "androidx.room:room-testing:$version"
        }
    }

    object Rome {
        private const val version = "1.14.1"
        const val rome = "com.rometools:rome:$version"
        const val modules = "com.rometools:rome-modules:$version"
    }
}
