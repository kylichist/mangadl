package com.github.kylichist.mangakakalotkt

import org.jsoup.Jsoup
import java.io.File

object Kakalot {
    const val BASE_KAKALOT_URL = "https://mangakakalot.com/chapter/"

    /*
    *   ~url~/chapter_1 -> [1, 2, 3, .. , ~last-chapter-of-manga~]
    */
    fun chapters(url: String): List<String> {
        return Jsoup.connect(url)
            .get()
            .select("div.option_wrap select option")
            .map { it.attr("data-c") }
            .asReversed()
    }

    /*
    *   ~url~/chapter_1 -> [1 to "Chapter 1: Move 1", 2 to "..", 3 to "..", .. , ~last-chapter-of-manga~ to ~last-title-of-manga~]
    */
    fun chaptersWithTitles(url: String): List<Pair<String, String>> {
        return Jsoup.connect(url)
            .get()
            .select("div.option_wrap select option")
            .map { it.attr("data-c") to it.html() }
            .asReversed()
    }

    /*
    *   ~url~/chapter_2 -> [~url~/chapter_1, ~url~/chapter_2, ~url~/chapter_3, .. , ~url~/~last-chapter-of-manga~]
    */
    fun chaptersUrls(url: String): List<String> {
        return chapters(url).map {
            url.chapterScheme() + it
        }
    }

    /*
    * If newFolder:
    *   ~url~/chapter_3 -> Download pictures of chapter(url = ~url~/chapter_3) to folder(path = ~path~/)
    * Unless:
    *   ~url~/chapter_3 -> Download pictures of chapter(url = ~url~/chapter_3) to folder(path = ~path~/~manga-name~/)
    */
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

    /*
    *   ~url~/chapter_4 -> [~url-to-download-picture-1-of-chapter-4~, ..]
    */
    fun downloadChapterUrls(url: String): List<String> {
        return Jsoup.connect(url)
            .get()
            .select("div#vungdoc img")
            .toList().map { it.attr("src") }
    }

    /*
    * If newFolder:
    *   ~url~/chapter_5 -> Download whole manga with all pictures to folder(path = ~path~/)
    * Unless:
    *   ~url~/chapter_5 -> Download whole manga with all pictures to folder(path = ~path~/~manga-name~)
    */
    fun downloadManga(url: String, path: String, newFolder: Boolean = false) {
        url.mangaName().let { manga ->
            chaptersUrls(url).forEach { chapter ->
                downloadChapter(chapter, "$path/${"$manga/".takeIf { newFolder } ?: ""}${chapter.chapterIndex()}")
            }
        }
    }

    /*
    *   ~url~/chapter_6 -> {
    *       1 to [~url-to-download-picture-1-of-chapter-1~, ..]
    *       ..
    *       n to [~url-to-download-picture-1-of-chapter-n~, ..]
    * }
    */
    fun downloadMangaUrls(url: String): Map<String, List<String>> {
        return chapters(url).associateWith {
            downloadChapterUrls(url.chapterUrl(it))
        }
    }

    /*
    *   ~url~/chapter_7, 1 -> ~url~/chapter_1
    */
    fun String.chapterUrl(index: String) = chapterScheme() + index

    /*
    *   ~url~/chapter_8 -> ~url~/chapter_
    */
    fun String.chapterScheme() = substring(0, indexOfLast { it == '_' } + 1)

    /*
    *   ~url~/~manga-name~/chapter_9 -> ~manga-name~
    */
    fun String.mangaName() = substringAfter("chapter/").substringBefore("/chapter")

    /*
    *   ~url~/chapter_10 -> 10
    */
    fun String.chapterIndex() = substringAfterLast("_")

    /*
    *   ps918650, 11 -> ~url~/chapter/ps918650/chapter_11
    */
    fun formChapterUrl(name: String, chapter: String): String = "$BASE_KAKALOT_URL/$name/$chapter"
}

internal fun File.path(next: String) = File("$absolutePath/$next")