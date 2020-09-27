import kotlinx.html.classes
import react.*
import react.dom.*

external interface TableListProps: RProps {
    var headers: List<TableItemData>
    var items: List<List<TableItemData>>
}

class TableList: RComponent<TableListProps, RState>() {
    override fun RBuilder.render() {
        table {
            attrs.classes = setOf("table", "table-bordered", "table-striped")

            thead {
                attrs.classes = setOf("thead-dark")

                tableRow {
                    attrs.isHeader = true
                    attrs.items = props.headers
                }
            }
            tbody {
                 for (item in props.items) {
                     tableRow {
                         attrs.isHeader = false
                         attrs.items = item
                     }
                 }
            }
        }
    }
}

fun RBuilder.tableList(handler: RElementBuilder<TableListProps>.() -> Unit): ReactElement =
    child(TableList::class, handler)