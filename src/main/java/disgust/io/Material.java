package disgust.io;

import disgust.icons.Icon;
import megalodonte.base.components.Component;
import megalodonte.base.async.RunnableThrowing;
import megalodonte.base.state.State;
import megalodonte.base.theme.ThemeColors;
import megalodonte.base.theme.ThemeManager;
import megalodonte.components.Accordion;
import megalodonte.components.Card;
import megalodonte.components.LineHorizontal;
import megalodonte.components.Modal;
import megalodonte.components.ProgressBar;
import megalodonte.components.Slider;
import megalodonte.components.Switch;
import megalodonte.components.Tabs;
import megalodonte.components.Text;
import megalodonte.components.Button;
import megalodonte.components.layout_components.Row;
import megalodonte.components.layout_components.Stack;
import megalodonte.props.CardProps;
import megalodonte.props.RowProps;
import megalodonte.props.TextProps;
import megalodonte.v2.Show;

import java.util.List;
import java.util.Objects;

/** Material-style compositions built entirely from native Megalodonte components. */
public final class Material {
    public enum Severity { INFO, SUCCESS, WARNING, ERROR }

    private Material() {}

    private static ThemeColors colors() {
        return Objects.requireNonNull(ThemeManager.theme(), "Configure the Megalodonte theme first").colors();
    }

    private static String accent(Severity severity) {
        var palette = colors();
        return switch (Objects.requireNonNull(severity, "severity")) {
            case INFO -> palette.primary();
            case SUCCESS -> palette.success();
            case WARNING -> palette.warning();
            case ERROR -> palette.danger();
        };
    }

    /** A non-modal status message with an accessible severity label. */
    public static Card alert(Severity severity, String message) {
        if (message == null || message.isBlank()) throw new IllegalArgumentException("message must not be blank");
        return new Card(new Row(new RowProps().spacingOf(8)).children(
                new Text(severity.name(), new TextProps().bold().textColor(accent(severity))),
                new Text(message, new TextProps().textColor(colors().textPrimary()))),
                new CardProps().bgColor(colors().surface()).borderColor(accent(severity))
                        .borderWidth(1).borderRadius(6).paddingAll(12).fillWidth());
    }

    /** Small status label, suitable for filters and metadata. */
    public static Card chip(String label, Severity severity) {
        if (label == null || label.isBlank()) throw new IllegalArgumentException("label must not be blank");
        return new Card(new Text(label, new TextProps().textColor(colors().surface())),
                new CardProps().bgColor(accent(severity)).borderRadius(16).paddingLeft(10)
                        .paddingRight(10).paddingTop(5).paddingDown(5));
    }

    /** Numbered marker on a framework component. Zero or negative counts hide it. */
    public static Component badge(Component content, int count) {
        Objects.requireNonNull(content, "content");
        var stack = new Stack().children(content);
        if (count > 0) {
            var text = count > 99 ? "99+" : Integer.toString(count);
            stack.childInCorner(chip(text, Severity.INFO), Stack.Corner.TOP_RIGHT, 0);
        }
        return stack;
    }

    public static Card avatar(String initials) {
        if (initials == null || initials.isBlank() || initials.length() > 3) {
            throw new IllegalArgumentException("initials must contain one to three characters");
        }
        return new Card(new Text(initials.toUpperCase(),
                new TextProps().bold().textColor(colors().surface())),
                new CardProps().bgColor(colors().primary()).borderRadius(24).paddingAll(10));
    }

    public static Row breadcrumbs(List<String> labels) {
        Objects.requireNonNull(labels, "labels");
        if (labels.isEmpty()) throw new IllegalArgumentException("breadcrumbs requires a label");
        var row = new Row(new RowProps().spacingOf(8));
        for (int index = 0; index < labels.size(); index++) {
            String label = labels.get(index);
            if (label == null || label.isBlank()) throw new IllegalArgumentException("breadcrumb label is blank");
            if (index > 0) row.r_child(new Text("/", new TextProps().textColor(colors().textSecondary())));
            row.r_child(new Text(label, new TextProps().textColor(index == labels.size() - 1
                    ? colors().textPrimary() : colors().textSecondary())));
        }
        return row;
    }

    public static Row buttonGroup(List<Button> buttons) {
        Objects.requireNonNull(buttons, "buttons");
        if (buttons.isEmpty()) throw new IllegalArgumentException("buttonGroup requires a button");
        return new Row(new RowProps().spacingOf(4)).items(buttons);
    }

    public static Button floatingActionButton(Icon icon, String accessibleLabel, RunnableThrowing action) {
        return ButtonsPack.IconButton(icon, accessibleLabel, action);
    }

    public static LineHorizontal divider() { return new LineHorizontal(); }

    public static Card paper(Component content) {
        return new Card(Objects.requireNonNull(content, "content"),
                new CardProps().bgColor(colors().surface()).borderRadius(6).paddingAll(16));
    }

    public static Modal dialog(State<Boolean> open, Component content) {
        return new Modal(Objects.requireNonNull(open, "open"), paper(content));
    }

    /** Place this over the current screen content to show a controlled message. */
    public static Stack snackbar(Component content, State<Boolean> open, Severity severity, String message) {
        return new Stack().children(Objects.requireNonNull(content, "content"))
                .childInCorner(Show.when(Objects.requireNonNull(open, "open"), () -> alert(severity, message)),
                        Stack.Corner.BOTTOM_LEFT, 16);
    }

    public static Accordion accordion(String title, Component content, State<Boolean> expanded) {
        return new Accordion(title, content, expanded);
    }

    public static Tabs tabs(List<Tabs.Page> pages, State<Integer> selected) {
        return new Tabs(pages, selected);
    }

    public static Switch switchControl(String accessibleLabel, State<Boolean> selected) {
        return new Switch(accessibleLabel, selected);
    }

    public static Slider slider(double min, double max, State<Double> value) {
        return new Slider(min, max, value);
    }

    public static ProgressBar progress(State<Integer> percent) {
        return new ProgressBar(Objects.requireNonNull(percent, "percent"));
    }
}
