import kotlinx.browser.window
import kotlinx.coroutines.await

const val DATA_URL = "http://0.0.0.0:8081"

class Fetcher<T>(val path: String) {
    companion object {
        fun createProblemsFetcher() = Fetcher<ProblemsData>("$DATA_URL/problems")
    }

    suspend fun fetch(): T =
        window.fetch(path).await()
            .json().await()
            .unsafeCast<T>()
}

data class ProblemsData(
    val data: Array<ProblemData>
)

data class ProblemData(
    val id: String,
    val title: String
)