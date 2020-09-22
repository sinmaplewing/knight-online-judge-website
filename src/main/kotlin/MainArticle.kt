import react.*
import react.dom.*

external interface MainArticleProps: RProps {
    var content: String
}

class MainArticle: RComponent<MainArticleProps, RState>() {
    override fun RBuilder.render() {
        article {
            section {
                +props.content
            }
        }
    }
}

fun RBuilder.mainArticle(handler: MainArticleProps.() -> Unit): ReactElement =
    child(MainArticle::class) {
        attrs(handler)
    }