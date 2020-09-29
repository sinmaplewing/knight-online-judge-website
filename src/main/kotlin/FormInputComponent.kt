import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.events.Event
import react.*
import react.dom.*

external interface FormInputComponentProps: RProps {
    var id: String
    var name: String
    var currentValue: String
    var inputType: InputType
    var onChange: (Event) -> Unit
}

class FormInputComponent: RComponent<FormInputComponentProps, RState>() {
    override fun RBuilder.render() {
        div {
            attrs.classes = setOf("form-group")

            label {
                attrs.htmlFor = props.id
                +props.name
            }

            input {
                attrs.type = props.inputType
                attrs.id = props.id
                attrs.classes = setOf("form-control")
                attrs.value = props.currentValue
                attrs.onChangeFunction = props.onChange
            }
        }
    }
}

fun RBuilder.formInputComponent(handler: RElementBuilder<FormInputComponentProps>.() -> Unit): ReactElement =
    child(FormInputComponent::class, handler)