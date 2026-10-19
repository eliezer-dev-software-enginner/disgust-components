package disgust.web.feedback;

import disgust.io.Material;
import disgust.web.Severity;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;
import megalodonte.components.Modal;
import megalodonte.components.ProgressBar;

/** Feedback compositions that keep state in the client application. */
public final class Feedback {
    private Feedback() { }

    public static Component alert(Severity severity, String message) {
        return Material.alert(severity.material(), message);
    }
    public static Modal dialog(State<Boolean> open, Component content) {
        return Material.dialog(open, content);
    }
    public static Component snackbar(Component content, State<Boolean> open,
                                     Severity severity, String message) {
        return Material.snackbar(content, open, severity.material(), message);
    }
    public static ProgressBar progress(State<Integer> percent) { return Material.progress(percent); }
}
