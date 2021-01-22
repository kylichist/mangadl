@file : Suppress("NOTHING_TO_INLINE")

package com.github.kylichist.mangadl.common

import java.io.File

inline fun File.path(next: String): File = File("$absolutePath/$next")
inline fun fileOf(path: String): File = File(path).apply { mkdirs() }