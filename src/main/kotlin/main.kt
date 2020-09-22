import react.dom.*
import kotlinx.browser.document
import kotlinx.html.id

fun main() {
    render(document.getElementById("root")) {
        app { }
    }
}