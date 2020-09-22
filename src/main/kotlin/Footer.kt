import react.*
import react.dom.*

class Footer: RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        footer {
            +"這是尾巴的部分"
        }
    }
}

fun RBuilder.websiteFooter(handler: RProps.() -> Unit): ReactElement =
    child(Footer::class) {
        attrs(handler)
    }