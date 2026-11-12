package disgust.io;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

/**
 * Native input entry points for Disgust applications.
 *
 * <p>The returned objects are Megalodonte components backed by JavaFX controls;
 * this pack only gives the native input family one cohesive Disgust API.</p>
 */
public final class InputsPack {

    private InputsPack() {}

    public static Input text(State<String> state) {
        return text(state, new InputProps());
    }

    public static Input text(State<String> state, InputProps props) {
        return new Input(Objects.requireNonNull(state, "state"),
                Objects.requireNonNull(props, "props"));
    }

    public static PasswordInput password(State<String> state) {
        return password(state, new InputProps());
    }

    public static PasswordInput password(State<String> state, InputProps props) {
        return new PasswordInput(Objects.requireNonNull(state, "state"),
                Objects.requireNonNull(props, "props"));
    }

    public static TextAreaInput textArea(State<String> state) {
        return textArea(state, new InputProps());
    }

    public static TextAreaInput textArea(State<String> state, InputProps props) {
        return new TextAreaInput(Objects.requireNonNull(state, "state"),
                Objects.requireNonNull(props, "props"));
    }

    public static <T> Select<T> select(Iterable<T> items, State<T> selected) {
        return new Select<T>()
                .items(Objects.requireNonNull(items, "items"))
                .value(Objects.requireNonNull(selected, "selected"));
    }

    public static DatePicker date(State<LocalDate> state) {
        return new DatePicker(Objects.requireNonNull(state, "state"));
    }

    public static Checkbox checkbox(String label, State<Boolean> checked) {
        return new Checkbox(label, Objects.requireNonNull(checked, "checked"));
    }

    public static Switch switchControl(String accessibleLabel, State<Boolean> selected) {
        return new Switch(accessibleLabel, Objects.requireNonNull(selected, "selected"));
    }

    public static RadioGroup.Option option(String value, String label) {
        return new RadioGroup.Option(value, label);
    }

    public static RadioGroup radioGroup(String accessibleLabel,
                                        List<RadioGroup.Option> options,
                                        State<String> selected) {
        return new RadioGroup(accessibleLabel, Objects.requireNonNull(options, "options"),
                Objects.requireNonNull(selected, "selected"));
    }

    public static Slider slider(double min, double max, State<Double> value) {
        return new Slider(min, max, Objects.requireNonNull(value, "value"));
    }
}
