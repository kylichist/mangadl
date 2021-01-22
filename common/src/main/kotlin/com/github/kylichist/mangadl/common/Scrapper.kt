package com.github.kylichist.mangadl.common

//TODO: update "docs"
abstract class Scrapper(url: String) {
    //working with chapters' info
    /*
    *   ~url~/chapter_1 -> [1, 2, 3, .. , ~last-chapter-of-manga~]
    */
    abstract suspend fun chaptersIndexes(): List<String>
    /*
    * TODO!!
     */
    abstract suspend fun chaptersTitles(): List<String>
    /*
    *   ~url~/chapter_1 -> [~url~/chapter_1, ~url~/chapter_2, ~url~/chapter_3, .. , ~url~/~last-chapter-of-manga~]
    */
    abstract suspend fun chaptersUrls(): List<String>
    /*
    *   ~url~/chapter_1 -> [1 to "Chapter 1: Move 1", 2 to "..", 3 to "..", .. , ~last-chapter-of-manga~ to ~last-title-of-manga~]
    */
    abstract suspend fun chaptersTitlesIndexed(): Map<String, String>
    /*
    *   ~url~/chapter_1 -> [~url-to-download-picture-1-of-chapter-1~, ..]
    */
    abstract suspend fun chapterDownloadUrls(): List<String>
    /*
    *   ~url~/chapter_1 -> {
    *       1 to [~url-to-download-picture-1-of-chapter-1~, ..]
    *       ..
    *       n to [~url-to-download-picture-1-of-chapter-n~, ..]
    * }
    */
    abstract suspend fun mangaDownloadUrls(): Map<String, List<String>>

    //download
    /*
    * If newFolder:
    *   ~url~/chapter_1 -> Download pictures of chapter(url = ~url~/chapter_1) to folder(path = ~path~/)
    * Unless:
    *   ~url~/chapter_1 -> Download pictures of chapter(url = ~url~/chapter_1) to folder(path = ~path~/~manga-name~/)
    */
    abstract suspend fun downloadChapter(path: String, newFolder: Boolean = false)
    /*
    * TODO!!
     */
    abstract suspend fun downloadChapterRange(path: String, from: String, to: String, newFolder: Boolean = false)
    /*
    * If newFolder:
    *   ~url~/chapter_1 -> Download whole manga with all pictures to folder(path = ~path~/)
    * Unless:
    *   ~url~/chapter_1 -> Download whole manga with all pictures to folder(path = ~path~/~manga-name~)
    */
    abstract suspend fun downloadManga(path: String, newFolder: Boolean = false)
}

interface Client {
    //basic properties
    /*
    * TODO!!
     */
    val BASE_URL: String

    /*
    * TODO!!
     */
    fun scrapperOf(url: String): Scrapper

    //extracting data
    /*
    *   ~url~/chapter_2, 1 -> ~url~/chapter_1
    */
    fun chapterUrlOf(source: String): String
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