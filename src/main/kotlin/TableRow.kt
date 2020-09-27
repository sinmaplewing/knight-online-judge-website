import react.*
import react.dom.*
import react.router.dom.routeLink

external interface TableRowProps: RProps {
    var isHeader: Boolean
    var items: List<TableItemData>
}

class TableRow: RComponent<TableRowProps, RState>() {
    override fun RBuilder.render() {
        tr {
            if (props.isHeader) {
                for (item in props.items) {
                    th {
                        tableItem {
                            attrs.itemData = item
                        }
                    }
                }
            } else {
                for (item in props.items) {
                    td {
                        tableItem {
                            attrs.itemData = item
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.tableRow(handler: RElementBuilder<TableRowProps>.() -> Unit): ReactElement =
    child(TableRow::class, handler)