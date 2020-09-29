import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.classes
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.routeLink

external interface ProblemsArticleState: RState {
    var problemsData: List<ProblemData>
    var isEditable: Boolean
    var redirectToProblemId: Int?
}

class ProblemsArticle: RComponent<RProps, ProblemsArticleState>() {
    override fun ProblemsArticleState.init() {
        problemsData = listOf()
        isEditable = false
        redirectToProblemId = null

        val mainScope = MainScope()
        mainScope.launch {
            val remoteProblemData = Fetcher.createProblemsFetcher().fetch()
            setState {
                problemsData = remoteProblemData.data.toList()
                isEditable = remoteProblemData.isEditable
            }
        }
    }

    override fun RBuilder.render() {
        mainArticle {
            if (state.redirectToProblemId != null) {
                redirect(to = "problems/${state.redirectToProblemId}/delete")
            } else {
                div {
                    attrs.classes = setOf("row")
                    h1 {
                        attrs.classes = setOf("col")
                        +"題目列表"
                    }

                    if (state.isEditable) {
                        div {
                            attrs.classes = setOf("col-md-2")

                            routeLink("/problems/new", className = "btn btn-primary") {
                                +"新增題目"
                            }
                        }
                    }
                }

                table {
                    attrs.classes = setOf("table", "table-bordered", "table-striped")

                    thead {
                        attrs.classes = setOf("thead-dark")

                        tr {
                            th { +"編號" }
                            th { +"標題" }

                            if (state.isEditable) {
                                th { +"操作" }
                            }
                        }
                    }
                    tbody {
                        for (item in state.problemsData) {
                            tr {
                                if (item.isAccepted == "true") {
                                    attrs.classes = setOf("table-success")
                                } else if (item.isSubmitted == "true") {
                                    attrs.classes = setOf("table-danger")
                                }

                                td { +item.id }
                                td {
                                    routeLink("/problems/${item.id}") {
                                        +item.title
                                    }
                                }

                                if (state.isEditable) {
                                    td {
                                        routeLink("/problems/${item.id}/edit", className = "btn btn-primary") {
                                            +"編輯"
                                        }

                                        button {
                                            attrs.type = ButtonType.button
                                            attrs.classes = setOf("btn", "btn-danger")
                                            attrs.onClickFunction = {
                                                if (window.confirm("確定要刪除這筆題目嗎？")) {
                                                    setState {
                                                        redirectToProblemId = item.id.toInt()
                                                    }
                                                }
                                            }

                                            +"刪除"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.problemsArticle(handler: RElementBuilder<RProps>.() -> Unit): ReactElement =
    child(ProblemsArticle::class, handler)