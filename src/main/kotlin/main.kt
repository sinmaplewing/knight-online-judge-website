import react.dom.*
import kotlinx.browser.document
import kotlinx.html.id
import react.redux.*
import redux.*

fun main() {
    val store = createStore(::reducer, createAppState(), rEnhancer())
    render(document.getElementById("root")) {
        provider(store) {
            app { }
        }
    }
}