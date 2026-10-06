package disgust.icons;

import megalodonte.base.components.Component;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/** Reusable icon definition. Each call must create a fresh component for its owner. */
@FunctionalInterface
public interface Icon {
    Component create(double size, Paint color);

    default Component create() { return create(24, Color.BLACK); }

    default Component create(double size, String color) {
        return create(size, Color.web(color));
    }
}
