import redux.RAction

class CheckUserAction: RAction
class UpdateUserAction(val userCheckDTO: UserCheckDTO): RAction
class ResetLoginUserStateAction: RAction
class LoginUserErrorAction: RAction

fun reducer(state: AppState, action: RAction) =
    when (action) {
        is CheckUserAction ->
            AppState(UserDataState(FetchState.Pending, state.userDataState.userCheckDTO, false))
        is UpdateUserAction ->
            AppState(UserDataState(FetchState.Fulfilled, action.userCheckDTO, false))
        is ResetLoginUserStateAction ->
            AppState(UserDataState(state.userDataState.fetchState, state.userDataState.userCheckDTO, false))
        is LoginUserErrorAction ->
            AppState(UserDataState(state.userDataState.fetchState, state.userDataState.userCheckDTO, true))
        else -> state
    }