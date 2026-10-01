package disgust.io;

import javafx.scene.paint.Color;
import megalodonte.base.Redirect;
import megalodonte.base.async.RunnableThrowing;
import megalodonte.base.components.IconInterface;
import megalodonte.base.components.Props;
import megalodonte.components.Button;
import megalodonte.props.ButtonProps;
import megalodonte.props.ButtonSize;
import megalodonte.props.ButtonStyle;
import megalodonte.props.ButtonVariant;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

public final class ButtonsPack {

    private ButtonsPack() {}

    private static final double ICON_SIZE = 14;

    /**
     * Configuração de um botão do pack — dados puros, sem construir nada.
     * Passe pra {@link ButtonsPack#button(String, Props)} pra obter o {@link Button}.
     */
    public static class Props {
        private ButtonVariant variant = ButtonVariant.PRIMARY;
        private ButtonStyle style = ButtonStyle.FILLED;
        private ButtonSize size = ButtonSize.MEDIUM;
        private boolean fillWidth;
        private Ikon iconStart;
        private Ikon iconEnd;
        private String href;
        private RunnableThrowing onClick;

        public Props() {}

        public Props variant(ButtonVariant variant) { this.variant = variant; return this; }
        public Props filled() { this.style = ButtonStyle.FILLED; return this; }
        public Props outlined() { this.style = ButtonStyle.OUTLINED; return this; }
        public Props text() { this.style = ButtonStyle.TEXT; return this; }
        public Props small() { this.size = ButtonSize.SMALL; return this; }
        public Props medium() { this.size = ButtonSize.MEDIUM; return this; }
        public Props large() { this.size = ButtonSize.LARGE; return this; }
        public Props size(ButtonSize size) { this.size = size; return this; }
        public Props fillWidth() { this.fillWidth = true; return this; }
        public Props iconStart(Ikon icon) { this.iconStart = icon; return this; }
        public Props iconEnd(Ikon icon) { this.iconEnd = icon; return this; }
        public Props href(String href) { this.href = href; return this; }
        public Props onClick(RunnableThrowing onClick) { this.onClick = onClick; return this; }
    }

    private static ButtonProps toButtonProps(Props props) {
        var buttonProps = new ButtonProps()
                .paddingLeft(15).paddingRight(15).paddingTop(10).paddingDown(10)
                .variant(props.variant)
                .style(props.style);
        if (props.fillWidth) buttonProps.fillWidth();
        if (props.iconEnd != null) buttonProps.iconOnRight();
        return buttonProps;
    }

    /** Constrói o {@link Button} a partir de um {@link Props} já configurado. */
    private static Button button(String text, Props props) {
        Objects.requireNonNull(text, "text é obrigatório");
        Objects.requireNonNull(props, "props é obrigatório");
        if (props.iconStart != null && props.iconEnd != null) {
            throw new IllegalStateException("Use apenas um entre iconStart e iconEnd, não os dois");
        }
        if (props.href != null && props.onClick != null) {
            throw new IllegalStateException("Use apenas um entre href e onClick, não os dois");
        }
        if (props.href == null && props.onClick == null) {
            throw new IllegalStateException("Defina href(...) ou onClick(...) em Props");
        }

        RunnableThrowing finalOnClick = props.href != null ? () -> Redirect.to(props.href) : props.onClick;

        var buttonProps = toButtonProps(props);
        var btn = new Button(text, buttonProps).onClick(finalOnClick);

        // buttonProps.getTextColor() só reflete o valor correto depois que o Button
        // termina de construir (Props.apply roda dentro do super(...)).
        Ikon icon = props.iconStart != null ? props.iconStart : props.iconEnd;
        if (icon != null) {
            btn.icon(IconInterface.of(FontIcon.of(icon, (int) ICON_SIZE, Color.web(buttonProps.getTextColor()))));
        }

        return btn;
    }

    // ---- Atalhos finos, mantidos pra compatibilidade com código existente ----

    public static Button ContainedButton(String text, ButtonVariant variant, String href) {
        return button(text, new Props().variant(variant).filled().href(href));
    }

    public static Button ContainedButton(String text, ButtonVariant variant, RunnableThrowing onClick) {
        return button(text, new Props().variant(variant).filled().onClick(onClick));
    }

    public static Button ContainedButton(String text, ButtonVariant variant, boolean fillWidth, RunnableThrowing onClick) {
        var props = new Props().variant(variant).filled().onClick(onClick);
        if (fillWidth) props.fillWidth();
        return button(text, props);
    }

    public static Button ContainedButtonWithIconStart(String text, ButtonVariant variant, Ikon icon, RunnableThrowing onClick) {
        return ContainedButtonWithIconStart(text, variant, false, icon, onClick);
    }

    public static Button ContainedButtonWithIconStart(String text, ButtonVariant variant, boolean fillWidth, Ikon icon, RunnableThrowing onClick) {
        var props = new Props().variant(variant).filled().iconStart(icon).onClick(onClick);
        if (fillWidth) props.fillWidth();
        return button(text, props);
    }

    public static Button ContainedButtonWithIconEnd(String text, ButtonVariant variant, Ikon icon, RunnableThrowing onClick) {
        return ContainedButtonWithIconEnd(text, variant, false, icon, onClick);
    }

    public static Button ContainedButtonWithIconEnd(String text, ButtonVariant variant, boolean fillWidth, Ikon icon, RunnableThrowing onClick) {
        var props = new Props().variant(variant).filled().iconEnd(icon).onClick(onClick);
        if (fillWidth) props.fillWidth();
        return button(text, props);
    }

    public static Button OutlinedButton(String text, ButtonVariant variant, RunnableThrowing onClick) {
        return OutlinedButton(text, variant, false, onClick);
    }

    public static Button OutlinedButton(String text, ButtonVariant variant, boolean fillWidth, RunnableThrowing onClick) {
        var props = new Props().variant(variant).outlined().onClick(onClick);
        if (fillWidth) props.fillWidth();
        return button(text, props);
    }

    public static Button TextButton(String text, ButtonVariant variant, RunnableThrowing onClick) {
        return button(text, new Props().variant(variant).text().onClick(onClick));
    }
}