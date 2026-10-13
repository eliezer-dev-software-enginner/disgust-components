# Material-style components

`disgust.io.Material` provides desktop-native compositions inspired by the
[Material UI component catalog](https://mui.com/material-ui/all-components/).
It is an API for Megalodonte applications, not a port of React or CSS. Configure
`ThemeManager` before constructing components. Every returned visual component
comes from `megalodonte-components`; Disgust supplies consistent composition.

| Catalog area | Disgust API or existing Megalodonte control |
| --- | --- |
| Inputs | `ButtonsPack`, `Material.buttonGroup`, `Material.floatingActionButton`, `Material.switchControl`, `Material.slider`; framework `Input`, `PasswordInput`, `TextAreaInput`, `Checkbox`, `Select`, `DatePicker` |
| Data display | `Material.avatar`, `Material.badge`, `Material.chip`, `Material.divider`, `Material.breadcrumbs`, `Tooltip`; framework `Text`, `SimpleTable`, `PaginatedTable`, `DataList`, `Image` |
| Feedback | `Material.alert`, `Material.dialog`, `Material.snackbar`, `Material.progress`; framework `Modal` |
| Surfaces | `Material.accordion`, `Material.paper`; framework `Card`, `Container` |
| Navigation | `Material.tabs`, `Material.breadcrumbs`; framework `Menu`, `MenuBar`, `Scroll` |
| Layout | framework `Row`, `Column`, `Stack`, `GridFlow`, `Container` |

The new switch, slider, accordion and tabs are implemented in
`megalodonte-components`, where platform controls belong. They bind to
Megalodonte `State` objects; Disgust only exposes them through a themed API.
`Material.snackbar` is controlled by a boolean state and is placed over a screen
component; the caller decides when to dismiss it. `Material.badge` renders a
fixed count and caps display at `99+`.

```java
ThemeManager.setTheme(new DefaultTheme());
var notifications = State.of(false);
var volume = State.of(35.0);

var controls = new Column().children(
    Material.alert(Material.Severity.INFO, "Preferences saved"),
    Material.switchControl("Notifications", notifications),
    Material.slider(0, 100, volume),
    Material.chip("Ready", Material.Severity.SUCCESS)
);
```

Autocomplete, radio groups, rating, transfer lists, data grids, pickers, charts,
tree views, timeline and advanced overlays are not yet part of this API. They
should be added only with concrete application use cases and native control
behavior, rather than copying browser-specific abstractions.
