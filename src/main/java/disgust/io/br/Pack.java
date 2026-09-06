package disgust.io.br;

import javafx.scene.paint.Color;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;
import megalodonte.base.theme.ThemeManager;
import megalodonte.components.Text;
import megalodonte.components.inputs.OnChangeResult;
import megalodonte.components.layout_components.Column;
import megalodonte.components.v2.Input;
import megalodonte.props.TextProps;
import megalodonte.props.v2.InputProps;
import org.kordamp.ikonli.entypo.Entypo;
import org.kordamp.ikonli.javafx.FontIcon;
import pack.utilities.FormatterPack;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Variantes de input com formatação/máscara específica de locale BR (CPF, CNPJ,
 * CEP, telefone, moeda). Depende de {@code pack.utilities.FormatterPack} — se
 * essa utilidade também for parte do "disgust", mova-a pra dentro deste módulo;
 * caso contrário, o app consumidor precisa fornecê-la no classpath.
 */
public final class Pack {

    private Pack() {}

    private static InputProps getInputPropsV2(String placeholder, int width) {
        return disgust.io.Pack.getInputPropsV2(placeholder).width(width);
    }

    public static Component InputColumnCep(String label, State<String> inputState) {
        var inputProps = getInputPropsV2("00000-000", 120);

        var input = new Input(inputState, inputProps)
                .onInitialize(value -> OnChangeResult.of(FormatterPack.formatCep(value), value))
                .onChange(value -> {
                    String numeric = value.replaceAll("[^0-9]", "");
                    if (numeric.length() > 8) numeric = numeric.substring(0, 8);
                    return OnChangeResult.of(FormatterPack.formatCep(numeric), numeric);
                })
                .lockCursorToEnd();

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(input);
    }

    public static Component InputColumnCpf(String label, State<String> inputState) {
        var inputProps = getInputPropsV2("000.000.000-00", 160);

        var input = new Input(inputState, inputProps)
                .onInitialize(value -> OnChangeResult.of(FormatterPack.formatCpf(value), value))
                .onChange(value -> {
                    String numeric = value.replaceAll("[^0-9]", "");
                    if (numeric.length() > 11) numeric = numeric.substring(0, 11);
                    return OnChangeResult.of(FormatterPack.formatCpf(numeric), numeric);
                })
                .lockCursorToEnd();

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(input);
    }

    public static Component InputColumnCnpjAlfanumerico(String label, State<String> inputState) {
        var inputProps = getInputPropsV2("AA.AAA.AAA/AAAA-DD", 190);

        var input = new Input(inputState, inputProps)
                .onInitialize(value -> OnChangeResult.of(FormatterPack.formatCnpj(value), value))
                .onChange(value -> {
                    String raw = value.toUpperCase().replaceAll("[^0-9A-Z]", "");
                    if (raw.length() > 14) raw = raw.substring(0, 14);
                    return OnChangeResult.of(FormatterPack.formatCnpj(raw), raw);
                })
                .lockCursorToEnd();

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(input);
    }

    public static Component InputColumnPhone(String label, State<String> inputState) {
        var inputProps = getInputPropsV2("(00) 00000-0000", 160);

        var input = new Input(inputState, inputProps)
                .onInitialize(value -> OnChangeResult.of(FormatterPack.formatPhone(value), value))
                .onChange(value -> {
                    String numeric = value.replaceAll("[^0-9]", "");
                    if (numeric.length() > 11) numeric = numeric.substring(0, 11);
                    return OnChangeResult.of(FormatterPack.formatPhone(numeric), numeric);
                })
                .lockCursorToEnd();

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(input);
    }

    private static final NumberFormat BRL = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public static Component InputColumnCurrency(String label, State<String> inputState, boolean disableInput) {
        var fonticon = FontIcon.of(Entypo.CREDIT, 15, Color.web("green"));
        var inputProps = getInputPropsV2("R$ 0,00", 140);
        if (disableInput) inputProps.disable();

        // inputState armazena valores brutos (em centavos), campo exibe formato BRL
        var input = new Input(inputState, inputProps)
                .onInitialize(value -> {
                    if (value.matches("\\d+")) {
                        BigDecimal realValue = new BigDecimal(value).movePointLeft(2);
                        return OnChangeResult.of(BRL.format(realValue), value);
                    }
                    return OnChangeResult.of(value, value);
                })
                .onChange(value -> {
                    String numeric = value.replaceAll("[^0-9]", "");
                    if (numeric.isEmpty()) return OnChangeResult.of("R$ 0,00", "0");
                    BigDecimal realValue = new BigDecimal(numeric).movePointLeft(2);
                    return OnChangeResult.of(BRL.format(realValue), numeric);
                })
                .lockCursorToEnd()
                .left(fonticon);

        return new Column()
                .c_child(new Text(label, new TextProps().fontSize(ThemeManager.theme().typography().small())))
                .c_child(input);
    }

    public static Component InputColumnCurrency(String label, State<String> inputState) {
        return InputColumnCurrency(label, inputState, false);
    }
}