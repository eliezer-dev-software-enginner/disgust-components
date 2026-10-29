package disgust.web.inputs;

import disgust.icons.Icon;
import disgust.io.ButtonsPack;
import disgust.io.Material;
import java.util.List;
import java.util.Objects;
import megalodonte.base.async.RunnableThrowing;
import megalodonte.base.state.State;
import megalodonte.components.Button;
import megalodonte.components.Checkbox;
import megalodonte.components.RadioGroup;
import megalodonte.components.Select;
import megalodonte.components.Slider;
import megalodonte.components.Switch;
import megalodonte.components.inputs.TextAreaInput;

/** Material-inspired input entry point; names stay distinct from Megalodonte controls. */
public final class Inputs {
    private Inputs() { }

    public static Button button(String label, RunnableThrowing action) {
        return ButtonsPack.ContainedButton(label, new ButtonsPack.Props().onClick(action));
    }

    public static Button floatingActionButton(Icon icon, String accessibleLabel, RunnableThrowing action) {
        return Material.floatingActionButton(icon, accessibleLabel, action);
    }

    public static megalodonte.components.layout_components.Row buttonGroup(List<Button> buttons) {
        return Material.buttonGroup(buttons);
    }

    public static Checkbox checkbox(String label, State<Boolean> checked) {
        return new Checkbox(label, Objects.requireNonNull(checked, "checked"));
    }

    public static Switch switchControl(String label, State<Boolean> selected) {
        return Material.switchControl(label, selected);
    }

    public static Slider slider(double min, double max, State<Double> value) {
        return Material.slider(min, max, value);
    }

    public static RadioGroup.Option option(String value, String label) {
        return new RadioGroup.Option(value, label);
    }

    public static RadioGroup radioGroup(String label, List<RadioGroup.Option> options, State<String> selected) {
        return Material.radioGroup(label, options, selected);
    }

    public static <T> Select<T> select(Iterable<T> options, State<T> selected) {
        return new Select<T>().items(Objects.requireNonNull(options, "options"))
                .value(Objects.requireNonNull(selected, "selected"));
    }

    public static megalodonte.components.inputs.Input textField(State<String> value) {
        return new megalodonte.components.inputs.Input(Objects.requireNonNull(value, "value"));
    }

    public static TextAreaInput textArea(State<String> value) {
        return new TextAreaInput(Objects.requireNonNull(value, "value"), new megalodonte.props.InputProps());
    }
}
