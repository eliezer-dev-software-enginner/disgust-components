# Disgust Components

Composition library for **Megalodonte**, built with Java 25 and JavaFX 25.0.1.
Configure the Megalodonte theme before constructing components.

The [Material catalog](docs/MATERIAL_COMPONENTS.md) documents native
compositions inspired by the main MUI controls. They use Megalodonte components
and never load a browser or WebView.

The [`web`](web/) directory contains only static documentation for these
components. It describes the Java API and illustrates what users see in native
desktop applications. It is not a Gradle module, does not publish a
`disgust-web` artifact, and does not provide browser components. Applications
consume the implementations from `disgust-components`, `disgust-icons`, and
Megalodonte.

Visit the [public Disgust component showcase](https://disgust-components.vercel.app/#Button)
to view previews and copy native Java API examples.

## Icon architecture

`disgust-icons` is a separate Gradle subproject and Java module
(`disgust.icons`), published as `disgust:disgust-icons:1.0.0-beta`. It depends
on `megalodonte-base` and JavaFX without depending on `disgust-components` or
Ikonli. Megalodonte applications can therefore use the icons without importing
the UI packs. `disgust-components` depends on this module and reexports it
through JPMS.

The public API works with `Component`. `Icon` is a reusable factory that accepts
a size and color and creates a new `Component` for each call. `Feather`
implements this interface. It provides 286 icons with the same names and codes
as the [Ikonli Feather reference](https://kordamp.org/ikonli/cheat-sheet-feather.html).
The font is embedded in the JAR; runtime does not require provider discovery or
an Ikonli dependency.

This separation allows additional icon collections and custom factories without
coupling components to a specific collection. `Node` remains an internal
rendering detail.

## Icons and buttons

```java
import disgust.icons.Feather;
import disgust.icons.Icon;
import disgust.io.ButtonsPack;
import megalodonte.base.components.Component;
import megalodonte.components.Image;
import megalodonte.props.ImageProps;

Component searchIcon = Feather.SEARCH.create(20, "#2563eb");

var saveButton = ButtonsPack.ContainedButton("Save",
        new ButtonsPack.Props().iconStart(Feather.SAVE).onClick(this::save));

// Any Component can be used as a button icon.
Component logo = new Image("icons/company.png", new ImageProps().size(18));
var companyButton = ButtonsPack.TextButton("Company",
        new ButtonsPack.Props().iconStart(logo).onClick(this::openCompany));

// Create a new Component per call when the definition is reused by several buttons.
Icon companyIcon = (size, color) ->
        new Image("icons/company.png", new ImageProps().size((int) size));
```

Use `iconStart` or `iconEnd`. A `Component` instance belongs to one button; an
`Icon` factory can be shared. Custom components preserve their own appearance,
while factories can apply the received size and color. Feather applies both
automatically.

## Native inputs

`InputsPack` groups the input controls used by Disgust applications without
creating a separate visual hierarchy. Its methods return native Megalodonte
components bound to `State` and rendered by JavaFX.

```java
var name = State.of("");
var category = State.of("Finished product");
var notifications = State.of(true);

var nameInput = InputsPack.text(name,
        new InputProps().placeHolder("Enter your name").fillWidth());
var categorySelect = InputsPack.select(
        List.of("Raw material", "Finished product", "Service"), category);
var notificationsInput = InputsPack.checkbox(
        "Receive notifications", notifications);
```

The pack also provides `password`, `textArea`, `date`, `switchControl`,
`radioGroup`, `option`, and `slider`. The [visual catalog](web/) shows every
control and includes copyable Java API examples.

## Tooltip and IconButton

```java
import disgust.icons.Feather;
import disgust.io.ButtonsPack;
import disgust.io.Tooltip;

var deleteButton = ButtonsPack.IconButton(Feather.TRASH_2, "Delete", this::delete);
var deleteWithTooltip = new Tooltip("Delete record", deleteButton)
        .enterDelay(300)
        .leaveDelay(100)
        .showDuration(5000);

// It can be passed directly to Megalodonte children/c_child/r_child methods.
row.r_child(deleteWithTooltip);
```

`Tooltip` is a `Component` that wraps a child `Component`. It preserves the
child's node, layout, and handlers. It uses the native JavaFX Tooltip on hover
and also displays it when keyboard focus reaches the child. Durations are in
milliseconds: `enterDelay` applies to hover, while keyboard focus displays the
tooltip immediately.

An empty title disables the tooltip. `title(...)` updates its text; `close()`
removes its installation and listeners and hides it. Installing another Disgust
Tooltip on the same child closes the previous instance. Construct and configure
components on the JavaFX Application Thread.

`IconButton` accepts an `Icon` or `Component`, requires an accessible name, and
runs its action through mouse or keyboard activation of the JavaFX button. Use
`getJavaFxTooltip()` and the `disgust-tooltip` CSS class for popup customization.

## Migrating from Ikonli

- Replace `Ikon` with `disgust.icons.Icon` in reusable definitions.
- Replace constants from other collections with Feather equivalents, such as
  `AntDesignIconsOutlined.SEARCH` with `Feather.SEARCH`.
- Replace `FontIcon.of(...)` with `Feather.SEARCH.create(size, color)`, which
  returns a `Component` for direct use in Megalodonte compositions.
- Replace `Pack.ikon(...)` with `Pack.icon(...)`, which returns a `Component`.
- `ButtonsPack.*WithIconStart/End` shortcuts and `Pack.MenuItem` accept `Icon`.
- Remove unused `requires org.kordamp.ikonli.*` declarations and use
  `requires disgust.components`, or `requires disgust.icons` when only icons
  are needed.

The parameter change is incompatible with calls that still pass `Ikon`. The
existing components artifact retains the `distust-io-components` name to
preserve its published coordinates.

## Build and local publication

The build uses the Gradle Wrapper (Gradle 9.2.0) and requires a JDK 25 toolchain.
The `megalodonte:*:1.0.0-beta` and
`pack.utilities:pack-utilities:1.0.0` dependencies must be available from the
configured repositories (`mavenLocal()` or Maven Central). The commands below
cannot complete when these artifacts are unavailable. Their source code is not
included in this repository.

### `disgust-ui` showcase

`disgust-ui` is a local Megalodonte application for trying buttons,
Tooltip/IconButton, the searchable Feather catalog, form fields, Brazilian
masks, and compositions. Its actions only produce local feedback; they do not
use a database or external services.

```powershell
./gradlew.bat :disgust-ui:run
./gradlew.bat :disgust-ui:build
./gradlew.bat :disgust-ui:test
```

On Linux, replace `./gradlew.bat` with `./gradlew` in Gradle commands.

The module is **optional**: regular `build`, `test`, and `publishToMavenLocal`
tasks include only the libraries. `disgust-ui` is added to the build only when
one of its tasks is explicitly requested. It does not apply `maven-publish` or
publish artifacts. Enable `-PwithUi=true` during Gradle synchronization to
import it into an IDE. This option includes the module in that session and
therefore in its aggregate tasks as well. Showcase tests generate previews in
`disgust-ui/build/reports/previews`.

### Libraries

```powershell
./gradlew.bat test build
./gradlew.bat publishToMavenLocal
```

The second command publishes both artifacts. Applications that only need icons
can use:

```kotlin
implementation("disgust:disgust-icons:1.0.0-beta")
```

Tests cover the complete Feather catalog, embedded font, independent instance
creation, button integration, IconButton accessibility and activation, and
Tooltip configuration, updates, replacement, and disposal.

The `disgust.components` module declares `requires pack.utilities` in its
`module-info.java`. With JPMS, the `pack-utilities` JAR must expose that module
name; this repository does not configure a metadata transformation for it.

## License

Disgust is described as MIT, but this repository does not contain its own
license file; the project terms require confirmation from the maintainer. The
Feather font is MIT, and the mapping adapted from Ikonli is Apache-2.0. Licenses
and attribution for these resources are included under `META-INF` in the
`disgust-icons` JAR.

Developed by **Eliezer**.
