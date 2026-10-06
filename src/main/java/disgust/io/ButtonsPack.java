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
import disgust.icons.Icon;
import megalodonte.base.components.Component;


import java.util.Objects;

public final class ButtonsPack {

    private ButtonsPack() {}

    private static final double ICON_SIZE = 14;

    public static Button IconButton(Component icon, String accessibleLabel, RunnableThrowing onClick) {
        Objects.requireNonNull(icon, "icon");
        return IconButton((size, color) -> icon, accessibleLabel, onClick);
    }

    /** Icon-only action with a required accessible name. */
    public static Button IconButton(Icon icon, String accessibleLabel, RunnableThrowing onClick) {
        Objects.requireNonNull(icon, "icon");
        if (accessibleLabel == null || accessibleLabel.isBlank()) {
            throw new IllegalArgumentException("accessibleLabel é obrigatório");
        }
        var button = button("", new Props().iconStart(icon).onClick(onClick));
        var node = (javafx.scene.control.Button) button.getJavaFxNode();
        node.setAccessibleText(accessibleLabel);
        node.setContentDisplay(javafx.scene.control.ContentDisplay.GRAPHIC_ONLY);
        node.setOnMouseClicked(null);
        node.setOnAction(event -> {
            try { onClick.run(); }
            catch (Exception e) { megalodonte.application.ErrorReporter.handle(e); }
        });
        return button;
    }

    /**
     * Configuração de um botão do pack — dados puros, sem construir nada.
     * Passe pra {@link ButtonsPack#button(String, Props)} pra obter o {@link Button}.
     */
    public static class Props {
        private ButtonVariant variant = ButtonVariant.PRIMARY;
        private ButtonStyle style = ButtonStyle.FILLED;
        private ButtonSize size = ButtonSize.MEDIUM;
        private boolean fillWidth;
        private Icon iconStart;
        private Icon iconEnd;
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
        public Props iconStart(Icon icon) { this.iconStart = icon; return this; }
        public Props iconEnd(Icon icon) { this.iconEnd = icon; return this; }
        /** A component instance belongs to one button; use Icon for a reusable factory. */
        public Props iconStart(Component icon) {
            Objects.requireNonNull(icon, "icon");
            return iconStart((size, color) -> icon);
        }
        public Props iconEnd(Component icon) {
            Objects.requireNonNull(icon, "icon");
            return iconEnd((size, color) -> icon);
        }
        public Props href(String href) { this.href = href; return this; }
        public Props onClick(RunnableThrowing onClick) { this.onClick = onClick; return this; }
        public Props style(ButtonStyle style) {this.style = style; return this; }
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
        Icon icon = props.iconStart != null ? props.iconStart : props.iconEnd;
        if (icon != null) {
            btn.icon(IconInterface.of(icon.create(ICON_SIZE, Color.web(buttonProps.getTextColor())).getJavaFxNode()));
        }

        return btn;
    }

    // ---- Atalhos finos, mantidos pra compatibilidade com código existente ----

    private static Button buildShortcut(String text, ButtonVariant variant, ButtonStyle style,
                                        boolean fillWidth, Icon iconStart, Icon iconEnd,
                                        String href, RunnableThrowing onClick) {
        var props = new Props().variant(variant).style(style);
        if (fillWidth) props.fillWidth();
        if (iconStart != null) props.iconStart(iconStart);
        if (iconEnd != null) props.iconEnd(iconEnd);
        if (href != null) props.href(href);
        if (onClick != null) props.onClick(onClick);
        return button(text, props);
    }

    // -- Contained --

    public static Button ContainedButton(String text, ButtonVariant variant, String href) {
        return buildShortcut(text, variant, ButtonStyle.FILLED, false, null, null, href, null);
    }

    public static Button ContainedButton(String text, ButtonVariant variant, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.FILLED, false, null, null, null, onClick);
    }

    public static Button ContainedButton(String text, ButtonVariant variant, boolean fillWidth, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.FILLED, fillWidth, null, null, null, onClick);
    }

    public static Button ContainedButtonWithIconStart(String text, ButtonVariant variant, Icon icon, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.FILLED, false, icon, null, null, onClick);
    }

    public static Button ContainedButtonWithIconStart(String text, ButtonVariant variant, boolean fillWidth, Icon icon, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.FILLED, fillWidth, icon, null, null, onClick);
    }

    public static Button ContainedButtonWithIconEnd(String text, ButtonVariant variant, Icon icon, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.FILLED, false, null, icon, null, onClick);
    }

    public static Button ContainedButtonWithIconEnd(String text, ButtonVariant variant, boolean fillWidth, Icon icon, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.FILLED, fillWidth, null, icon, null, onClick);
    }

    public static Button ContainedButton(String text, Props props) {
        return button(text, props.filled());
    }

    // -- Outlined --

    public static Button OutlinedButton(String text, ButtonVariant variant, String href) {
        return buildShortcut(text, variant, ButtonStyle.OUTLINED, false, null, null, href, null);
    }

    public static Button OutlinedButton(String text, ButtonVariant variant, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.OUTLINED, false, null, null, null, onClick);
    }

    public static Button OutlinedButton(String text, ButtonVariant variant, boolean fillWidth, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.OUTLINED, fillWidth, null, null, null, onClick);
    }

    public static Button OutlinedButtonWithIconStart(String text, ButtonVariant variant, Icon icon, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.OUTLINED, false, icon, null, null, onClick);
    }

    public static Button OutlinedButtonWithIconStart(String text, ButtonVariant variant, boolean fillWidth, Icon icon, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.OUTLINED, fillWidth, icon, null, null, onClick);
    }

    public static Button OutlinedButtonWithIconEnd(String text, ButtonVariant variant, Icon icon, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.OUTLINED, false, null, icon, null, onClick);
    }

    public static Button OutlinedButtonWithIconEnd(String text, ButtonVariant variant, boolean fillWidth, Icon icon, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.OUTLINED, fillWidth, null, icon, null, onClick);
    }

    public static Button OutlinedButton(String text, Props props) {
        return button(text, props.outlined());
    }

    // -- Text --

    public static Button TextButton(String text, ButtonVariant variant, String href) {
        return buildShortcut(text, variant, ButtonStyle.TEXT, false, null, null, href, null);
    }

    public static Button TextButton(String text, ButtonVariant variant, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.TEXT, false, null, null, null, onClick);
    }

    public static Button TextButton(String text, ButtonVariant variant, boolean fillWidth, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.TEXT, fillWidth, null, null, null, onClick);
    }

    public static Button TextButtonWithIconStart(String text, ButtonVariant variant, Icon icon, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.TEXT, false, icon, null, null, onClick);
    }

    public static Button TextButtonWithIconStart(String text, ButtonVariant variant, boolean fillWidth, Icon icon, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.TEXT, fillWidth, icon, null, null, onClick);
    }

    public static Button TextButtonWithIconEnd(String text, ButtonVariant variant, Icon icon, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.TEXT, false, null, icon, null, onClick);
    }

    public static Button TextButtonWithIconEnd(String text, ButtonVariant variant, boolean fillWidth, Icon icon, RunnableThrowing onClick) {
        return buildShortcut(text, variant, ButtonStyle.TEXT, fillWidth, null, icon, null, onClick);
    }

    public static Button TextButton(String text, Props props) {
        return button(text, props.text());
    }

}
