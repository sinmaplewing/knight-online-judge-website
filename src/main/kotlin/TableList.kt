import react.*
import react.dom.*

external interface TableListProps: RProps {
    var headers: List<TableItemData>
    var items: List<List<TableItemData>>
}

class TableList: RComponent<TableListProps, RState>() {
    override fun RBuilder.render() {
        table {
            thead {
                tableRow {
                    isHeader = true
                    items = props.headers
                }
            }
            tbody {
                 for (item in props.items) {
                     tableRow {
                         isHeader = false
                         items = item
                     }
                 }
            }
        }
    }
}

fun RBuilder.tableList(handler: TableListProps.() -> Unit): ReactElement =
    child(TableList::class) {
        attrs(handler)
    }