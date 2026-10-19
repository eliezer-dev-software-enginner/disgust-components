package disgust.web.navigation;

import disgust.io.Material;
import java.util.List;
import megalodonte.base.state.State;
import megalodonte.base.components.Component;
import megalodonte.components.Tabs;
import megalodonte.components.layout_components.Row;

/** Navigation compositions; clients import this facade rather than colliding Tabs names. */
public final class Navigation {
    private Navigation() { }

    public static Tabs.Page page(String title, Component content) {
        return new Tabs.Page(title, content);
    }

    public static Tabs tabs(List<Tabs.Page> pages, State<Integer> selected) {
        return Material.tabs(pages, selected);
    }
    public static Row breadcrumbs(List<String> labels) { return Material.breadcrumbs(labels); }
}
