package disgust.ui;

import disgust.icons.Feather;
import disgust.io.ButtonsPack;
import disgust.io.Pack;
import disgust.io.Tooltip;
import megalodonte.base.components.Component;
import megalodonte.base.components.ScreenComponent;
import megalodonte.base.state.State;
import megalodonte.components.Card;
import megalodonte.components.Text;
import megalodonte.components.TextFlow;
import megalodonte.components.inputs.OnChangeResult;
import megalodonte.components.layout_components.Column;
import megalodonte.components.layout_components.Row;
import megalodonte.components.v2.Input;
import megalodonte.props.*;
import megalodonte.props.v2.InputProps;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;

/** Interactive examples composed entirely through the Megalodonte Component API. */
public final class ShowcaseScreen implements ScreenComponent {
    enum Section {
        BUTTONS("Botões", Feather.MOUSE_POINTER),
        TOOLTIPS("Tooltip & IconButton", Feather.MESSAGE_SQUARE),
        ICONS("Ícones Feather", Feather.GRID),
        FORMS("Campos de formulário", Feather.EDIT_3),
        BRAZIL("Máscaras brasileiras", Feather.DOLLAR_SIGN),
        COMPOSITION("Texto & composição", Feather.LAYERS);

        final String title;
        final Feather icon;
        Section(String title, Feather icon) { this.title = title; this.icon = icon; }
    }

    private final State<String> feedback = State.of("Explore os exemplos e experimente os componentes.");
    private final State<String> iconCount = State.of("");
    private final State<String> iconQuery = State.of("");
    private final PreviewHost content = new PreviewHost();
    private final PreviewHost iconResults = new PreviewHost();
    private final EnumMap<Section, Component> previews = new EnumMap<>(Section.class);
    private final List<Tooltip> tooltips = new ArrayList<>();
    private Component root;

    @Override
    public Component render() {
        if (root != null) return root;
        var navigation = new Column(new ColumnProps().width(240).spacingOf(10).paddingAll(20).bgColor("#ffffff"))
                .c_child(label("COMPONENTES", 11, "#64748b"));
        for (Section section : Section.values()) {
            var item = ButtonsPack.TextButton(section.title, new ButtonsPack.Props()
                    .fillWidth().iconStart(section.icon).onClick(() -> select(section)));
            item.getJavaFxNode().setId("nav-" + section.name().toLowerCase(Locale.ROOT));
            navigation.c_child(item);
        }
        navigation.c_child(label("Megalodonte · JavaFX", 12, "#94a3b8"));

        var header = new Column(new ColumnProps().paddingAll(24).spacingOf(8).bgColor("#0f172a"))
                .c_child(label("Disgust UI", 30, "#ffffff"))
                .c_child(label("Mostruário de componentes · exemplos vivos para experimentar", 14, "#cbd5e1"));
        var body = new Row(new RowProps().fillHeight().fillWidth().spacingOf(20).paddingAll(20))
                .r_child(navigation)
                .r_child(new Column(new ColumnProps().fillWidth().fillHeight()).c_child(content));
        var status = new Column(new ColumnProps().paddingAll(16).bgColor("#e2e8f0"))
                .c_child(new Text(feedback, new TextProps().fontSize(13).textColor("#334155")));
        root = new Column(new ColumnProps().fillHeight().bgColor("#f1f5f9"))
                .children(header, body, status);
        select(Section.BUTTONS);
        return root;
    }

    void select(Section section) {
        Component preview = previews.computeIfAbsent(section, this::preview);
        content.show(preview);
    }

    private Component preview(Section section) {
        Component examples = switch (section) {
            case BUTTONS -> buttons();
            case TOOLTIPS -> tooltipExamples();
            case ICONS -> icons();
            case FORMS -> forms();
            case BRAZIL -> brazil();
            case COMPOSITION -> composition();
        };
        return Pack.ScrollPaneDefault(new Column(new ColumnProps().spacingOf(20).paddingAll(8))
                .c_child(label(section.title, 26, "#0f172a"))
                .c_child(examples));
    }

