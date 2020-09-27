import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.classes
import react.*
import react.dom.*
import react.router.dom.routeLink

external interface ProblemDetailArticleProps: RProps {
    var problemId: Int
}

external interface ProblemDetailArticleState: RState {
    var problemDetailData: ProblemDetailData?
    var onLoad: (Int) -> Unit
}

class ProblemDetailArticle: RComponent<ProblemDetailArticleProps, ProblemDetailArticleState>() {
    override fun ProblemDetailArticleState.init() {
        problemDetailData = null

        onLoad = {
            val mainScope = MainScope()
            mainScope.launch {
                val remoteProblemDetailData = Fetcher.createProblemDetailFetcher(it).fetch()
                setState {
                    problemDetailData = remoteProblemDetailData.data
                }
            }
        }
    }

    override fun RBuilder.render() {
        mainArticle {
            val problemDetailData = state.problemDetailData
            if (problemDetailData == null || problemDetailData.id != props.problemId.toString()) {
                state.onLoad(props.problemId)
            } else {
                h1 {
                    +"${problemDetailData.id}. ${problemDetailData.title}"
                }

                pre {
                    +problemDetailData.description
                }
            }
        }
    }
}

fun RBuilder.problemDetailArticle(handler: RElementBuilder<ProblemDetailArticleProps>.() -> Unit): ReactElement =
    child(ProblemDetailArticle::class, handler)