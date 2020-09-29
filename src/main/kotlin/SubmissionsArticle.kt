import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.classes
import react.*
import react.dom.*
import react.router.dom.routeLink

external interface SubmissionsArticleState: RState {
    var submissionsData: List<SubmissionData>
    var isRefreshable: Boolean
}

class SubmissionsArticle: RComponent<RProps, SubmissionsArticleState>() {
    override fun SubmissionsArticleState.init() {
        submissionsData = listOf()
        isRefreshable = false

        val mainScope = MainScope()
        mainScope.launch {
            val remoteSubmissionsData = Fetcher.createSubmissionsFetcher().fetch()
            setState {
                submissionsData = remoteSubmissionsData.data.toList()
                isRefreshable = remoteSubmissionsData.isRefreshable
            }
        }
    }

    override fun RBuilder.render() {
        mainArticle {
            div {
                attrs.classes = setOf("row")
                h1 {
                    attrs.classes = setOf("col")
                    +"遞交程式碼列表"
                }

                if (state.isRefreshable) {
                    div {
                        attrs.classes = setOf("col-md-2")

                        routeLink("/submissions/restart", className = "btn btn-primary") {
                            +"重新審核未審核的程式碼"
                        }
                    }
                }
            }

            val isDisplayRefreshableColumn = state.submissionsData.any { it.isRefreshable }
            table {
                attrs.classes = setOf("table", "table-bordered", "table-striped")

                thead {
                    attrs.classes = setOf("thead-dark")

                    tr {
                        th { +"編號" }
                        th { +"使用者名稱" }
                        th { +"題目名稱" }
                        th { +"使用程式語言" }
                        th { +"審核結果" }
                        th { +"執行時間（秒）" }

                        if (isDisplayRefreshableColumn) {
                            th { +"操作" }
                        }
                    }
                }
                tbody {
                    for (item in state.submissionsData) {
                        tr {
                            td { +item.id }
                            td { +item.name }
                            td { routeLink("/problems/${item.problemId}") { +item.title } }
                            td { +item.language }
                            td { +item.result }
                            td { +item.executedTime }

                            if (isDisplayRefreshableColumn) {
                                td {
                                    if (item.isRefreshable) {
                                        routeLink("/submissions/${item.id}/restart", className = "btn btn-primary") {
                                            +"重新審核"
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

fun RBuilder.submissionsArticle(handler: RElementBuilder<RProps>.() -> Unit): ReactElement =
    child(SubmissionsArticle::class, handler)