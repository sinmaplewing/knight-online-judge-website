import kotlinx.coroutines.*
import kotlinx.html.classes
import react.*
import react.dom.*
import react.redux.rConnect
import react.router.dom.redirect
import react.router.dom.routeLink
import redux.RAction
import redux.WrapperAction

external interface RestartSubmissionComponentProps: RProps {
    var submissionId: Int?
}

external interface RestartSubmissionComponentState: RState {
    var isRestart: Boolean
    var onRestart: (Int?) -> Unit
}

class RestartSubmissionComponent: RComponent<RestartSubmissionComponentProps, RestartSubmissionComponentState>() {
    override fun RestartSubmissionComponentState.init() {
        isRestart = false;

        onRestart = {
            val mainScope = MainScope()
            mainScope.launch {
                if (it == null) {
                    Fetcher.createSubmissionsRestartFetcher().fetch("POST")
                } else {
                    Fetcher.createSubmissionRestartFetcher(it).fetch("POST")
                }

                setState {
                    isRestart = true
                }
            }
        }
    }

    override fun RBuilder.render() {
        if (!state.isRestart) {
            state.onRestart(props.submissionId)
        } else {
            redirect(to = "/submissions")
        }
    }
}

fun RBuilder.restartSubmissionComponent(handler: RElementBuilder<RestartSubmissionComponentProps>.() -> Unit): ReactElement =
    child(RestartSubmissionComponent::class, handler)