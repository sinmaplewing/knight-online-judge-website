import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.classes
import react.*
import react.dom.*
import react.router.dom.routeLink

external interface UsersArticleState: RState {
    var usersData: List<UserData>
}

class UsersArticle: RComponent<RProps, UsersArticleState>() {
    override fun UsersArticleState.init() {
        usersData = listOf()

        val mainScope = MainScope()
        mainScope.launch {
            val remoteUsersData = Fetcher.createUsersFetcher().fetch()
            setState {
                usersData = remoteUsersData.data.toList()
            }
        }
    }

    override fun RBuilder.render() {
        mainArticle {
            h1 {
                +"使用者列表"
            }

            table {
                attrs.classes = setOf("table", "table-bordered", "table-striped")

                thead {
                    attrs.classes = setOf("thead-dark")

                    tr {
                        th { +"編號" }
                        th { +"名稱" }
                        th { +"解題數" }
                    }
                }
                tbody {
                    for (item in state.usersData) {
                        tr {
                            td { +item.id }
                            td { +item.name }
                            td { +item.solvedProblemCount }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.usersArticle(handler: RElementBuilder<RProps>.() -> Unit): ReactElement =
    child(UsersArticle::class, handler)