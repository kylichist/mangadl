@file : Suppress("NOTHING_TO_INLINE")

package com.github.kylichist.mangadl.common

import java.io.File

inline fun File.path(next: String): File = File("$absolutePath/$next")
inline fun fileOf(path: String): File = File(path).apply { mkdirs() }

inline fun String.ifTrue(condition: Boolean): String = if (condition) this else ""

inline fun <A> Iterable<A>.filterWithContext(predicate: Iterable<A>.(A) -> Boolean): Iterable<A> = filter { predicate(it) }

inline fun String.replaceRegex(regex: String, replacement: String): String = replace(Regex(regex), replacement)