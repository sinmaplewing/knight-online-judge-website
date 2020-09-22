import react.*
import react.dom.*
import react.router.dom.routeLink

data class TableItemData(
    val content: String,
    val hyperLink: String? = null
)

external interface TableItemProps: RProps {
    var itemData: TableItemData
}

class TableItem: RComponent<TableItemProps, RState>() {
    override fun RBuilder.render() {
        val hyperlinkData = props.itemData.hyperLink
        if (hyperlinkData != null) {
            routeLink(hyperlinkData) {
                +props.itemData.content
            }
        }
        else {
            +props.itemData.content
        }
    }
}

fun RBuilder.tableItem(handler: TableItemProps.() -> Unit): ReactElement =
    child(TableItem::class) {
        attrs(handler)
    }