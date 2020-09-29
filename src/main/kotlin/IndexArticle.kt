import kotlinx.css.LinearDimension
import kotlinx.css.margin
import kotlinx.css.width
import kotlinx.html.classes
import react.*
import react.dom.*
import styled.css
import styled.styledArticle

class IndexArticle: RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        mainArticle {
            div {
                attrs.classes = setOf("jumbotron")
                h1 {
                    attrs.classes = setOf("display-4")
                    +"歡迎光臨 Knight Online Judge"
                }
                p {
                    attrs.classes = setOf("lead")
                    +"快點來解些題目吧！"
                }
            }
        }
    }
}

fun RBuilder.indexArticle(handler: RElementBuilder<RProps>.() -> Unit): ReactElement =
    child(IndexArticle::class, handler)