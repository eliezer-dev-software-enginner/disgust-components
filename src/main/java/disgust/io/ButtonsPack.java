package disgust.io;

import megalodonte.base.async.RunnableThrowing;
import megalodonte.components.Button;
import megalodonte.props.ButtonProps;
import megalodonte.props.ButtonVariant;

public final class ButtonsPack {

    private ButtonsPack() {}

    public static Button ContainedButton(String text, ButtonVariant variant, RunnableThrowing onClick) {
        return ContainedButton(text, variant, true, onClick);
    }

    public static Button ContainedButton(String text, ButtonVariant variant, boolean fillWidth, RunnableThrowing onClick) {
        var props = new ButtonProps().variant(variant).height(31);
        if (fillWidth) props.fillWidth();
        return new Button(text, props).onClick(onClick);
    }

    public static Button OutlinedButton(String text, ButtonVariant variant, RunnableThrowing onClick) {
        return new Button(text, new ButtonProps().variant(variant).outlined().height(31)).onClick(onClick);
    }

    public static Button TextButton(String text, ButtonVariant variant, RunnableThrowing onClick) {
        return new Button(text, new ButtonProps().variant(variant).text().height(31)).onClick(onClick);
    }
}