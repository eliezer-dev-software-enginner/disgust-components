module disgust.web {
    requires transitive disgust.components;
    requires transitive megalodonte.base;
    requires transitive megalodonte.components;
    requires transitive megalodonte.reactivity;

    exports disgust.web;
    exports disgust.web.inputs;
    exports disgust.web.display;
    exports disgust.web.feedback;
    exports disgust.web.surfaces;
    exports disgust.web.navigation;
    exports disgust.web.layout;
}
