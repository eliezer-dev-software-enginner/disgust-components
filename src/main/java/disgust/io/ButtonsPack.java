package disgust.io;

import javafx.scene.paint.Color;
import megalodonte.base.async.RunnableThrowing;
import megalodonte.base.components.IconInterface;
import megalodonte.components.Button;
import megalodonte.props.ButtonProps;
import megalodonte.props.ButtonVariant;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public final class ButtonsPack {

    private ButtonsPack() {}

    private static final double ICON_SIZE = 14; // era 10 — bem pequeno pra acompanhar o texto do botão

    public static Button ContainedButton(String text, ButtonVariant variant, RunnableThrowing onClick) {
        return ContainedButton(text, variant, false, onClick);
    }

    public static Button ContainedButton(String text, ButtonVariant variant, boolean fillWidth, RunnableThrowing onClick) {
        var props = new ButtonProps().variant(variant).filled()
                .paddingLeft(15).paddingRight(15).paddingTop(10).paddingDown(10);
        if (fillWidth) props.fillWidth();
        return new Button(text, props).onClick(onClick);
    }

    public static Button ContainedButtonWithIconStart(String text, ButtonVariant variant, Ikon icon, RunnableThrowing onClick) {
        return ContainedButtonWithIconStart(text, variant, false, icon, onClick);
    }

    public static Button ContainedButtonWithIconStart(String text, ButtonVariant variant, boolean fillWidth, Ikon icon, RunnableThrowing onClick) {
        var props = new ButtonProps().variant(variant).filled()
                .paddingLeft(15).paddingRight(15).paddingTop(10).paddingDown(10);
        if (fillWidth) props.fillWidth();

        var button = new Button(text, props).onClick(onClick);
        // props.getTextColor() só reflete o valor correto depois que o Button
        // termina de construir (Props.apply roda dentro do super(...)).
        button.icon(IconInterface.of(FontIcon.of(icon, (int) ICON_SIZE, Color.web(props.getTextColor()))));
        return button;
    }

    public static Button ContainedButtonWithIconEnd(String text, ButtonVariant variant, Ikon icon, RunnableThrowing onClick) {
        return ContainedButtonWithIconEnd(text, variant, false, icon, onClick);
    }

    public static Button ContainedButtonWithIconEnd(String text, ButtonVariant variant, boolean fillWidth, Ikon icon, RunnableThrowing onClick) {
        var props = new ButtonProps().variant(variant).filled().iconOnRight()
                .paddingLeft(15).paddingRight(15).paddingTop(10).paddingDown(10);
        if (fillWidth) props.fillWidth();

        var button = new Button(text, props).onClick(onClick);
        button.icon(IconInterface.of(FontIcon.of(icon, (int) ICON_SIZE, Color.web(props.getTextColor()))));
        return button;
    }

    public static Button OutlinedButton(String text, ButtonVariant variant, RunnableThrowing onClick) {
        return OutlinedButton(text, variant, false, onClick);
    }

    public static Button OutlinedButton(String text, ButtonVariant variant, boolean fillWidth, RunnableThrowing onClick) {
        var props = new ButtonProps().variant(variant).outlined()
                .paddingLeft(15).paddingRight(15).paddingTop(10).paddingDown(10);
        if (fillWidth) props.fillWidth();
        return new Button(text, props).onClick(onClick);
    }

    public static Button TextButton(String text, ButtonVariant variant, RunnableThrowing onClick) {
        return new Button(text, new ButtonProps().variant(variant).text()
                .paddingLeft(15).paddingRight(15).paddingTop(10).paddingDown(10))
                .onClick(onClick);
    }
}