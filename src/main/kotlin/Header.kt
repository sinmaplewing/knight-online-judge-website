import kotlinx.html.classes
import react.*
import react.dom.*
import react.router.dom.routeLink

class Header: RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        header {
            nav {
                attrs.classes = setOf("navbar", "navbar-expand-xl", "navbar-dark", "bg-dark")

                routeLink("/", className = "navbar-brand") {
                    +"Knight Online Judge"
                }

                ul {
                    attrs.classes = setOf("navbar-nav")

                    li {
                        attrs.classes = setOf("nav-item")
                        routeLink("/", className = "nav-link") { +"首頁" }
                    }
                    li {
                        attrs.classes = setOf("nav-item")
                        routeLink("/problems", className = "nav-link") { +"問題列表" }
                    }
                    li {
                        attrs.classes = setOf("nav-item")
                        routeLink("/submissions", className = "nav-link") { +"遞交程式碼列表" }
                    }
                    li {
                        attrs.classes = setOf("nav-item")
                        routeLink("/users", className = "nav-link") { +"使用者列表" }
                    }
                }

                connectedLoginStatus { }
            }
        }
    }
}

fun RBuilder.websiteHeader(handler: RElementBuilder<RProps>.() -> Unit): ReactElement =
    child(Header::class, handler)