package com.github.kylichist.mangadl.common

import java.io.File

suspend fun Scrapper.downloadChapter(file: File, newFolder: Boolean = false) = downloadChapter(file.absolutePath, newFolder)
suspend fun Scrapper.downloadChapterRange(file: File, from: String, to: String, newFolder: Boolean = false) = downloadChapterRange(file.absolutePath, from, to, newFolder)
suspend fun Scrapper.downloadManga(file: File, newFolder: Boolean = false) = downloadManga(file.absolutePath, newFolder)