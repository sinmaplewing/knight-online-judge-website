import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.dom.NavigatorLanguage
import org.w3c.fetch.*

const val DATA_URL = "https://127.0.0.1:8082"

class Fetcher<T>(val path: String) {
    companion object {
        fun createProblemsFetcher() = Fetcher<ProblemsData>("$DATA_URL/problems")
        fun createProblemDetailFetcher(id: Int) = Fetcher<ProblemDetailWrapperData>("$DATA_URL/problems/$id")
        fun createProblemAllFetcher(id: Int) = Fetcher<ProblemFormWrapperData>("$DATA_URL/problems/$id/all")
        fun createProblemCreateFetcher() = Fetcher<JustFetch>("$DATA_URL/problems")
        fun createProblemEditFetcher(id: Int) = Fetcher<JustFetch>("$DATA_URL/problems/$id")
        fun createProblemDeleteFetcher(id: Int) = Fetcher<JustFetch>("$DATA_URL/problems/$id")

        fun createUsersFetcher() = Fetcher<UsersData>("$DATA_URL/users")
        fun createUserCheckFetcher() = Fetcher<UserCheckDTO>("$DATA_URL/users/check")
        fun createUserLoginFetcher() = Fetcher<FetchResult>("$DATA_URL/users/login")
        fun createUserLogoutFetcher() = Fetcher<FetchResult>("$DATA_URL/users/logout")
        fun createUserRegisterFetcher() = Fetcher<JustFetch>("$DATA_URL/users")

        fun createSubmissionsFetcher() = Fetcher<SubmissionsData>("$DATA_URL/submissions")
        fun createSubmitCodeFetcher() = Fetcher<JustFetch>("$DATA_URL/submissions")
        fun createSubmissionsRestartFetcher() = Fetcher<JustFetch>("$DATA_URL/submissions/restart")
        fun createSubmissionRestartFetcher(id: Int) = Fetcher<JustFetch>("$DATA_URL/submissions/$id/restart")
    }

    fun createHeaders(method: String): Headers {
        val headers = Headers()
        if (method == "POST" || method == "PUT") {
            headers.append("Content-Type", "application/json")
        }
        return headers
    }

    suspend fun fetch(method: String = "GET", data: Any? = null): T =
         window.fetch(
            path, RequestInit(
                method,
                mode = RequestMode.CORS,
                credentials = RequestCredentials.INCLUDE,
                headers = createHeaders(method),
                body = if (data != null) JSON.stringify(data) else null
            )
        ).await().json().await().unsafeCast<T>()
}

data class ProblemsData(
    val data: Array<ProblemData>,
    val isEditable: Boolean
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

data class ProblemFormWrapperData(
    val data: ProblemFormArrayData
)

data class ProblemFormArrayData(
    var id: String?,
    var title: String,
    var description: String,
    var testCases: Array<TestCaseData>
)

data class ProblemFormData(
    var id: String?,
    var title: String,
    var description: String,
    var testCases: MutableList<TestCaseData>
)

data class TestCaseData(
    var id: String?,
    var input: String,
    var expectedOutput: String,
    var comment: String,
    var score: Int,
    var timeOutSeconds: Double
)

data class ProblemPostDTO(
    val title: String,
    val description: String,
    val testCases: List<TestCasePostDTO>
)

data class ProblemPutDTO(
    val id: String,
    val title: String,
    val description: String,
    val testCases: List<TestCasePutDTO>
)

data class TestCasePostDTO(
    val input: String,
    val expectedOutput: String,
    val comment: String,
    val score: Int,
    val timeOutSeconds: Double
)

data class TestCasePutDTO(
    val id: String?,
    val input: String,
    val expectedOutput: String,
    val comment: String,
    val score: Int,
    val timeOutSeconds: Double
)

data class UsersData(
    val data: Array<UserData>
)

data class UserData(
    val id: String,
    val name: String,
    val solvedProblemCount: String
)

data class UserPostDTO (
    var username: String,
    var password: String,
    var name: String,
    var email: String
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
    val data: Array<SubmissionData>,
    val isRefreshable: Boolean
)

data class SubmissionData(
    val id: String,
    val name: String,
    val problemId: String,
    val title: String,
    val language: String,
    val result: String,
    val executedTime: String,
    val isRefreshable: Boolean
)

data class SubmissionPostDTO(
    var language: String,
    var code: String,
    var problemId: Int
)

data class FetchResult(
    val OK: Boolean?
)

class JustFetch