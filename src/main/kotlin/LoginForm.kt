import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.LinearDimension
import kotlinx.css.margin
import kotlinx.css.width
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import react.redux.rConnect
import react.router.dom.redirect
import redux.RAction
import redux.WrapperAction
import styled.css
import styled.styledDiv

external interface LoginFormState: RState {
    var username: String
    var password: String
}

external interface LoginFormProps: RProps {
    var isUserIdExisted: Boolean
    var isError: Boolean
    var onSubmit: (String, String) -> Unit
}

internal interface LoginFormStateProps: RProps {
    var isUserIdExisted: Boolean
    var isError: Boolean
}

internal interface LoginFormDispatchProps: RProps {
    var onSubmit: (String, String) -> Unit
}

val connectedLoginForm: RClass<LoginFormProps> =
    rConnect<AppState, RAction, WrapperAction, RProps, LoginFormStateProps, LoginFormDispatchProps, LoginFormProps>({
        state, _ ->
            isError = state.userDataState.isLoginError
            isUserIdExisted = state.userDataState.userCheckDTO.userId != null
    }, {
        dispatch, _ ->
            onSubmit = { username, password ->
                dispatch(ResetLoginUserStateAction())

                val mainScope = MainScope()
                mainScope.launch {
                    val result = Fetcher.createUserLoginFetcher().fetch(
                        "POST",
                        UserLoginDTO(username, password)
                    )
                    if (result?.OK == true) {
                        dispatch(CheckUserAction())
                    } else {
                        dispatch(LoginUserErrorAction())
                    }
                }
            }
    })(LoginForm::class.rClass)

class LoginForm: RComponent<LoginFormProps, LoginFormState>() {
    override fun LoginFormState.init() {
        username = ""
        password = ""
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                width = LinearDimension("80%")
                margin = "30px auto"
            }

            if (props.isUserIdExisted) {
                redirect(to = "/")
            }
            else {
                if (props.isError) {
                    div {
                        attrs.classes = setOf("alert", "alert-danger")
                        +"登入失敗！請確認您輸入的帳號密碼是否正確。"
                    }
                }

                form {
                    attrs.onSubmitFunction = {
                        it.preventDefault()
                        props.onSubmit(state.username, state.password)
                    }

                    div {
                        attrs.classes = setOf("form-group")

                        label {
                            attrs.htmlFor = "usernameInput"
                            +"帳號"
                        }

                        input {
                            attrs.type = InputType.text
                            attrs.id = "usernameInput"
                            attrs.classes = setOf("form-control")
                            attrs.value = state.username

                            attrs.onChangeFunction = {
                                val target = it.target as HTMLInputElement
                                setState {
                                    username = target.value
                                }
                            }
                        }
                    }
                    div {
                        attrs.classes = setOf("form-group")

                        label {
                            attrs.htmlFor = "passwordInput"
                            +"密碼"
                        }

                        input {
                            attrs.type = InputType.password
                            attrs.id = "passwordInput"
                            attrs.classes = setOf("form-control")

                            attrs.onChangeFunction = {
                                val target = it.target as HTMLInputElement
                                setState {
                                    password = target.value
                                }
                            }
                        }
                    }

                    button {
                        attrs.type = ButtonType.submit
                        attrs.classes = setOf("btn", "btn-primary")

                        +"登入"
                    }
                }
            }
        }
    }
}

fun RBuilder.loginForm(handler: RElementBuilder<LoginFormProps>.() -> Unit): ReactElement =
    child(LoginForm::class, handler)