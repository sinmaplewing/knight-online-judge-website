import kotlinx.browser.window
import kotlinx.html.classes
import kotlinx.html.id
import react.*
import react.dom.*
import react.router.dom.*

external interface IdProps : RProps {
    var id: Int
}

class App: RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        hashRouter {
            div {
                attrs.id = "container"

                websiteHeader { }

                switch {
                    route("/", exact = true) { indexArticle {  } }
                    route("/problems", exact = true) { problemsArticle {  } }
                    route("/problems/new", exact = true) { problemForm { } }
                    route<IdProps>("/problems/:id", exact = true) { problemDetailArticle {
                        attrs.problemId = it.match.params.id
                    }}
                    route<IdProps>("/problems/:id/edit") {
                        problemForm {
                            attrs.problemId = it.match.params.id
                        }
                    }
                    route<IdProps>("/problems/:id/delete") {
                        problemDeleteComponent {
                            attrs.problemId = it.match.params.id
                        }
                    }

                    route("/submissions", exact = true) { submissionsArticle { } }
                    route("/submissions/restart", exact = true) {
                        restartSubmissionComponent {  }
                    }
                    route<IdProps>("/submissions/:id/restart") {
                        restartSubmissionComponent {
                            attrs.submissionId = it.match.params.id
                        }
                    }

                    route("/users", exact = true) { usersArticle {  } }

                    route("/register") { userRegisterForm { } }
                    route("/login") { mainArticle { connectedLoginForm { } } }
                    route("/logout") { mainArticle { connectedLogoutComponent { } }}
                }
                websiteFooter { }
            }
        }
    }
}

fun RBuilder.app(handler: RElementBuilder<RProps>.() -> Unit): ReactElement =
    child(App::class, handler)