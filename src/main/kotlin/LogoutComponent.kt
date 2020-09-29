import kotlinx.coroutines.*
import kotlinx.html.classes
import react.*
import react.dom.*
import react.redux.rConnect
import react.router.dom.redirect
import react.router.dom.routeLink
import redux.RAction
import redux.WrapperAction

external interface LogoutComponentProps: RProps {
    var isUserIdExisted: Boolean
    var onLogout: () -> Unit
}

private interface LogoutStateProps: RProps {
    var isUserIdExisted: Boolean
}

private interface LogoutDispatchProps: RProps {
    var onLogout: () -> Unit
}

val connectedLogoutComponent: RClass<LogoutComponentProps> =
    rConnect<AppState, RAction, WrapperAction, RProps, LogoutStateProps, LogoutDispatchProps, LogoutComponentProps>({
        state, _ ->
            isUserIdExisted = state.userDataState.userCheckDTO.userId != null
    }, {
        dispatch, _ ->
            onLogout = {
                val mainScope = MainScope()
                mainScope.launch {
                    Fetcher.createUserLogoutFetcher().fetch("POST")
                    dispatch(CheckUserAction())
                }
            }
    })(LogoutComponent::class.rClass)

class LogoutComponent: RComponent<LogoutComponentProps, RState>() {
    override fun RBuilder.render() {
        if (props.isUserIdExisted) {
            props.onLogout()
        } else {
            redirect(to = "/?success=true")
        }
    }
}

fun RBuilder.logoutComponent(handler: RElementBuilder<LogoutComponentProps>.() -> Unit): ReactElement =
    child(LogoutComponent::class, handler)