# Disgust Web component API

`web` is the Gradle subproject `:web` and publishes `disgust:disgust-web`. It
contains native Megalodonte compositions inspired by the [Material UI component
catalog](https://mui.com/material-ui/all-components/). The existing `index.html`,
`index.js`, and `jsons/` are a static documentation viewer, not a runtime
WebView. A client application uses the Java library and Megalodonte controls.

The API is split by purpose: `disgust.web.inputs.Inputs`,
`disgust.web.display.Display`, `disgust.web.feedback.Feedback`,
`disgust.web.surfaces.Surfaces`, `disgust.web.navigation.Navigation`, and
`disgust.web.layout.Layout`. Distinct facade names let clients import them
alongside `megalodonte.components.Button`, `Card`, `Tabs`, and JavaFX controls
without writing fully qualified class names. These methods return Megalodonte
components; they do not introduce a second component hierarchy.

```java
import disgust.web.Severity;
import disgust.web.display.Display;
import disgust.web.feedback.Feedback;
import disgust.web.inputs.Inputs;
import disgust.web.layout.Layout;
import megalodonte.base.state.State;

var name = State.of("");
var screen = Layout.column(java.util.List.of(
        Inputs.textField(name),
        Inputs.button("Save", () -> save(name.get())),
        Display.chip("Ready", Severity.SUCCESS),
        Feedback.alert(Severity.INFO, "Changes are local")));
```

Configure a Megalodonte theme before constructing visual components. Build with
`./gradlew :web:build`. Publishing is available with
`./gradlew :web:publishToMavenLocal`.

## Component coverage

The table tracks the [MUI catalog](https://mui.com/material-ui/all-components/)
by behavior, not React prop or CSS compatibility. **Available** means a named
entry point exists in `:web`; **next** means implementation is planned. The
underlying native control must live in `megalodonte-components` if absent, while
its Material-style composition and public API belong here.

| Area | Available in `:web` | Next |
| --- | --- | --- |
| Inputs | Button, Button Group, Checkbox, Floating Action Button, Radio Group, Select, Slider, Switch, Text Field, Textarea | Autocomplete, Number Field, Rating, Transfer List, Toggle Button |
| Data display | Avatar, Badge, Chip, Divider, Breadcrumbs | List, Table, Tooltip, Typography, Image List |
| Feedback | Alert, Dialog, Progress, Snackbar | Backdrop, Skeleton |
| Surfaces | Accordion, Paper | App Bar, Card |
| Navigation | Tabs, Breadcrumbs | Bottom Navigation, Drawer, Link, Menu, Menubar, Pagination, Speed Dial, Stepper |
| Layout | Row, Column, Stack | Box, Container, Grid, Image List |
| Utilities and advanced | — | Modal, Popover, Popper, Portal, Click-Away Listener, Transitions, Date/Time Pickers, Data Grid, Tree View |

Icons are supplied by `disgust-icons`. Browser-specific APIs such as SSR and CSS
Baseline do not map to a desktop runtime and are excluded. MUI X charts and
other paid/advanced APIs are separate from this initial Material-style scope.

### Implementation order

1. Finish common inputs, including disabled/error states and keyboard behavior;
   add any missing native controls to Megalodonte Components first.
2. Add list/table, tooltip and typography APIs with accessible semantics.
3. Add navigation and overlay controls, ensuring focus handling and dismissal.
4. Expand the showcase and JSON documentation after each API is functional.

Each component should preserve state binding, accessible text, theme colors,
and keyboard interaction. Avoid exposing JavaFX nodes in the public API merely
to mimic a browser prop.
