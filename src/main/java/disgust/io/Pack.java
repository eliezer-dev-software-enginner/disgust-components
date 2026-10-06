package disgust.io;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import megalodonte.ComputedState;
import megalodonte.application.ErrorReporter;
import megalodonte.base.async.RunnableThrowing;
import megalodonte.base.components.Component;
import megalodonte.base.components.IconInterface;
import megalodonte.base.components.Ref;
import megalodonte.base.state.ReadableState;
import megalodonte.base.state.State;
import megalodonte.base.theme.ThemeManager;
import megalodonte.components.*;
import megalodonte.components.Button;
import megalodonte.components.DatePicker;
import megalodonte.components.inputs.OnChangeResult;
import megalodonte.components.inputs.TextAreaInput;
import megalodonte.components.layout_components.Column;
import megalodonte.components.layout_components.Container;
import megalodonte.components.layout_components.Row;
import megalodonte.components.v2.Input;
import megalodonte.props.*;
import megalodonte.props.v2.InputProps;
import megalodonte.v2.ListState;
import disgust.icons.Icon;
import disgust.icons.Feather;


import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

/**
 * Componentes genéricos de composição, sem dependência de tipos de domínio de
 * aplicações específicas. Locale/formatação BR (CPF, CNPJ, CEP, moeda) vive
 * separadamente em {@code disgust.io.br.Pack}.
 */
public final class Pack {

    private Pack() {}

    // ---------------- Ícones ----------------

    public static Component icon(Icon icon, double size, String color) {
        return icon.create(size, Color.web(color));
    }

    // ---------------- Texto / exibição ----------------

    public static Component imageWithTextRow(String imgPath, String text) {
        return new Row().children(
                new Image(imgPath, new ImageProps().size(25)),
                new SpacerHorizontal(5),
                new Text(text, new TextProps().textColor("white").fontSize(14))
        );
    }

