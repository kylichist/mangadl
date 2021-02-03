package com.github.kylichist.mangadl.manganelo

import com.github.kylichist.mangadl.common.*
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup

object MangaNelo : Client {

    override val BASE_URL = "https://manganelo.com"

    override val worker: Worker = MangaNeloWorker

    override fun scrapper(url: String): Scrapper {
        return Scrapper(url, MangaNeloWorker)
    }

    override suspend fun search(keyword: String): List<Manga> {
        val keywordFiltered = keyword.toLowerCase()
            .replaceRegex("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a")
            .replaceRegex("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e")
            .replaceRegex("ì|í|ị|ỉ|ĩ", "i")
            .replaceRegex("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o")
            .replaceRegex("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u")
            .replaceRegex("ỳ|ý|ỵ|ỷ|ỹ", "y")
            .replaceRegex("đ", "d")
            .replaceRegex(" ", "_")
            .replaceRegex("[^0-9a-z\\s]", "")
            .replaceRegex("^\\_+|\\_+\$", "")

        require(keywordFiltered.length > 2) {
            "Keyword length must be > 2!"
        }

        return JSONArray(
            Jsoup.connect("https://manganelo.com/getstorysearchjson")
                .data("searchword", keywordFiltered)
                .method(Connection.Method.POST)
                .execute()
                .body()
        ).filterIsInstance(JSONObject::class.java)
            .map {
                MangaNeloManga(
                    Jsoup.parse(it.getString("name")).text(),
                    it.getString("image"),
                    Jsoup.parse(it.getString("author")).text(),
                    it.getString("lastchapter"),
                    it.getString("id_encode")
                )
            }
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

    override fun formChapterUrl(name: String, chapter: String): String = "$BASE_URL/chapter/$name/chapter_$chapter"
}

private object MangaNeloWorker : Worker {

    override suspend fun chaptersIndexes(url: String): List<String> {
        return Jsoup.connect(url)
            .get()
            .selectFirst("div.panel-navigation select")
            .select("option")
            .map { it.attr("data-c") }
            .asReversed()
    }

    override suspend fun chaptersTitles(url: String): List<String> {
        return Jsoup.connect(url)
            .get()
            .selectFirst("div.panel-navigation select")
            .select("option")
            .map { it.html() }
            .asReversed()
    }

    override suspend fun chaptersUrls(url: String): List<String> {
        return chaptersIndexes(url).map {
            MangaNelo.chapterSchemeOf(url) + it
        }
    }

    override suspend fun chaptersTitlesIndexed(url: String): Map<String, String> {
        return Jsoup.connect(url)
            .get()
            .selectFirst("div.panel-navigation select")
            .select("option")
            .map { it.attr("data-c") to it.html() }
            .asReversed()
            .toMap()
    }

    override suspend fun chaptersUrlsIndexed(url: String): Map<String, String> {
        return chaptersIndexes(url).map {
            it to MangaNelo.chapterSchemeOf(url) + it
        }.toMap()
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
            chapterDownloadUrls(MangaNelo.chapterUrlOf(url, it))
        }
    }

    override suspend fun downloadChapter(url: String, path: String, newFolder: Boolean) {
        val chapter: String = MangaNelo.chapterIndexOf(url)
        chapterDownloadUrls(url).forEachIndexed { index, element ->
            fileOf("$path/${"$chapter/".ifTrue(newFolder)}")
                .path("${index + 1}.jpg")
                .writeBytes(
                    Jsoup.connect(element)
                        .header("referer", "https://manganelo.com/")
                        .ignoreContentType(true)
                        .execute()
                        .bodyAsBytes()
                )
        }
    }

    override suspend fun downloadChapterRange(url: String, path: String, from: String, to: String, newFolder: Boolean) {
        val manga: String = MangaNelo.mangaNameOf(url)
        chaptersIndexes(url).filterWithContext {
            indexOf(it) in indexOf(from)..indexOf(to)
        }.map {
            MangaNelo.formChapterUrl(manga, it)
        }.forEach {
            downloadChapter(it, "$path/${manga.ifTrue(newFolder)}", true)
        }
    }

    override suspend fun downloadManga(url: String, path: String, newFolder: Boolean) {
        val manga = MangaNelo.mangaNameOf(url)
        chaptersUrls(url).forEach { chapter ->
            downloadChapter(
                chapter,
                "$path/${manga.ifTrue(newFolder)}", true
            )
        }
    }
}

class MangaNeloManga(
    override val title: String,
    override val pictureUrl: String,
    override val author: String,
    override val lastChapter: String,
    override val id: String
) : Manga {
    override fun scrapper(chapter: String): Scrapper {
        return MangaNelo.scrapper(MangaNelo.formChapterUrl(id, chapter))
    }
}