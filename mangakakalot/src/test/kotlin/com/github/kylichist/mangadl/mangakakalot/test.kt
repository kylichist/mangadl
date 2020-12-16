package com.github.kylichist.mangadl.mangakakalot

import com.github.kylichist.mangadl.mangakakalot.Kakalot

const val TEST_URL = "https://mangakakalot.com/chapter/ps918650/chapter_1"
const val TEST_PATH = "C:/Users/peter/Downloads/manga-tests"

fun main() {
    Kakalot.downloadChapter(TEST_URL, TEST_PATH, true)
}
