import com.ccfraser.muirwik.components.mAppBar
import com.ccfraser.muirwik.components.mTypography
import react.*
import react.dom.*
import react.router.dom.routeLink

class Header: RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        header {
            +"Knight Online Judge"
            nav {
                ul {
                    li { routeLink("/") { +"首頁" } }
                    li { routeLink("/problems") { +"問題列表" } }
                    li { routeLink("/submissions") { +"遞交程式碼列表" } }
                    li { routeLink("/users") { +"使用者列表" } }
                }
            }
        }
    }
}

fun RBuilder.websiteHeader(handler: RProps.() -> Unit): ReactElement =
    child(Header::class) {
        attrs(handler)
    }