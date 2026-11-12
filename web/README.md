# Disgust visual catalog

This directory contains the static documentation for the native Java components
provided by Disgust. It describes the available API and approximates what users
see when those components are rendered in Megalodonte desktop applications.

This content **is not a web component library**:

- it does not provide browser, React, or JavaScript components;
- it is not a `:web` Gradle module and does not publish a `disgust-web` artifact;
- `index.html`, `index.js`, and `style.css` only implement the documentation viewer;
- files under `jsons/` document the real Java classes;
- HTML previews are illustrations and do not define the final behavior or
  appearance of native controls.

The source of truth for each component is its Java implementation under
`src/main` or, when the control belongs to the framework,
`megalodonte-components`. The catalog must use the names, types, constraints,
and examples from those APIs rather than creating a second component hierarchy.

## Local preview

The catalog loads JSON files with `fetch`, so serve it through a local HTTP
server:

```bash
python3 -m http.server 8000 --directory web
```

Then open `http://localhost:8000`. This server only displays the documentation;
it is not involved when Java applications run.

## Documenting a component

1. Implement or change the native Java component first.
2. Update its JSON file with the real package, properties, and examples.
3. Use the preview only to communicate the appearance observed in the application.
4. State any differences that depend on the theme, JavaFX, or operating system.
5. Do not add Java facades under a `disgust.web` package.

## Brand assets

`favicon-source.png` is the high-resolution source of the simplified mark.
`favicon.png` is the 64 px browser asset, while `favicon.ico` contains 16, 32,
48, and 64 px variants. Keep these files aligned when the brand changes.
