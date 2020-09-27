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
                    route("/", exact = true) { mainArticle { +"這裡是首頁" } }
                    route("/problems", exact = true) { problemsArticle {  } }
                    route<IdProps>("/problems/:id") { problemDetailArticle {
                        attrs.problemId = it.match.params.id
                    }}

                    route("/submissions", exact = true) { submissionsArticle { } }

                    route("/users", exact = true) { usersArticle {  } }

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