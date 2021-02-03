package com.github.kylichist.mangadl.manganelo

import com.github.kylichist.mangadl.common.Client

const val TEST_URL = "https://manganelo.com/chapter/the_hero_and_the_demon_kings_romcom/chapter_38"
const val TEST_PATH = "C:/Users/peter/Downloads/manga-tests"

suspend fun main() {
    val client: Client = MangaNelo
    client.search("aaa")[2].scrapper("2").downloadChapter(TEST_PATH)
}

private fun Any?.print() = println(toString())