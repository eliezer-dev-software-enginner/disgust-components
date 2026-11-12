package disgust.io;

import java.util.List;
import megalodonte.base.state.State;
import megalodonte.components.Checkbox;
import megalodonte.components.DatePicker;
import megalodonte.components.RadioGroup;
import megalodonte.components.Select;
import megalodonte.components.Slider;
import megalodonte.components.Switch;
import megalodonte.components.inputs.Input;
import megalodonte.components.inputs.PasswordInput;
import megalodonte.components.inputs.TextAreaInput;
import megalodonte.props.InputProps;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputsPackApiTest {

    @Test
    void exposesEveryInputDocumentedByTheCatalog() throws Exception {
        assertEquals(Input.class, InputsPack.class
                .getMethod("text", State.class, InputProps.class).getReturnType());
        assertEquals(PasswordInput.class, InputsPack.class
                .getMethod("password", State.class, InputProps.class).getReturnType());
        assertEquals(TextAreaInput.class, InputsPack.class
                .getMethod("textArea", State.class, InputProps.class).getReturnType());
        assertEquals(Select.class, InputsPack.class
                .getMethod("select", Iterable.class, State.class).getReturnType());
        assertEquals(DatePicker.class, InputsPack.class
                .getMethod("date", State.class).getReturnType());
        assertEquals(Checkbox.class, InputsPack.class
                .getMethod("checkbox", String.class, State.class).getReturnType());
        assertEquals(Switch.class, InputsPack.class
                .getMethod("switchControl", String.class, State.class).getReturnType());
        assertEquals(RadioGroup.Option.class, InputsPack.class
                .getMethod("option", String.class, String.class).getReturnType());
        assertEquals(RadioGroup.class, InputsPack.class
                .getMethod("radioGroup", String.class, List.class, State.class).getReturnType());
        assertEquals(Slider.class, InputsPack.class
                .getMethod("slider", double.class, double.class, State.class).getReturnType());
    }
}
