module disgust.components {
    requires transitive megalodonte.base;
    requires transitive megalodonte.components;
    requires transitive megalodonte.reactivity;

    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.antdesignicons;
    requires org.kordamp.ikonli.entypo;

    requires pack.utilities;

    exports disgust.io;
    exports disgust.io.br;
}