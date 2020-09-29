import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.*
import react.router.dom.redirect
import styled.css
import styled.styledDiv

external interface UserRegisterFormState: RState {
    var userPostDTO: UserPostDTO
    var isSubmitted: Boolean
    var isResultGet: Boolean
}

class UserRegisterForm: RComponent<RProps, UserRegisterFormState>() {
    override fun UserRegisterFormState.init() {
        isSubmitted = false
        isResultGet = false
        userPostDTO = UserPostDTO(
            "",
            "",
            "",
            ""
        )
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                width = LinearDimension("80%")
                margin = "30px auto"
            }

            if (state.isResultGet) {
                redirect(to = "/")
            } else {
                h1 { +"註冊會員" }

                form {
                    attrs.onSubmitFunction = onSubmitFunction@{
                        it.preventDefault()
                        if (state.isSubmitted) return@onSubmitFunction

                        val mainScope = MainScope()
                        mainScope.launch {
                            Fetcher.createUserRegisterFetcher().fetch(
                                "POST",
                                state.userPostDTO
                            )
                            setState {
                                isResultGet = true
                            }
                        }
                    }

                    formInputComponent {
                        attrs.inputType = InputType.text
                        attrs.id = "usernameInput"
                        attrs.name = "帳號"
                        attrs.currentValue = state.userPostDTO.username
                        attrs.onChange = {
                            val target = it.target as HTMLInputElement
                            setState {
                                userPostDTO.username = target.value
                            }
                        }
                    }

                    formInputComponent {
                        attrs.inputType = InputType.password
                        attrs.id = "passwordInput"
                        attrs.name = "密碼"
                        attrs.currentValue = state.userPostDTO.password
                        attrs.onChange = {
                            val target = it.target as HTMLInputElement
                            setState {
                                userPostDTO.password = target.value
                            }
                        }
                    }

                    formInputComponent {
                        attrs.inputType = InputType.text
                        attrs.id = "nameInput"
                        attrs.name = "顯示名稱"
                        attrs.currentValue = state.userPostDTO.name
                        attrs.onChange = {
                            val target = it.target as HTMLInputElement
                            setState {
                                userPostDTO.name = target.value
                            }
                        }
                    }

                    formInputComponent {
                        attrs.inputType = InputType.text
                        attrs.id = "emailInput"
                        attrs.name = "電子郵件信箱"
                        attrs.currentValue = state.userPostDTO.email
                        attrs.onChange = {
                            val target = it.target as HTMLInputElement
                            setState {
                                userPostDTO.email = target.value
                            }
                        }
                    }

                    button {
                        attrs.type = ButtonType.submit
                        attrs.classes = setOf("btn", "btn-primary")

                        +"註冊"
                    }
                }
            }
        }
    }
}

fun RBuilder.userRegisterForm(handler: RElementBuilder<RProps>.() -> Unit): ReactElement =
    child(UserRegisterForm::class, handler)