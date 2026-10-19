package disgust.web.layout;

import java.util.List;
import java.util.Objects;
import megalodonte.base.components.Component;
import megalodonte.components.layout_components.Column;
import megalodonte.components.layout_components.Row;
import megalodonte.components.layout_components.Stack;

/** Explicit layout helpers that return only Megalodonte components. */
public final class Layout {
    private Layout() { }

    public static Column column(List<? extends Component> children) {
        return new Column().items(Objects.requireNonNull(children, "children"));
    }
    public static Row row(List<? extends Component> children) {
        return new Row().items(Objects.requireNonNull(children, "children"));
    }
    public static Stack stack(List<? extends Component> children) {
        return new Stack().children(Objects.requireNonNull(children, "children").toArray(Component[]::new));
    }
}
