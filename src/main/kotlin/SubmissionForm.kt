import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.*
import react.router.dom.redirect
import styled.css
import styled.styledDiv

external interface SubmissionFormProps: RProps {
    var problemId: Int
}

external interface SubmissionFormState: RState {
    var submissionPostDTO: SubmissionPostDTO
    var isSubmitted: Boolean
    var isResultGet: Boolean
}

class SubmissionForm: RComponent<SubmissionFormProps, SubmissionFormState>() {
    override fun SubmissionFormState.init() {
        isSubmitted = false
        isResultGet = false
        submissionPostDTO = SubmissionPostDTO(
            "kotlin",
            "",
            -1 // 會在送出時填入
        )
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                width = LinearDimension("80%")
                margin = "30px auto"
            }

            if (state.isResultGet) {
                redirect(to = "/submissions")
            } else {
                h2 { +"上傳程式碼" }

                form {
                    attrs.onSubmitFunction = onSubmitFunction@{
                        it.preventDefault()
                        if (state.isSubmitted) return@onSubmitFunction

                        val mainScope = MainScope()
                        mainScope.launch {
                            Fetcher.createSubmitCodeFetcher().fetch(
                                "POST",
                                state.submissionPostDTO.apply {
                                    problemId = props.problemId
                                }
                            )
                            setState {
                                isResultGet = true
                            }
                        }
                    }

                    div {
                        attrs.classes = setOf("form-group")

                        label {
                            attrs.htmlFor = "languageInput"
                            +"使用的程式語言"
                        }

                        select {
                            attrs.classes = setOf("form-control")
                            attrs.id = "languageInput"
                            attrs.value = state.submissionPostDTO.language
                            attrs.onChangeFunction = {
                                val target = it.target as HTMLSelectElement
                                setState {
                                    submissionPostDTO.language = target.value
                                }
                            }

                            option {
                                attrs.value = "kotlin"
                                +"Kotlin"
                            }

                            option {
                                attrs.value = "c"
                                +"C"
                            }

                            option {
                                attrs.value = "java"
                                +"Java"
                            }

                            option {
                                attrs.value = "python"
                                +"Python"
                            }
                        }
                    }

                    formTextAreaComponent {
                        attrs.id = "codeInput"
                        attrs.name = "程式碼"
                        attrs.currentValue = state.submissionPostDTO.code
                        attrs.onChange = {
                            val target = it.target as HTMLTextAreaElement
                            setState {
                                submissionPostDTO.code = target.value
                            }
                        }
                    }

                    button {
                        attrs.type = ButtonType.submit
                        attrs.classes = setOf("btn", "btn-primary")

                        +"遞交程式碼"
                    }
                }
            }
        }
    }
}

fun RBuilder.submissionForm(handler: RElementBuilder<SubmissionFormProps>.() -> Unit): ReactElement =
    child(SubmissionForm::class, handler)