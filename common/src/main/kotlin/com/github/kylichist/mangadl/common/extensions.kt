package com.github.kylichist.mangadl.common

import java.io.File

suspend inline fun Scrapper.downloadChapter(file: File, newFolder: Boolean = false) = downloadChapter(file.absolutePath, newFolder)
suspend inline fun Scrapper.downloadChapterRange(file: File, from: String, to: String, newFolder: Boolean = false) = downloadChapterRange(file.absolutePath, from, to, newFolder)
suspend inline fun Scrapper.downloadManga(file: File, newFolder: Boolean = false) = downloadManga(file.absolutePath, newFolder)

inline fun Client.register(producer: () -> String): Scrapper = scrapper(producer())