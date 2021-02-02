package com.github.kylichist.mangadl.common

//TODO: update "docs"
interface Worker {
    //working with chapters' info
    /*
    *   ~url~/chapter_1 -> [1, 2, 3, .. , ~last-chapter-of-manga~]
    */
    suspend fun chaptersIndexes(url: String): List<String>
    /*
    * TODO!!
     */
    suspend fun chaptersTitles(url: String): List<String>
    /*
    *   ~url~/chapter_1 -> [~url~/chapter_1, ~url~/chapter_2, ~url~/chapter_3, .. , ~url~/~last-chapter-of-manga~]
    */
    suspend fun chaptersUrls(url: String): List<String>
    /*
    *   ~url~/chapter_1 -> [1 to "Chapter 1: Move 1", 2 to "..", 3 to "..", .. , ~last-chapter-of-manga~ to ~last-title-of-manga~]
    */
    suspend fun chaptersTitlesIndexed(url: String): Map<String, String>
    /*
    *   ~url~/chapter_1 -> [~url-to-download-picture-1-of-chapter-1~, ..]
    */
    suspend fun chapterDownloadUrls(url: String): List<String>
    /*
    *   ~url~/chapter_1 -> {
    *       1 to [~url-to-download-picture-1-of-chapter-1~, ..]
    *       ..
    *       n to [~url-to-download-picture-1-of-chapter-n~, ..]
    * }
    */
    suspend fun mangaDownloadUrls(url: String): Map<String, List<String>>

    //download
    /*
    * If newFolder:
    *   ~url~/chapter_1 -> Download pictures of chapter(url = ~url~/chapter_1) to folder(path = ~path~/)
    * Unless:
    *   ~url~/chapter_1 -> Download pictures of chapter(url = ~url~/chapter_1) to folder(path = ~path~/~manga-name~/)
    */
    suspend fun downloadChapter(url: String, path: String, newFolder: Boolean)
    /*
    * TODO!!
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

class Scrapper(private val url: String, private val worker: Worker) {
    suspend fun chaptersIndexes(): List<String> = worker.chaptersIndexes(url)
    suspend fun chaptersTitles(): List<String> = worker.chaptersTitles(url)
    suspend fun chaptersUrls(): List<String> = worker.chaptersUrls(url)
    suspend fun chaptersTitlesIndexed(): Map<String, String> = worker.chaptersTitlesIndexed(url)
    suspend fun chapterDownloadUrls(): List<String> = worker.chapterDownloadUrls(url)
    suspend fun mangaDownloadUrls(): Map<String, List<String>> = worker.mangaDownloadUrls(url)
    suspend fun downloadChapter(path: String, newFolder: Boolean = false) = worker.downloadChapter(url, path, newFolder)
    suspend fun downloadChapterRange(path: String, from: String, to: String, newFolder: Boolean = false) = worker.downloadChapterRange(url, path, from, to, newFolder)
    suspend fun downloadManga(path: String, newFolder: Boolean = false) = worker.downloadManga(url, path, newFolder)
}

interface Client {
    //basic properties
    /*
    * TODO!!
     */
    val BASE_URL: String
    val worker: Worker

    /*
    * TODO!!
     */
    fun scrapper(url: String): Scrapper
    /*
     * TODO!!
     */

    /*
     * TODO!!
     */
    suspend fun search(keyword: String): List<Scrapper>

    //extracting data
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
     *  TODO!!
     */
    fun chapterIndexOfDownloadUrl(source: String): String

    /*
    *   ps918650, 11 -> ~url~/chapter/ps918650/chapter_11
    */
    fun formChapterUrl(name: String, chapter: String): String
}

//TODO
data class Manga(val title: String, val picture: String)