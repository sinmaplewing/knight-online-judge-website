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
    var isError: Boolean
    var onLoad: (Int) -> Unit
}

class ProblemDetailArticle: RComponent<ProblemDetailArticleProps, ProblemDetailArticleState>() {
    override fun ProblemDetailArticleState.init() {
        problemDetailData = null
        isError = false

        onLoad = {
            val mainScope = MainScope()
            mainScope.launch {
                try {
                    val remoteProblemDetailData = Fetcher.createProblemDetailFetcher(it).fetch()
                    setState {
                        problemDetailData = remoteProblemDetailData.data
                    }
                } catch(e: Throwable) {
                    setState {
                        isError = true
                    }
                }
            }
        }
    }

    override fun RBuilder.render() {
        mainArticle {
            if (state.isError) {
                div {
                    attrs.classes = setOf("alert", "alert-danger")
                    +"找不到題目資訊。"
                }

                return@mainArticle
            }

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

                submissionForm { attrs.problemId = props.problemId }
            }
        }
    }
}

fun RBuilder.problemDetailArticle(handler: RElementBuilder<ProblemDetailArticleProps>.() -> Unit): ReactElement =
    child(ProblemDetailArticle::class, handler)