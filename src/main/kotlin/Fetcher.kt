import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.dom.NavigatorLanguage
import org.w3c.fetch.*

const val DATA_URL = "https://127.0.0.1:8082"

class Fetcher<T>(val path: String) {
    companion object {
        fun createProblemsFetcher() = Fetcher<ProblemsData>("$DATA_URL/problems")
        fun createProblemDetailFetcher(id: Int) = Fetcher<ProblemDetailWrapperData>("$DATA_URL/problems/$id")

        fun createUsersFetcher() = Fetcher<UsersData>("$DATA_URL/users")
        fun createUserCheckFetcher() = Fetcher<UserCheckDTO>("$DATA_URL/users/check")
        fun createUserLoginFetcher() = Fetcher<FetchResult>("$DATA_URL/users/login")
        fun createUserLogoutFetcher() = Fetcher<FetchResult>("$DATA_URL/users/logout")

        fun createSubmissionsFetcher() = Fetcher<SubmissionsData>("$DATA_URL/submissions")
    }

    fun createHeaders(method: String): Headers {
        val headers = Headers()
        if (method == "POST") {
            headers.append("Content-Type", "application/json")
        }
        return headers
    }

    suspend fun fetch(method: String = "GET", data: Any? = null): T =
        window.fetch(path, RequestInit(
            method,
            mode = RequestMode.CORS,
            credentials = RequestCredentials.INCLUDE,
            headers = createHeaders(method),
            body = if (data != null) JSON.stringify(data) else null
        )).await()
            .json().await()
            .unsafeCast<T>()
}

data class ProblemsData(
    val data: Array<ProblemData>
)

data class ProblemData(
    val id: String,
    val title: String,
    val isSubmitted: String? = null,
    val isAccepted: String? = null
)

data class ProblemDetailWrapperData(
    val data: ProblemDetailData
)

data class ProblemDetailData(
    val id: String,
    val title: String,
    val description: String
)

data class UsersData(
    val data: Array<UserData>
)

data class UserData(
    val id: String,
    val name: String,
    val solvedProblemCount: String
)

data class UserLoginDTO (
    val username: String,
    val password: String
)

data class UserCheckDTO (
    val userId: Int? = null,
    val name: String = "",
    val authority: Int = 0
)

data class SubmissionsData(
    val data: Array<SubmissionData>
)

data class SubmissionData(
    val id: String,
    val name: String,
    val problemId: String,
    val title: String,
    val language: String,
    val result: String,
    val executedTime: String
)

data class FetchResult(
    val OK: Boolean?
)