    private Component buttons() {
        var examples = new Column(new ColumnProps().spacingOf(20));
        for (ButtonStyle style : ButtonStyle.values()) {
            var row = row();
            for (ButtonVariant variant : List.of(ButtonVariant.PRIMARY, ButtonVariant.SECONDARY,
                    ButtonVariant.SUCCESS, ButtonVariant.WARNING, ButtonVariant.DANGER)) {
                var props = new ButtonsPack.Props().variant(variant)
                        .onClick(() -> feedback.set("Botão " + style + " · " + variant + " acionado."));
                row.r_child(switch (style) {
                    case FILLED -> ButtonsPack.ContainedButton(name(variant), props);
                    case OUTLINED -> ButtonsPack.OutlinedButton(name(variant), props);
                    case TEXT -> ButtonsPack.TextButton(name(variant), props);
                });
            }
            examples.c_child(example(switch (style) {
                case FILLED -> "Preenchidos";
                case OUTLINED -> "Contorno";
                case TEXT -> "Texto";
            }, "As mesmas ações com diferentes estilos e cores semânticas.", row));
        }
        examples.c_child(example("Ícones e estados", "Ícone inicial, final, personalizado e botão desabilitado.", row()
                .r_child(ButtonsPack.ContainedButton("Salvar", new ButtonsPack.Props().iconStart(Feather.SAVE)
                        .onClick(() -> feedback.set("Exemplo de salvar acionado."))))
                .r_child(ButtonsPack.OutlinedButton("Avançar", new ButtonsPack.Props().iconEnd(Feather.ARROW_RIGHT)
                        .onClick(() -> feedback.set("Exemplo de avançar acionado."))))
                .r_child(ButtonsPack.TextButton("Favorito", new ButtonsPack.Props()
                        .iconStart(new Text("★", new TextProps().fontSize(20).textColor("#f59e0b")))
                        .onClick(() -> feedback.set("Ícone personalizado como Component."))))
                .r_child(ButtonsPack.ContainedButton("Indisponível", new ButtonsPack.Props().variant(ButtonVariant.DISABLED)
                        .onClick(() -> {})))));
        return examples;
    }

    private Component tooltipExamples() {
        var favorite = State.of(false);
        var favoriteLabel = State.of("Ainda não favoritado");
        var actions = row()
                .r_child(tooltip("Excluir registro (demonstração)", ButtonsPack.IconButton(Feather.TRASH_2, "Excluir",
                        () -> feedback.set("Excluir acionado. Este exemplo não remove dados."))))
                .r_child(tooltip("Editar registro", ButtonsPack.IconButton(Feather.EDIT_2, "Editar",
                        () -> feedback.set("Editar acionado."))))
                .r_child(tooltip("Alternar favorito", ButtonsPack.IconButton(Feather.HEART, "Favoritar", () -> {
                    favorite.set(!favorite.get());
                    favoriteLabel.set(favorite.get() ? "Favoritado!" : "Ainda não favoritado");
                })))
                .r_child(new Text(favoriteLabel));
        var delayExample = tooltip("Dica com 800 ms de espera", ButtonsPack.OutlinedButton("Passe o mouse aqui",
                new ButtonsPack.Props().onClick(() -> feedback.set("Tooltip com atraso personalizado."))))
                .enterDelay(800).leaveDelay(200).showDuration(5000);
        return new Column(new ColumnProps().spacingOf(20))
                .c_child(example("Ações com ícones", "Passe o mouse ou use Tab para ver as dicas. Enter e Espaço ativam as ações.", actions))
                .c_child(example("Atraso personalizado", "A espera do hover é configurável; o foco por teclado mostra a dica imediatamente.", delayExample))
                .c_child(example("Tooltip em um componente", "A dica pode acompanhar uma composição inteira.",
                        tooltip("Componentes podem conter outros componentes.", row()
                                .r_child(Feather.INFO.create(24, "#2563eb"))
                                .r_child(label("Informação adicional", 14, "#334155")))));
    }

    private Tooltip tooltip(String title, Component child) {
        var tooltip = new Tooltip(title, child).enterDelay(250);
        tooltips.add(tooltip);
        return tooltip;
    }

    private Component icons() {
        var search = new Input(iconQuery, new InputProps().placeHolder("Buscar: search, arrow, heart…").height(38).fillWidth())
                .onChange(value -> {
                    filterIcons(value);
                    return OnChangeResult.of(value, value);
                });
        filterIcons("");
        return new Column(new ColumnProps().spacingOf(20))
                .c_child(description("Explore o catálogo Feather. Clique em um ícone para ver a constante na barra de status."))
                .c_child(search)
                .c_child(new Text(iconCount, new TextProps().fontSize(13).textColor("#64748b")))
                .c_child(iconResults);
    }

    List<Feather> filterIcons(String query) {
        String normalized = query.strip().toLowerCase(Locale.ROOT).replace('_', '-');
        List<Feather> matches = Arrays.stream(Feather.values())
                .filter(icon -> icon.getDescription().contains(normalized)).toList();
        iconCount.set(matches.size() + " de " + Feather.values().length + " ícones");
        var grid = new Column(new ColumnProps().spacingOf(12));
        for (int i = 0; i < matches.size(); i += 5) {
            var line = row();
            for (Feather icon : matches.subList(i, Math.min(i + 5, matches.size()))) {
                var tile = new Column(new ColumnProps().width(128).centerHorizontally().spacingOf(8))
                        .c_child(ButtonsPack.IconButton((size, color) -> icon.create(28, color), icon.name(),
                                () -> feedback.set("Selecionado: Feather." + icon.name())))
                        .c_child(label(icon.getDescription().substring(4), 10, "#64748b"));
                line.r_child(new Card(tile, new CardProps().width(140).paddingAll(10)));
            }
            grid.c_child(line);
        }
        if (matches.isEmpty()) grid.c_child(description("Nenhum ícone encontrado. Experimente outro nome."));
        iconResults.show(grid);
        return matches;
    }

