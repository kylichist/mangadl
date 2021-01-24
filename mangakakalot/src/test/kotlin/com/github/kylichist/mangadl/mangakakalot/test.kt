package com.github.kylichist.mangadl.mangakakalot

import com.github.kylichist.mangadl.common.Client
import com.github.kylichist.mangadl.common.Scrapper

const val TEST_URL = "https://mangakakalot.com/chapter/ps918650/chapter_1"
const val TEST_PATH = "C:/Users/peter/Downloads/manga-tests"

suspend fun main() {
    val client: Client = MangaKakalot
    val scrapper: Scrapper = client.scrapper(TEST_URL)
    scrapper.downloadChapterRange(TEST_PATH, "3", "6")
}

private fun Any?.print() = println(toString())