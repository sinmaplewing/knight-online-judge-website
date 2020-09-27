enum class FetchState {
    Pending, Rejected, Fulfilled
}

data class UserDataState (
    val fetchState: FetchState,
    val userCheckDTO: UserCheckDTO,
    val isLoginError: Boolean = false
)

data class AppState(
    val userDataState: UserDataState
)

fun createAppState() = AppState(UserDataState(FetchState.Pending, UserCheckDTO()))

