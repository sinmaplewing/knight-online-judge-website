import kotlinx.css.LinearDimension
import kotlinx.css.fontSize
import kotlinx.html.classes
import react.*
import react.dom.*
import styled.css
import styled.styledFooter

class Footer: RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        styledFooter {
            css {
                fontSize = LinearDimension("small")
            }

            hr { }
            div {
                attrs.classes = setOf("text-center")
                +"© 2020 Copyright: Maplewing"
            }
        }
    }
}

fun RBuilder.websiteFooter(handler: RElementBuilder<RProps>.() -> Unit): ReactElement =
    child(Footer::class, handler)