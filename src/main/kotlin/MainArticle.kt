import kotlinx.css.LinearDimension
import kotlinx.css.margin
import kotlinx.css.width
import kotlinx.html.classes
import react.*
import react.dom.*
import styled.css
import styled.styledArticle

class MainArticle: RComponent<RProps, RState>() {
    override fun RBuilder.render() {

        styledArticle {
            css {
                width = LinearDimension("80%")
                margin = "30px auto"
            }

            attrs.classes = setOf("row")

            section {
                attrs.classes = setOf("col")
                children()
            }
        }
    }
}

fun RBuilder.mainArticle(handler: RElementBuilder<RProps>.() -> Unit): ReactElement =
    child(MainArticle::class, handler)