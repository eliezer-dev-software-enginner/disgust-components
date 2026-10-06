package disgust.ui;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import megalodonte.base.components.Component;

/** Internal slot for swapping Component previews without replacing the app scene. */
final class PreviewHost extends Component {
    PreviewHost() {
        super(new VBox());
        VBox.setVgrow(node, Priority.ALWAYS);
    }

    void show(Component component) {
        ((VBox) node).getChildren().setAll(component.getJavaFxNode());
    }
}
