module disgust.components {
    requires transitive megalodonte.base.desktop;
    requires transitive megalodonte.components.desktop;
    requires transitive megalodonte.reactivity.desktop;

    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;

    requires transitive disgust.icons;

    requires pack.utilities;

    exports disgust.io;
    exports disgust.io.br;
}
