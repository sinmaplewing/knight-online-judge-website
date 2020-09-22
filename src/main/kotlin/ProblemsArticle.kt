import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.*

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
    article {
        section {
            for (data in state.problemsData) {
                div {
                    +"${data.id} - ${data.title}"
                }
            }
/*
                tableList {
                    headers = listOf(
                        TableItemData("id"), TableItemData("title")
                    )
                    items = state.problemsData.map {
                        listOf(TableItemData(it.id), TableItemData(it.title, "/problems/${it.id}"))
                    }
                }
 */
            }
        }
    }
}

fun RBuilder.problemsArticle(handler: RProps.() -> Unit): ReactElement =
    child(ProblemsArticle::class) {
        attrs(handler)
    }