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

external interface ProblemFormProps: RProps {
    var problemId: Int?
}

external interface ProblemFormState: RState {
    var problemFormData: ProblemFormData
    var isSubmitted: Boolean
    var isResultGet: Boolean
    var onLoad: (Int) -> Unit
}

class ProblemForm: RComponent<ProblemFormProps, ProblemFormState>() {
    override fun ProblemFormState.init() {
        isSubmitted = false
        isResultGet = false
        problemFormData = ProblemFormData(
            null,
            "",
            "",
            mutableListOf()
        )

        onLoad = {
            val mainScope = MainScope()
            mainScope.launch {
                val remoteProblemFormData = Fetcher.createProblemAllFetcher(it).fetch()
                setState {
                    problemFormData = remoteProblemFormData.data.let {
                        ProblemFormData(
                            it.id,
                            it.title,
                            it.description,
                            it.testCases.toMutableList()
                        )
                    }
                }
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                width = LinearDimension("80%")
                margin = "30px auto"
            }

            val problemId = props.problemId
            if (state.isResultGet) {
                redirect(to = if (problemId != null) "/problems/$problemId" else "/problems")
            } else {
                if (problemId != null && state.problemFormData.id == null) {
                    state.onLoad(problemId)
                } else {
                    h1 { +"題目表單" }
                    form {
                        attrs.onSubmitFunction = onSubmitFunction@{
                            it.preventDefault()
                            if (state.isSubmitted) return@onSubmitFunction

                            if (problemId != null) {
                                val mainScope = MainScope()
                                mainScope.launch {
                                    Fetcher.createProblemEditFetcher(problemId).fetch(
                                        "PUT",
                                        ProblemPutDTO(
                                            state.problemFormData.id.toString(),
                                            state.problemFormData.title,
                                            state.problemFormData.description,
                                            state.problemFormData.testCases.map {
                                                TestCasePutDTO(
                                                    it.id,
                                                    it.input,
                                                    it.expectedOutput,
                                                    it.comment,
                                                    it.score,
                                                    it.timeOutSeconds
                                                )
                                            }
                                        )
                                    )
                                    setState {
                                        isResultGet = true
                                    }
                                }
                            } else {
                                val mainScope = MainScope()
                                mainScope.launch {
                                    Fetcher.createProblemCreateFetcher().fetch(
                                        "POST",
                                        ProblemPostDTO(
                                            state.problemFormData.title,
                                            state.problemFormData.description,
                                            state.problemFormData.testCases.map {
                                                TestCasePostDTO(
                                                    it.input,
                                                    it.expectedOutput,
                                                    it.comment,
                                                    it.score,
                                                    it.timeOutSeconds
                                                )
                                            }
                                        )
                                    )
                                    setState {
                                        isResultGet = true
                                    }
                                }
                            }
                        }

                        formInputComponent {
                            attrs.inputType = InputType.text
                            attrs.id = "titleInput"
                            attrs.name = "題目標題"
                            attrs.currentValue = state.problemFormData.title
                            attrs.onChange = {
                                val target = it.target as HTMLInputElement
                                setState {
                                    problemFormData.title = target.value
                                }
                            }
                        }

                        formTextAreaComponent {
                            attrs.id = "descriptionInput"
                            attrs.name = "題目描述"
                            attrs.currentValue = state.problemFormData.description
                            attrs.onChange = {
                                val target = it.target as HTMLTextAreaElement
                                setState {
                                    problemFormData.description = target.value
                                }
                            }
                        }

                        div {
                            attrs.classes = setOf("form-group")

                            div {
                                attrs.classes = setOf("row")
                                h2 {
                                    attrs.classes = setOf("col")
                                    +"測試資料編輯"
                                }
                                div {
                                    attrs.classes = setOf("col-md-2")
                                    button {
                                        attrs.type = ButtonType.button
                                        attrs.classes = setOf("btn", "btn-primary")
                                        attrs.onClickFunction = {
                                            setState {
                                                state.problemFormData.testCases.add(
                                                    TestCaseData(
                                                        null,
                                                        "",
                                                        "",
                                                        "",
                                                        0,
                                                        10.0
                                                    )
                                                )
                                            }
                                        }

                                        +"新增一筆測資"
                                    }
                                }
                            }

                            for ((index, testCase) in state.problemFormData.testCases.withIndex()) {
                                styledDiv {
                                    css {
                                        padding = "20px"
                                        margin = "10px auto"
                                        borderWidth = LinearDimension("1px")
                                        borderStyle = BorderStyle.solid
                                        borderColor = Color.aliceBlue
                                        backgroundColor = Color.antiqueWhite
                                    }

                                    attrs.classes = setOf("container", "form-group")

                                    div {
                                        attrs.classes = setOf("row")
                                        h3 {
                                            attrs.classes = setOf("col")
                                            +"測試資料 ${index + 1}"
                                        }

                                        div {
                                            attrs.classes = setOf("col-md-2")
                                            button {
                                                attrs.type = ButtonType.button
                                                attrs.classes = setOf("btn", "btn-danger")
                                                attrs.onClickFunction = {
                                                    if (window.confirm("確定要刪除這筆測試資料嗎？")) {
                                                        setState {
                                                            state.problemFormData.testCases.removeAt(index)
                                                        }
                                                    }
                                                }

                                                +"刪除這筆測資"
                                            }
                                        }
                                    }

                                    formTextAreaComponent {
                                        attrs.id = "inputInput"
                                        attrs.name = "測資輸入"
                                        attrs.currentValue = testCase.input
                                        attrs.onChange = {
                                            val target = it.target as HTMLTextAreaElement
                                            setState {
                                                testCase.input = target.value
                                            }
                                        }
                                    }

                                    formTextAreaComponent {
                                        attrs.id = "expectedOutputInput"
                                        attrs.name = "預期測資輸出"
                                        attrs.currentValue = testCase.expectedOutput
                                        attrs.onChange = {
                                            val target = it.target as HTMLTextAreaElement
                                            setState {
                                                testCase.expectedOutput = target.value
                                            }
                                        }
                                    }

                                    formTextAreaComponent {
                                        attrs.id = "commentInput"
                                        attrs.name = "註解"
                                        attrs.currentValue = testCase.comment
                                        attrs.onChange = {
                                            val target = it.target as HTMLTextAreaElement
                                            setState {
                                                testCase.comment = target.value
                                            }
                                        }
                                    }

                                    formInputComponent {
                                        attrs.inputType = InputType.number
                                        attrs.id = "scoreInput"
                                        attrs.name = "分數"
                                        attrs.currentValue = testCase.score.toString()
                                        attrs.onChange = {
                                            val target = it.target as HTMLInputElement
                                            setState {
                                                testCase.score = target.value.toIntOrNull() ?: 0
                                            }
                                        }
                                    }

                                    formInputComponent {
                                        attrs.inputType = InputType.number
                                        attrs.id = "timeOutSecondsInput"
                                        attrs.name = "最高執行時間限制（秒）"
                                        attrs.currentValue = testCase.timeOutSeconds.toString()
                                        attrs.onChange = {
                                            val target = it.target as HTMLInputElement
                                            setState {
                                                testCase.timeOutSeconds = target.value.toDoubleOrNull() ?: 10.0
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        button {
                            attrs.type = ButtonType.submit
                            attrs.classes = setOf("btn", "btn-primary")

                            +"完成"
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.problemForm(handler: RElementBuilder<ProblemFormProps>.() -> Unit): ReactElement =
    child(ProblemForm::class, handler)