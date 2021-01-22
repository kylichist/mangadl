package com.github.kylichist.mangadl.mangakakalot

import com.github.kylichist.mangadl.common.path
import org.jsoup.Jsoup
import java.io.File

object MangaKakalot {
    const val BASE_KAKALOT_URL = "https://mangakakalot.com/chapter/"

    fun chapters(url: String): List<String> {
        return Jsoup.connect(url)
            .get()
            .select("div.option_wrap select option")
            .map { it.attr("data-c") }
            .asReversed()
    }

    fun chaptersWithTitles(url: String): List<Pair<String, String>> {
        return Jsoup.connect(url)
            .get()
            .select("div.option_wrap select option")
            .map { it.attr("data-c") to it.html() }
            .asReversed()
    }

    fun chaptersUrls(url: String): List<String> {
        return chapters(url).map {
            url.chapterScheme() + it
        }
    }

    fun downloadChapter(url: String, path: String, newFolder: Boolean = false) {
        (url.mangaName() to url.chapterIndex()).let { (manga, chapter) ->
            downloadChapterUrls(url)
                .forEachIndexed { index, element ->
                    File("$path/${"$manga-chapter$chapter/".takeIf { newFolder } ?: ""}")
                        .apply { mkdirs() }
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
    }

    fun downloadChapterRange(url: String, path: String, from: String, to: String, newFolder: Boolean = false) {
        //TODO
        (url.mangaName() to url.chapterIndex()).let { (manga, chapter) ->
            downloadChapterUrls(url).apply {
                filter { current ->
                    indexOf(current) in indexOf(from)..indexOf(to)
                }
            }.forEachIndexed { index, element ->
                    File("$path/${"$manga-chapter$chapter/".takeIf { newFolder } ?: ""}")
                        .apply { mkdirs() }
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
    }

    fun downloadChapterUrls(url: String): List<String> {
        return Jsoup.connect(url)
            .get()
            .select("div#vungdoc img")
            .toList().map { it.attr("src") }
    }

    fun downloadManga(url: String, path: String, newFolder: Boolean = false) {
        url.mangaName().let { manga ->
            chaptersUrls(url).forEach { chapter ->
                downloadChapter(chapter, "$path/${"$manga/".takeIf { newFolder } ?: ""}${chapter.chapterIndex()}")
            }
        }
    }

    fun downloadMangaUrls(url: String): Map<String, List<String>> {
        return chapters(url).associateWith {
            downloadChapterUrls(url.chapterUrl(it))
        }
    }

    fun String.chapterUrl(index: String) = chapterScheme() + index

    fun String.chapterScheme() = substring(0, indexOfLast { it == '_' } + 1)

    fun String.mangaName() = substringAfter("chapter/").substringBefore("/chapter")

    fun String.chapterIndex() = substringAfterLast("_")

    fun formChapterUrl(name: String, chapter: String): String = "$BASE_KAKALOT_URL/$name/$chapter"
}
