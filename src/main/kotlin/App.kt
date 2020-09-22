import kotlinx.css.LinearDimension
import kotlinx.css.width
import kotlinx.html.id
import react.*
import react.dom.*
import react.router.dom.*
import styled.css
import styled.styledDiv

external interface IdProps : RProps {
    var id: Int
}

class App: RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        hashRouter {
            styledDiv {
                css {
                    width = LinearDimension("100%")
                }

                attrs.id = "container"

                websiteHeader { }
                switch {
                    route("/", exact = true) { mainArticle { content = "這裡是首頁" } }
                    route("/problems", exact = true) { problemsArticle {  } }
                    route<IdProps>("/problems/:id") {
                        val id = it.match.params.id
                        mainArticle {
                            content = "這裡是第 $id 題題目詳細資料"
                        }
                    }

                    route("/submissions", exact = true) { mainArticle { content = "這裡是總遞交程式碼列表" } }
                    route<IdProps>("/submissions/:id") {
                        val id = it.match.params.id
                        mainArticle {
                            content = "這裡是第 $id 個程式碼詳細資料"
                        }
                    }

                    route("/users", exact = true) { mainArticle { content = "這裡是總使用者列表" } }
                    route<IdProps>("/users/:id") {
                        val id = it.match.params.id
                        mainArticle {
                            content = "這裡是第 $id 編號使用者詳細資料"
                        }
                    }
                }
                mainArticle { }
                websiteFooter { }
            }
        }
    }
}

fun RBuilder.app(handler: RProps.() -> Unit): ReactElement =
    child(App::class) {
        attrs(handler)
    }