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
                            itemData = item
                        }
                    }
                }
            } else {
                for (item in props.items) {
                    td {
                        tableItem {
                            itemData = item
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.tableRow(handler: TableRowProps.() -> Unit): ReactElement =
    child(TableRow::class) {
        attrs(handler)
    }