    public static Row TextWithDetails(String label, Object value, boolean wrapText) {
        var comp = new Text(value == null ? "" : value.toString(),
                new TextProps().fontSize(ThemeManager.theme().typography().body()));

        var textValueComponent = wrapText ? new TextFlow(comp) : comp;

        return new Row()
                .children(
                        new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().body()).bold()),
                        textValueComponent
                );
    }

    public static Row TextWithDetails(String label, Object value) {
        return TextWithDetails(label, value, false);
    }

    public static Column TextColumn(String label, String value) {
        return new Column(new ColumnProps())
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().body()).bold()))
                .c_child(new Text(value, new TextProps().fontSize(ThemeManager.theme().typography().body())));
    }

    public static Component TextWithValue(String label, ReadableState<String> valueState) {
        return new Row()
                .r_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().body()).bold()))
                .r_child(new Text(valueState, new TextProps().fontSize(ThemeManager.theme().typography().body())));
    }

    public static Text FormTitle(String title, String textColor) {
        return new Text(title, new TextProps().fontSize(ThemeManager.theme().typography().title()).bold().textColor(textColor));
    }

    public static Text FormTitle(String title) {
        return new Text(title, new TextProps().fontSize(ThemeManager.theme().typography().title()).bold());
    }

    public static Text FormSubtitle(String title, String color) {
        return new Text(title, new TextProps().fontSize(ThemeManager.theme().typography().subtitle())
                .textColor(color));
    }

    public static Text FormSubtitle(String title) {
        return FormSubtitle(title, "black");
    }

    // ---------------- Scroll / layout util ----------------

    public static Component ScrollPaneDefault(Component child) {
        var scroll = new ScrollPane();
        scroll.setContent(child.getJavaFxNode());
        VBox.setVgrow(scroll, Priority.ALWAYS);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        // -fx-background-color no node raiz não alcança a subestrutura do skin
        // (.viewport, .corner) — cada um tem seu próprio background opaco herdado
        // do modena.css. -fx-background é a variável que o modena.css usa
        // internamente tanto pro scroll-pane quanto pro viewport.
        scroll.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;" +
                        "-fx-border-color: transparent;"
        );

        return Component.CreateFromJavaFxNode(scroll);
    }

    // ---------------- Date picker ----------------

    public static Component DatePickerColumn(State<LocalDate> localDateState, String label) {
        return DatePickerColumn(localDateState, label, null);
    }

    public static Component DatePickerColumn(State<LocalDate> localDateState, String label, IconInterface icon) {
        var datePicker = new DatePicker(localDateState,
                new DatePickerProps().fontSize(ThemeManager.theme().typography().small())
                        .placeHolder("dd/mm/yyyy")
                        .locale(new Locale("pt", "BR"))
                        .pattern("dd/MM/yyyy")
                        .width(140)
                        .editable(false)
        );

        if (icon != null) {
            datePicker.icon(icon);
        }

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(datePicker);
    }

    // ---------------- Imagem ----------------

    public static Column ImageSelector(String title, State<String> imageState,
                                       ImageProps props,
                                       RunnableThrowing callback) {
        return new Column()
                .c_child(new Image(imageState, props))
                .c_child(new SpacerVertical(10))
                .c_child(PrimaryActionButton(title, callback));
    }

    /**
     * Card com preview de imagem + botão de troca. {@code label} descreve o que a
     * imagem representa (ex.: "Foto do produto", "Avatar", "Logo").
     */
    public static Card CardImageSelector(String label, State<String> imagemState, RunnableThrowing handleChangeImage) {
        return new Card(
                new Column(new ColumnProps().centerHorizontally().spacingOf(15))
                        .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().body()).bold()))
                        .c_child(new Image(imagemState, new ImageProps().size(120)))
                        .c_child(new SpacerVertical().fill())
                        .c_child(new Button("Inserir imagem",
                                new ButtonProps().fontSize(ThemeManager.theme().typography().small()).bgColor("#A6B1E1"))
                                .onClick(handleChangeImage)
                        ),
                new CardProps().height(300).paddingAll(20)
        );
    }

    // ---------------- Botões ----------------

    static final ButtonProps propsPrimaryAction = new ButtonProps().fillWidth().height(31)
            .fontSize(ThemeManager.theme().typography().small()).textColor("white").bgColor("#2563eb");

    public static Button PrimaryActionButton(String text, RunnableThrowing onClick) {
        return new Button(text, propsPrimaryAction).onClick(onClick);
    }

    public static Component PrimaryActionButton(ComputedState<String> text, RunnableThrowing onClick) {
        return new Button(text, propsPrimaryAction).onClick(onClick);
    }

    public static Component actionButtons(ComputedState<String> btnText, RunnableThrowing onClick) {
        return new Button(btnText,
                new ButtonProps()
                        .fillWidth()
                        .height(31)
                        .fontSize(16)
                        .textColor("white").bgColor("#10b981")
        ).onClick(onClick);
    }

    public static Component MenuItem(String title, Icon definition, String color, Runnable onClick) {
        var icon = definition.create(25, Color.web(color));

        return new Clickable(new Card(
                new Column(new ColumnProps().centerHorizontally())
                        .c_child(icon)
                        .c_child(new SpacerVertical(6))
                        .c_child(new Text(title, new TextProps().fontSize(ThemeManager.theme().typography().small())))
        ), onClick);
    }

    // ---------------- Select ----------------

    private final static SelectProps selectProps = new SelectProps()
            .minWidth(100)
            .height(31);

    public static <T> Component SelectColumn(String label, State<List<T>> listState, State<T> stateSelected, Function<T, String> display) {
        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(new Select<T>(selectProps)
                        .items(listState)
                        .value(stateSelected)
                        .displayText(display)
                );
    }

    public static <T> Component SelectColumn(String label, List<T> list, State<T> stateSelected, Function<T, String> display) {
        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(new Select<T>(selectProps)
                        .items(list)
                        .value(stateSelected)
                        .displayText(display)
                );
    }

    public static <T> Component SelectColumn(String label, State<List<T>> list, State<T> stateSelected, Function<T, String> display, boolean compareById) {
        var select = new Select<T>(selectProps)
                .items(list)
                .value(stateSelected)
                .displayText(display);

        if (compareById) select.compareById();

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(select);
    }

    public static <T> Component SelectColumn(String label, ListState<T> list, State<T> stateSelected, Function<T, String> display,
                                             boolean compareById, ReadableState<Boolean> expandAutomatically) {
        var select = new Select<T>(selectProps)
                .items(list)
                .displayText(display)
                .value(stateSelected);

        if (compareById) select.compareById();
        if (expandAutomatically != null) select.expandWhen(expandAutomatically);

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(select);
    }

    public static <T> Component SelectColumn(String label, ListState<T> list, State<T> stateSelected, Function<T, String> display, boolean compareById) {
        var select = new Select<T>(selectProps)
                .items(list)
                .value(stateSelected)
                .displayText(display);

        if (compareById) select.compareById();

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(select);
    }

    public static <T> Component SelectColumnWithButton(
            String label, ListState<T> list, State<T> stateSelected,
            Function<T, String> display, boolean compareById,
            String btnText, RunnableThrowing handleClick) {

        var rowProps = new RowProps().spacingOf(2).bottomVertically();

        return new Row(rowProps)
                .r_child(SelectColumn(label, list, stateSelected, display, compareById))
                .r_child(new Button(btnText, new ButtonProps().height(31)
                        .textColor("#FFF")).onClick(handleClick)
                ).r_child(new SpacerVertical(2));
    }

    // ---------------- Input genérico ----------------