    private Component forms() {
        var name = State.of("");
        var search = State.of("");
        var notes = State.of("");
        var selected = State.of("Pequeno");
        return new Column(new ColumnProps().spacingOf(20))
                .c_child(example("Campos de texto", "Digite para experimentar o estado dos campos.",
                        new Column(new ColumnProps().spacingOf(16))
                                .c_child(Pack.InputColumn("Nome", name, "Digite seu nome", 420))
                                .c_child(Pack.TextWithValue("Valor atual: ", name))
                                .c_child(Pack.InputColumn("Campo desabilitado", State.of("Somente visualização"), "", true, 420))))
                .c_child(example("Busca", "Campo com ícone Feather integrado.", Pack.searchInput(search, "Buscar componentes", true, null)))
                .c_child(example("Seleção e data", "Escolha uma opção ou experimente o calendário.",
                        new Column(new ColumnProps().spacingOf(16))
                                .c_child(Pack.SelectColumn("Tamanho", List.of("Pequeno", "Médio", "Grande"), selected, value -> value))
                                .c_child(Pack.DatePickerColumn(State.of(LocalDate.now()), "Data"))))
                .c_child(example("Área de texto", "Notas com espaço para várias linhas.",
                        Pack.TextAreaColumn("Observações", notes, "Escreva uma observação", 100)));
    }

    private Component brazil() {
        return example("Formatação brasileira", "Digite valores para experimentar as máscaras. Os campos usam dados de demonstração.",
                new Column(new ColumnProps().spacingOf(16))
                        .c_child(disgust.io.br.Pack.InputColumnCep("CEP", State.of("01310100")))
                        .c_child(disgust.io.br.Pack.InputColumnCpf("CPF", State.of("")))
                        .c_child(disgust.io.br.Pack.InputColumnCnpjAlfanumerico("CNPJ", State.of("")))
                        .c_child(disgust.io.br.Pack.InputColumnCurrency("Valor", State.of("123456"), false))
                        .c_child(disgust.io.br.Pack.InputColumnCurrency("Valor desabilitado", State.of("123456"), true)));
    }

    private Component composition() {
        return new Column(new ColumnProps().spacingOf(20))
                .c_child(example("Hierarquia de texto", "Título, subtítulo e informações organizadas por rótulo.",
                        new Column(new ColumnProps().spacingOf(12))
                                .c_child(Pack.FormTitle("Título do formulário"))
                                .c_child(Pack.FormSubtitle("Uma descrição curta para orientar o usuário"))
                                .c_child(Pack.TextWithDetails("Status: ", "Disponível"))
                                .c_child(Pack.TextColumn("Categoria", "Componentes de interface"))))
                .c_child(example("Composição em cards", "Rows, Columns, ícones e botões podem formar componentes maiores.",
                        row().r_child(metric("Componentes", "6 categorias", Feather.LAYERS))
                                .r_child(metric("Ícones", "286 Feather", Feather.GRID))))
                .c_child(example("Ação principal", "Um exemplo de ação com feedback local.",
                        Pack.PrimaryActionButton("Experimentar ação", () -> feedback.set("Ação principal acionada."))));
    }

    private Component metric(String title, String value, Feather icon) {
        return new Card(new Column(new ColumnProps().spacingOf(12))
                .c_child(icon.create(32, "#2563eb"))
                .c_child(label(title, 13, "#64748b"))
                .c_child(label(value, 22, "#0f172a")), new CardProps().width(300).paddingAll(20));
    }

    private Component example(String title, String subtitle, Component demo) {
        return new Card(new Column(new ColumnProps().spacingOf(14))
                .c_child(label(title, 18, "#0f172a"))
                .c_child(description(subtitle))
                .c_child(demo), new CardProps().fillWidth().paddingAll(20));
    }

    private static Row row() { return new Row(new RowProps().spacingOf(12).centerVertically()); }
    private static Text label(String text, int size, String color) {
        return new Text(text, new TextProps().fontSize(size).textColor(color));
    }
    private static Component description(String text) { return new TextFlow(label(text, 13, "#64748b")); }
    private static String name(ButtonVariant variant) {
        return switch (variant) {
            case PRIMARY -> "Primário";
            case SECONDARY -> "Secundário";
            case SUCCESS -> "Sucesso";
            case WARNING -> "Atenção";
            case DANGER -> "Perigo";
            default -> variant.name();
        };
    }

    @Override
    public void onDestroy() {
        tooltips.forEach(Tooltip::close);
        tooltips.clear();
    }
}
