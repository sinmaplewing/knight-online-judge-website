import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.classes
import react.*
import react.dom.*
import react.router.dom.routeLink

external interface SubmissionsArticleState: RState {
    var submissionsData: List<SubmissionData>
}

class SubmissionsArticle: RComponent<RProps, SubmissionsArticleState>() {
    override fun SubmissionsArticleState.init() {
        submissionsData = listOf()

        val mainScope = MainScope()
        mainScope.launch {
            val remoteSubmissionsData = Fetcher.createSubmissionsFetcher().fetch()
            setState {
                submissionsData = remoteSubmissionsData.data.toList()
            }
        }
    }

    override fun RBuilder.render() {
        mainArticle {
            h1 {
                +"遞交程式碼列表"
            }

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
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.submissionsArticle(handler: RElementBuilder<RProps>.() -> Unit): ReactElement =
    child(SubmissionsArticle::class, handler)