@Deprecated(forRemoval = true)
    public static InputProps getInputPropsV2(String placeholder) {
        return new InputProps()
                .placeHolder(placeholder).fontSize(ThemeManager.theme().typography().small());
    }


    public static megalodonte.props.InputProps getInputProps(String placeholder) {
        return new megalodonte.props.InputProps()
                .placeHolder(placeholder).fontSize(ThemeManager.theme().typography().small());
    }

    public static Component InputColumnNumeric(String label, State<String> inputState, String placeholder) {
        var inputProps = getInputPropsV2(placeholder).width(100);

        var input = new Input(inputState, inputProps)
                .onChange(value -> {
                    String numeric = value.replaceAll("[^0-9]", "");
                    if (numeric.isEmpty()) return OnChangeResult.of("", "");
                    return OnChangeResult.of(numeric, numeric);
                })
                .lockCursorToEnd();

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(input);
    }

    public static Component InputColumnComEnterHandler(String label, ReadableState<String> inputState, String placeholder, Runnable onEnter) {
        return InputColumnComEnterHandler(label, inputState, placeholder, onEnter, null);
    }

    public static Component InputColumnComEnterHandler(String label, ReadableState<String> inputState, String placeholder,
                                                       Runnable onEnter, Ref<Input> ref) {
        var input = new Input((State<String>) inputState,
                getInputPropsV2(placeholder).width(100).borderWidth(ThemeManager.theme().border().width())
                        .borderColor(ThemeManager.theme().colors().border()).borderRadius(ThemeManager.theme().border().radiusMd())
        ).onEnter(onEnter);
        if (ref != null) input.ref(ref);

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(input);
    }

    public static Column InputColumn(String label, ReadableState<String> inputState, String placeholder, boolean disableInput,
                                     int borderWidth, int borderRadius, String borderColor, String labelColor,
                                     Integer width, Integer height) {
        var props = getInputProps(placeholder);
        if (disableInput) props.disable();
        props.width(width != null ? width : 220);

        if(height!=null) props.height(height);

        TextProps labelProps = new TextProps().fontSize(ThemeManager.theme().typography().small());
        if (labelColor != null) {
            labelProps.textColor(labelColor);
            labelProps.textColor(labelColor);
        }

        return new Column()
                .c_child(new Text(label, labelProps))
                .c_child(new megalodonte.components.inputs.Input((State<String>) inputState,
                        props.borderWidth(borderWidth).borderColor(borderColor).borderRadius(borderRadius)
                ));
    }

    public static Column InputColumn(String label, ReadableState<String> inputState, String placeholder, boolean disableInput,
                                     String labelColor, Integer width, Integer height) {
        return InputColumn(label, inputState, placeholder, disableInput, ThemeManager.theme().border().width(),
                ThemeManager.theme().border().radiusMd(),
                ThemeManager.theme().colors().border(), labelColor, width, height);
    }

    public static Column InputColumn(String label, ReadableState<String> inputState, String placeholder, boolean disableInput,
                                     String labelColor, Integer width) {
        return InputColumn(label, inputState, placeholder, disableInput, ThemeManager.theme().border().width(),
                ThemeManager.theme().border().radiusMd(),
                ThemeManager.theme().colors().border(), labelColor, width, null);
    }

    public static Column InputColumn(String label, ReadableState<String> inputState, String placeholder, boolean disableInput, String labelColor) {
        return InputColumn(label, inputState, placeholder, disableInput, labelColor, null);
    }

    public static Column InputColumn(String label, ReadableState<String> inputState, String placeholder, boolean disableInput) {
        return InputColumn(label, inputState, placeholder, disableInput, null, null);
    }

    public static Column InputColumn(String label, ReadableState<String> inputState, String placeholder, boolean disableInput, Integer width) {
        return InputColumn(label, inputState, placeholder, disableInput, null, width);
    }

    public static Column InputColumnAuthFill(String label, ReadableState<String> inputState, String placeholder) {
        var props = getInputPropsV2(placeholder);
        props.height(35);

        TextProps labelProps = new TextProps().fontSize(ThemeManager.theme().typography().body());

        return new Column(new ColumnProps().fillWidth())
                .c_child(new Text(label, labelProps))
                .c_child(new Input((State<String>) inputState,
                        props.borderWidth(ThemeManager.theme().border().width())
                                .borderColor(ThemeManager.theme().colors().border())
                                .borderRadius(ThemeManager.theme().border().radiusMd())
                                .maxWidth(300)
                ));
    }

    public static Column InputColumnAuth(String label, ReadableState<String> inputState, String placeholder, int width) {
        return InputColumn(label, inputState, placeholder, false, width);
    }

    public static Column InputColumn(String label, ReadableState<String> inputState, String placeholder, Integer width) {
        return InputColumn(label, inputState, placeholder, false, width);
    }

    public static Column InputColumn(String label, ReadableState<String> inputState, String placeholder) {
        return InputColumn(label, inputState, placeholder, false);
    }

    public static Component InputWithButtonRow(String label, String placeholder, String btnTitle, State<String> inputState, RunnableThrowing onClick) {
        return new Row(new RowProps().bottomVertically())
                .r_child(InputColumn(label, inputState, placeholder))
                .r_child(new Button(btnTitle, new ButtonProps().height(32).textColor("#FFF")
                        .borderRadius(ThemeManager.theme().border().radiusSm()).borderWidth(ThemeManager.theme().border().width()).borderColor(ThemeManager.theme().colors().primary())
                ).onClick(onClick));
    }

    /**
     * Input de busca com ícone. {@code fillWidth=true} estica na largura do pai;
     * caso contrário usa {@code fixedWidth} (padrão 300 se não informado).
     */
    public static Component searchInput(State<String> stateInput, String placeholder, boolean fillWidth, Integer fixedWidth) {
        var icon = Feather.SEARCH.create(20, Color.web(ThemeManager.theme().colors().secondary()));
        var props = new InputProps().placeHolder(placeholder).height(31);

        if (fillWidth) {
            props.fillWidth();
        } else {
            props.width(fixedWidth != null ? fixedWidth : 300);
        }

        return new Input(stateInput, props).left(icon.getJavaFxNode());
    }

    public static Component searchInput(State<String> stateInput, String placeholder) {
        return searchInput(stateInput, placeholder, false, null);
    }

    // ---------------- TextArea ----------------

    public static Component TextAreaColumn(String label, State<String> inputState, String placeholder) {
        return TextAreaColumn(label, inputState, placeholder, 80);
    }

    public static Component TextAreaColumnWidthNoRestricted(String label, State<String> inputState, String placeholder, int height) {
        TextAreaInput textAreaInput = new TextAreaInput(inputState,
                new megalodonte.props.InputProps().height(height)
                        .placeHolder(placeholder).fontSize(ThemeManager.theme().typography().small())
        );

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(textAreaInput);
    }

    public static Component TextAreaColumn(String label, State<String> inputState, String placeholder, int height) {
        TextAreaInput textAreaInput = new TextAreaInput(inputState,
                new megalodonte.props.InputProps().height(height)
                        .placeHolder(placeholder).fontSize(ThemeManager.theme().typography().small())
                        .width(400)
        );

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(textAreaInput);
    }

    // Variante que cresce com o conteúdo em vez de ficar travado numa altura fixa.
    // Não reaproveita o helper de height único: InputProps.applyTextAreaTheme
    // prioriza "height" sobre "maxHeight" quando os dois estão setados, então o
    // campo ficaria travado em vez de crescer.
    public static Component TextAreaColumn(String label, State<String> inputState, String placeholder, int minHeight, int maxHeight) {
        TextAreaInput textAreaInput = new TextAreaInput(inputState,
                new megalodonte.props.InputProps()
                        .placeHolder(placeholder)
                        .fontSize(ThemeManager.theme().typography().small())
                        .minHeight(minHeight)
                        .maxHeight(maxHeight)
                        .width(400)
        );

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(textAreaInput);
    }
}
