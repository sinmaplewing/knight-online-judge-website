import kotlinx.coroutines.*
import kotlinx.html.classes
import react.*
import react.dom.*
import react.redux.rConnect
import react.router.dom.redirect
import react.router.dom.routeLink
import redux.RAction
import redux.WrapperAction

external interface ProblemDeleteComponentProps: RProps {
    var problemId: Int
}

external interface ProblemDeleteComponentState: RState {
    var isDelete: Boolean
    var onDelete: (Int) -> Unit
}

class ProblemDeleteComponent: RComponent<ProblemDeleteComponentProps, ProblemDeleteComponentState>() {
    override fun ProblemDeleteComponentState.init() {
        isDelete = false;

        onDelete = {
            val mainScope = MainScope()
            mainScope.launch {
                Fetcher.createProblemDeleteFetcher(it).fetch("DELETE")
                setState {
                    isDelete = true
                }
            }
        }
    }

    override fun RBuilder.render() {
        if (!state.isDelete) {
            state.onDelete(props.problemId)
        } else {
            redirect(to = "/problems")
        }
    }
}

fun RBuilder.problemDeleteComponent(handler: RElementBuilder<ProblemDeleteComponentProps>.() -> Unit): ReactElement =
    child(ProblemDeleteComponent::class, handler)