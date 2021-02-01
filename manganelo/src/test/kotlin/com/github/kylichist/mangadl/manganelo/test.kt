package com.github.kylichist.mangadl.manganelo

import com.github.kylichist.mangadl.common.Client
import com.github.kylichist.mangadl.common.Scrapper
import org.jsoup.Jsoup

const val TEST_URL = "https://mangakakalot.com/chapter/ps918650/chapter_1"
const val TEST_PATH = "C:/Users/peter/Downloads/manga-tests"

suspend fun main() {
    val client: Client = MangaKakalot
    val scrapper: Scrapper = client.scrapper(TEST_URL)
    //im main cha
    Jsoup.connect("https://mangakakalot.com/home_json_search")
        .header("searchword", "im_main_cha")
        .post()
}

private fun Any?.print() = println(toString())