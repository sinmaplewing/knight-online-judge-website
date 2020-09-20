import react.dom.*
import kotlinx.browser.document
import kotlinx.html.id

fun main() {
    render(document.getElementById("root")) {
        div {
            attrs.id = "container"

            header {
                +"這是標頭部分"
                nav {
                    +"這是選項"
                }
            }

            article {
                section {
                    +"一個區域"
                }
            }

            footer {
                +"這是尾巴的部分"
            }
        }
    }
}