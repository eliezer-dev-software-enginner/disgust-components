module disgust.components {
    requires transitive megalodonte.base;
    requires transitive megalodonte.components;
    requires transitive megalodonte.reactivity;

    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;

    requires transitive disgust.icons;

    requires pack.utilities;

    exports disgust.io;
    exports disgust.io.br;
}
