import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.events.Event
import react.*
import react.dom.*

external interface FormTextAreaComponentProps: RProps {
    var id: String
    var name: String
    var currentValue: String
    var onChange: (Event) -> Unit
}

class FormTextAreaComponent: RComponent<FormTextAreaComponentProps, RState>() {
    override fun RBuilder.render() {
        div {
            attrs.classes = setOf("form-group")

            label {
                attrs.htmlFor = props.id
                +props.name
            }

            textArea {
                attrs.id = props.id
                attrs.classes = setOf("form-control")
                attrs.value = props.currentValue
                attrs.onChangeFunction = props.onChange
            }
        }
    }
}

fun RBuilder.formTextAreaComponent(handler: RElementBuilder<FormTextAreaComponentProps>.() -> Unit): ReactElement =
    child(FormTextAreaComponent::class, handler)