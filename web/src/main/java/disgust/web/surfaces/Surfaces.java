package disgust.web.surfaces;

import disgust.io.Material;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;
import megalodonte.components.Accordion;
import megalodonte.components.Card;

/** Native surface compositions using the configured Megalodonte theme. */
public final class Surfaces {
    private Surfaces() { }

    public static Card paper(Component content) { return Material.paper(content); }
    public static Accordion accordion(String title, Component content, State<Boolean> expanded) {
        return Material.accordion(title, content, expanded);
    }
}
