import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.classes
import react.*
import react.dom.*
import react.router.dom.routeLink

external interface ProblemsArticleState: RState {
    var problemsData: List<ProblemData>
}

class ProblemsArticle: RComponent<RProps, ProblemsArticleState>() {
    override fun ProblemsArticleState.init() {
        problemsData = listOf()

        val mainScope = MainScope()
        mainScope.launch {
            val remoteProblemData = Fetcher.createProblemsFetcher().fetch()
            setState {
                problemsData = remoteProblemData.data.toList()
            }
        }
    }

    override fun RBuilder.render() {
        mainArticle {
            h1 {
                +"題目列表"
            }

            table {
                attrs.classes = setOf("table", "table-bordered", "table-striped")

                thead {
                    attrs.classes = setOf("thead-dark")

                    tr {
                        th { +"編號" }
                        th { +"標題" }
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
                        }
                    }
                }
            }

            /*
            tableList {
                attrs.headers = listOf(
                    TableItemData("編號"), TableItemData("標題")
                )
                attrs.items = state.problemsData.map {
                    listOf(TableItemData(it.id), TableItemData(it.title, "/problems/${it.id}"))
                }
            }
             */
        }
    }
}

fun RBuilder.problemsArticle(handler: RElementBuilder<RProps>.() -> Unit): ReactElement =
    child(ProblemsArticle::class, handler)