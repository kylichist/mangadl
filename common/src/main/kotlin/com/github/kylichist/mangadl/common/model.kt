package com.github.kylichist.mangadl.common

/*
 * Base of the worker class implementation
 */
interface Worker {
    /*
     * ~url~/chapter_1 -> [1, 2, 3, .. , ~last-chapter-of-manga~]
     */
    suspend fun chaptersIndexes(url: String): List<String>

    /*
     * ~url~/chapter_1 -> [Chapter 1: Move 1, .. , ~last-title-of-manga~]
     */
    suspend fun chaptersTitles(url: String): List<String>

    /*
     * ~url~/chapter_1 -> [~url~/chapter_1, ~url~/chapter_2, ~url~/chapter_3, .. , ~url~/~last-chapter-of-manga~]
     */
    suspend fun chaptersUrls(url: String): List<String>

    /*
     * ~url~/chapter_1 -> {
     *     1 to "Chapter 1: Move 1",
     *     .. ,
     *     ~last-chapter-of-manga~ to ~last-title-of-manga~
     * }
     */
    suspend fun chaptersTitlesIndexed(url: String): Map<String, String>

    /*
     * ~url~/chapter_1 -> {
     *     1 to ~url~/chapter_1,
     *     .. ,
     *     ~last-chapter-of-manga~ to ~url~/chapter_~last-chapter-of-manga~
     * }
     */
    suspend fun chaptersUrlsIndexed(url: String): Map<String, String>

    /*
     * ~url~/chapter_1 -> [~url-to-download-picture-1-of-chapter-1~, ..]
     */
    suspend fun chapterDownloadUrls(url: String): List<String>

    /*
     * ~url~/chapter_1 -> {
     *     1 to [~url-to-download-picture-1-of-chapter-1~, ..],
     *     .. ,
     *     ~last-chapter-of-manga~ to [~url-to-download-picture-1-of-last-chapter~, ..]
     * }
     */
    suspend fun mangaDownloadUrls(url: String): Map<String, List<String>>

    /*
     * If newFolder:
     *   ~url~/chapter_1 -> Download pictures of chapter(url = ~url~/chapter_1) to folder(path = ~path~/)
     * Unless:
     *   ~url~/chapter_1 -> Download pictures of chapter(url = ~url~/chapter_1) to folder(path = ~path~/~manga-name~/)
     */
    suspend fun downloadChapter(url: String, path: String, newFolder: Boolean)

    /*
     * If newFolder:
     *   ~url~/chapter_1 -> Download pictures of chapters from [from] to [to] to folder(path = ~path~/)
     * Unless:
     *   ~url~/chapter_1 -> Download pictures of chapters from [from] to [to] to folder(path = ~path~/~manga-name~/)
     */
    suspend fun downloadChapterRange(url: String, path: String, from: String, to: String, newFolder: Boolean)

    /*
     * If newFolder:
     *   ~url~/chapter_1 -> Download whole manga with all pictures to folder(path = ~path~/)
     * Unless:
     *   ~url~/chapter_1 -> Download whole manga with all pictures to folder(path = ~path~/~manga-name~)
     */
    suspend fun downloadManga(url: String, path: String, newFolder: Boolean)
}

/*
 * Worker wrapper with a default url for all methods
 */
class Scrapper(val url: String, val worker: Worker) {
    suspend fun chaptersIndexes(): List<String> = worker.chaptersIndexes(url)
    suspend fun chaptersTitles(): List<String> = worker.chaptersTitles(url)
    suspend fun chaptersUrls(): List<String> = worker.chaptersUrls(url)
    suspend fun chaptersTitlesIndexed(): Map<String, String> = worker.chaptersTitlesIndexed(url)
    suspend fun chaptersUrlsIndexed(): Map<String, String> = worker.chaptersUrlsIndexed(url)
    suspend fun chapterDownloadUrls(): List<String> = worker.chapterDownloadUrls(url)
    suspend fun mangaDownloadUrls(): Map<String, List<String>> = worker.mangaDownloadUrls(url)
    suspend fun downloadChapter(path: String, newFolder: Boolean = false) = worker.downloadChapter(url, path, newFolder)
    suspend fun downloadChapterRange(path: String, from: String, to: String, newFolder: Boolean = false) = worker.downloadChapterRange(url, path, from, to, newFolder)
    suspend fun downloadManga(path: String, newFolder: Boolean = false) = worker.downloadManga(url, path, newFolder)
}

/*
 * Base of the client class implementation
 */
interface Client {
    /*
     * Base url of a manga site
     */
    val BASE_URL: String

    /*
     * Worker that does network requests to the site
     */
    val worker: Worker

    /*
    * Scrapper registered on a [url]
     */
    fun scrapper(url: String): Scrapper

    /*
     * Search for manga by a keyword
     */
    suspend fun search(keyword: String): List<Manga>

    /*
    *   ~url~/chapter_2, 1 -> ~url~/chapter_1
    */
    fun chapterUrlOf(source: String, index: String): String

    /*
    *   ~url~/chapter_1 -> ~url~/chapter_
    */
    fun chapterSchemeOf(source: String): String

    /*
    *   ~url~/~manga-name~/chapter_1 -> ~manga-name~
    */
    fun mangaNameOf(source: String): String

    /*
    *   ~url~/chapter_1 -> 1
    */
    fun chapterIndexOf(source: String): String

    /*
    *   ps918650, 11 -> ~url~/chapter/ps918650/chapter_11
    */
    fun formChapterUrl(name: String, chapter: String): String
}

/*
 * Base of the manga class implementation
 */
interface Manga {
    /*
     * Manga title(name)
     */
    val title: String

    /*
     * Url to preview picture
     */
    val pictureUrl: String

    /*
     * The author of the manga
     */
    val author: String

    /*
     * Last chapter index
     */
    val lastChapter: String

    /*
     * Manga special id, used to form a (manga/chapter) url
     */
    val id: String

    /*
     * Produces a Scrapper registered on url of chapter [chapter]
     */
    fun scrapper(chapter: String = "1"): Scrapper
}