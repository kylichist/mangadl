package com.github.kylichist.mangadl.manganelo

import com.github.kylichist.mangadl.common.Client
import com.github.kylichist.mangadl.common.Scrapper

const val TEST_URL = "https://manganelo.com/chapter/the_hero_and_the_demon_kings_romcom/chapter_38"
const val TEST_PATH = "C:/Users/peter/Downloads/manga-tests"

suspend fun main() {
    val client: Client = MangaNelo
    val scrapper: Scrapper = client.scrapper(TEST_URL)
    client.search("a")[2].scrapper().downloadChapter(TEST_PATH)
}

private fun Any?.print() = println(toString())