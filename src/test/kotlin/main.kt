import com.github.kylichist.mangakakalotkt.Kakalot
import com.github.kylichist.mangakakalotkt.Kakalot.chapters
import com.github.kylichist.mangakakalotkt.Kakalot.chaptersWithTitles

const val TEST_URL = "https://mangakakalot.com/chapter/ps918650/chapter_1"
const val TEST_PATH = "C:/Users/peter/Downloads/manga-tests"

fun main() {
    Kakalot.downloadManga(TEST_URL, TEST_PATH, true)
}