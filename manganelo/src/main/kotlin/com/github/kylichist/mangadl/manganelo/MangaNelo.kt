package com.github.kylichist.mangadl.manganelo

import com.github.kylichist.mangadl.common.*
import org.jsoup.Jsoup

object MangaKakalot : Client {

    override val BASE_URL = "https://mangakakalot.com"

    override fun scrapper(url: String): Scrapper {
        return Scrapper(url, MangaKakalotWorker)
    }

    override val worker: Worker = MangaKakalotWorker

    override suspend fun search(keyword: String): List<Scrapper> {
        /*Jsoup.connect("https://mangakakalot.com/read-is4cb158504873448")
            .get()
            .select("div.chapter-list div")
            .last()
            .select("span a")
            .attr("href")*/

        return TODO()
    }

    override fun chapterUrlOf(source: String, index: String): String {
        return chapterSchemeOf(source) + index
    }

    override fun chapterSchemeOf(source: String): String {
        return source.substring(0, source.indexOfLast { it == '_' } + 1)
    }

    override fun mangaNameOf(source: String): String {
        return source.substringAfter("chapter/").substringBefore("/chapter")
    }

    override fun chapterIndexOf(source: String): String {
        return source.substringAfterLast("_")
    }

    override fun chapterIndexOfDownloadUrl(source: String): String {
        return chapterIndexOf(source).substringBefore("/")
    }

    override fun formChapterUrl(name: String, chapter: String): String = "$BASE_URL/chapter/$name/chapter_$chapter"
}

private object MangaKakalotWorker : Worker {

    override suspend fun chaptersIndexes(url: String): List<String> {
        return Jsoup.connect(url)
            .get()
            .selectFirst("div.option_wrap select")
            .select("option")
            .map { it.attr("data-c") }
            .asReversed()
    }

    override suspend fun chaptersTitles(url: String): List<String> {
        return Jsoup.connect(url)
            .get()
            .selectFirst("div.option_wrap select")
            .select("option")
            .map { it.html() }
            .asReversed()
    }

    override suspend fun chaptersUrls(url: String): List<String> {
        return chaptersIndexes(url).map {
            MangaKakalot.chapterSchemeOf(url) + it
        }
    }

    override suspend fun chaptersTitlesIndexed(url: String): Map<String, String> {
        return Jsoup.connect(url)
            .get()
            .selectFirst("div.option_wrap select")
            .select("option")
            .map { it.attr("data-c") to it.html() }
            .asReversed()
            .toMap()
    }

    override suspend fun chapterDownloadUrls(url: String): List<String> {
        return Jsoup.connect(url)
            .get()
            .select("div.container-chapter-reader img")
            .map { it.attr("src") }
            .toList()
    }

    override suspend fun mangaDownloadUrls(url: String): Map<String, List<String>> {
        return chaptersUrls(url).associateWith {
            chapterDownloadUrls(MangaKakalot.chapterUrlOf(url, it))
        }
    }

    override suspend fun downloadChapter(url: String, path: String, newFolder: Boolean) {
        val chapter: String = MangaKakalot.chapterIndexOf(url)
        chapterDownloadUrls(url).forEachIndexed { index, element ->
                fileOf("$path/${"$chapter/".ifTrue(newFolder)}")
                    .path("${index + 1}.jpg")
                    .writeBytes(
                        Jsoup.connect(element)
                            .header("referer", "https://mangakakalot.com/")
                            .ignoreContentType(true)
                            .execute()
                            .bodyAsBytes()
                    )
            }
    }

    override suspend fun downloadChapterRange(url: String, path: String, from: String, to: String, newFolder: Boolean) {
        val manga: String = MangaKakalot.mangaNameOf(url)
        chaptersIndexes(url).filterWithContext {
            indexOf(it) in indexOf(from)..indexOf(to)
        }.map {
            MangaKakalot.formChapterUrl(manga, it)
        }.forEach {
            downloadChapter(it, "$path/${manga.ifTrue(newFolder)}", true)
        }
    }

    override suspend fun downloadManga(url: String, path: String, newFolder: Boolean) {
        val manga = MangaKakalot.mangaNameOf(url)
        chaptersUrls(url).forEach { chapter ->
            downloadChapter(chapter,
                "$path/${manga.ifTrue(newFolder)}", true)
        }
    }
}