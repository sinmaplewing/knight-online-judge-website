import kotlinx.coroutines.*
import kotlinx.html.classes
import react.*
import react.dom.*
import react.redux.rConnect
import react.router.dom.routeLink
import redux.RAction
import redux.WrapperAction

external interface LoginStatusProps: RProps {
    var isFetchPending: Boolean
    var userCheckDTO: UserCheckDTO
    var onFetchPending: () -> Unit
}

internal interface LoginStatusStateProps: RProps {
    var isFetchPending: Boolean
    var userCheckDTO: UserCheckDTO
}

internal interface LoginStatusDispatchProps: RProps {
    var onFetchPending: () -> Unit
}

val connectedLoginStatus: RClass<LoginStatusProps> =
    rConnect<AppState, RAction, WrapperAction, RProps, LoginStatusStateProps, LoginStatusDispatchProps, LoginStatusProps>({
        state, _ ->
            isFetchPending = state.userDataState.fetchState == FetchState.Pending
            userCheckDTO = state.userDataState.userCheckDTO
    }, {
        dispatch, _ ->
            onFetchPending = {
                val mainScope = MainScope()
                mainScope.launch {
                    val remoteUserCheckDTO = Fetcher.createUserCheckFetcher().fetch()
                    dispatch(UpdateUserAction(remoteUserCheckDTO))
                }
            }
    })(LoginStatus::class.rClass)

class LoginStatus: RComponent<LoginStatusProps, RState>() {
    override fun RBuilder.render() {
        div {
            attrs.classes = setOf("ml-md-auto")

            if (props.isFetchPending) {
                props.onFetchPending()
            } else {
                if (props.userCheckDTO.userId != null) {
                    div {
                        attrs.classes = setOf("navbar-text")

                        +"歡迎光臨，${props.userCheckDTO.name}！"
                    }
                    routeLink("/logout", className = "btn btn-primary") {
                        +"登出"
                    }
                } else {
                    div {
                        attrs.classes = setOf("navbar-text")

                        +"歡迎光臨，訪客！"
                    }
                    routeLink("/login", className = "btn btn-primary") {
                        +"登入"
                    }
                    routeLink("/register", className = "btn btn-primary") {
                        +"註冊"
                    }
                }
            }
        }
    }
}

fun RBuilder.loginStatus(handler: RElementBuilder<LoginStatusProps>.() -> Unit): ReactElement =
    child(LoginStatus::class, handler)