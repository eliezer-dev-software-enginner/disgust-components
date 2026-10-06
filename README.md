# Disgust Components

Biblioteca de composição para **Megalodonte**, com Java 25 e JavaFX 25.0.1.
Configure o tema Megalodonte antes de construir os componentes.

## Organização dos ícones

`disgust-icons` é um subprojeto Gradle e um módulo Java (`disgust.icons`) separado,
publicável como `disgust:disgust-icons:1.0.0-beta`. Ele depende de
`megalodonte-base` e JavaFX, sem depender de `disgust-components` ou Ikonli.
Assim, aplicações Megalodonte podem usar os ícones sem importar os packs de UI.
`disgust-components` depende desse módulo e o reexporta via JPMS.

A API pública trabalha com `Component`. `Icon` representa uma fábrica reutilizável
que recebe tamanho/cor e cria um novo `Component` a cada chamada. `Feather`
implementa essa interface. São 286 ícones, com os mesmos nomes e códigos da
[referência Feather do Ikonli](https://kordamp.org/ikonli/cheat-sheet-feather.html).
A fonte vem incorporada no JAR; não há descoberta de providers ou dependência
Ikonli em execução.

Essa separação permite acrescentar coleções e fábricas próprias sem acoplar os
componentes a uma coleção específica. `Node` é detalhe interno da renderização.

## Ícones e botões

```java
import disgust.icons.Feather;
import disgust.icons.Icon;
import disgust.io.ButtonsPack;
import megalodonte.base.components.Component;
import megalodonte.components.Image;
import megalodonte.props.ImageProps;

Component searchIcon = Feather.SEARCH.create(20, "#2563eb");

var saveButton = ButtonsPack.ContainedButton("Salvar",
        new ButtonsPack.Props().iconStart(Feather.SAVE).onClick(this::salvar));

// Qualquer Component pode ser usado como ícone de um botão.
Component logo = new Image("icons/company.png", new ImageProps().size(18));
var companyButton = ButtonsPack.TextButton("Empresa",
        new ButtonsPack.Props().iconStart(logo).onClick(this::abrirEmpresa));

// Para reutilizar a definição em vários botões, crie um Component novo por chamada.
Icon companyIcon = (size, color) ->
        new Image("icons/company.png", new ImageProps().size((int) size));
```

Use `iconStart` ou `iconEnd`. Uma instância de `Component` pertence a um único
botão; uma fábrica `Icon` pode ser compartilhada. Componentes personalizados
preservam sua própria aparência; fábricas podem aplicar o tamanho/cor recebidos.
Feather aplica ambos automaticamente.

## Tooltip e IconButton

```java
import disgust.icons.Feather;
import disgust.io.ButtonsPack;
import disgust.io.Tooltip;

var deleteButton = ButtonsPack.IconButton(Feather.TRASH_2, "Excluir", this::excluir);
var deleteWithTooltip = new Tooltip("Excluir registro", deleteButton)
        .enterDelay(300)
        .leaveDelay(100)
        .showDuration(5000);

// Pode ser passado diretamente para children/c_child/r_child do Megalodonte.
row.r_child(deleteWithTooltip);
```

`Tooltip` é um `Component` que recebe um filho `Component`. Ele conserva o nó,
layout e handlers do filho. Usa o Tooltip nativo do JavaFX para hover e mostra a
dica também no foco por teclado. As durações são em milissegundos; `enterDelay`
se aplica ao hover, e o foco por teclado mostra a dica imediatamente.
Título vazio desativa a dica. `title(...)` atualiza o texto; `close()` remove a
instalação/listeners e oculta a dica. Ao instalar outro Tooltip Disgust no mesmo
filho, o anterior é encerrado. Construa e configure os componentes na thread JavaFX.

O `IconButton` aceita `Icon` ou `Component`, exige um nome acessível e executa a
ação tanto por clique quanto pela ativação de teclado do botão JavaFX. Para
customização do popup, use `getJavaFxTooltip()` e a classe CSS `disgust-tooltip`.

## Migração do Ikonli

- Substitua `Ikon` por `disgust.icons.Icon` nas definições reutilizáveis.
- Substitua constantes de outras coleções por equivalentes Feather, por exemplo
  `AntDesignIconsOutlined.SEARCH` por `Feather.SEARCH`.
- Substitua `FontIcon.of(...)` por `Feather.SEARCH.create(tamanho, cor)`, que retorna
  `Component` para uso direto na composição Megalodonte.
- `Pack.ikon(...)` passa a ser `Pack.icon(...)`, retornando `Component`.
- Os atalhos `ButtonsPack.*WithIconStart/End` e `Pack.MenuItem` recebem `Icon`.
- Remova os `requires org.kordamp.ikonli.*` que sua aplicação não usa mais e use
  `requires disgust.components`, ou `requires disgust.icons` para apenas ícones.

A mudança dos parâmetros é incompatível com chamadas que ainda passam `Ikon`.
O artefato existente de componentes mantém o nome `distust-io-components` para
preservar suas coordenadas de publicação.

## Build e publicação local

### Mostruário `disgust-ui`

Aplicação Megalodonte local para experimentar botões, Tooltip/IconButton, o catálogo
Feather com busca, campos de formulário, máscaras brasileiras e composição.
As ações geram apenas feedback local; não usam banco de dados ou serviços externos.

```powershell
./gradlew.bat :disgust-ui:run
./gradlew.bat :disgust-ui:build
./gradlew.bat :disgust-ui:test
```

O módulo é **opcional**: `build`, `test` e `publishToMavenLocal` normais incluem
somente as bibliotecas. `disgust-ui` só entra nas configurações quando uma tarefa
dele é solicitada explicitamente. Não aplica `maven-publish` e não publica artefatos.
Para importá-lo na IDE, habilite `-PwithUi=true` na sincronização Gradle; essa opção
inclui o módulo na sessão e, portanto, também nas tarefas agregadas dessa sessão.
Os testes do mostruário geram prévias em `disgust-ui/build/reports/previews`.

### Bibliotecas

```powershell
./gradlew.bat test build
./gradlew.bat publishToMavenLocal
```

O segundo comando publica ambos os artefatos. Aplicações usando somente ícones:

```kotlin
implementation("disgust:disgust-icons:1.0.0-beta")
```

Os testes verificam todo o catálogo Feather, a fonte incorporada, criação de
instâncias independentes, integração dos botões, acessibilidade/ação do IconButton
e configuração, atualização, substituição e descarte de Tooltip.

O build atribui o nome automático `pack.utilities` ao JAR legado de utilidades
usando [GradleX Extra Java Module Info](https://github.com/gradlex-org/extra-java-module-info).
Consumidores JPMS precisam dessa mesma configuração enquanto o pacote original
não publicar metadados de módulo válidos.

## Licença

Disgust: MIT. A fonte Feather é MIT, e o mapeamento adaptado do Ikonli é Apache-2.0.
As licenças e atribuições estão incluídas em `META-INF` no JAR `disgust-icons`.

Desenvolvido por **Eliezer**.
