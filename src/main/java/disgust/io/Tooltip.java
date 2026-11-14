package disgust.io;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.util.Duration;
import megalodonte.base.components.Component;
import java.util.Objects;

/** Tooltip composition that preserves the child's node, layout and event handlers. */
public final class Tooltip extends Component implements AutoCloseable {
    private static final Object OWNER_KEY = new Object();
    private final javafx.scene.control.Tooltip popup = new javafx.scene.control.Tooltip();
    private final ChangeListener<Boolean> focusListener = (observable, oldValue, focused) -> {
        if (focused) showForKeyboard(); else popup.hide();
    };
    private final ChangeListener<Scene> sceneListener = (observable, oldScene, newScene) -> popup.hide();
    private boolean installed;
    private boolean closed;
    
    private static final double GAP = 6;

    public Tooltip(String title, Component child) {
        super(Objects.requireNonNull(child, "child").getJavaFxNode());
        Objects.requireNonNull(title, "title");
        if (node.getProperties().get(OWNER_KEY) instanceof Tooltip previous) previous.close();
        node.getProperties().put(OWNER_KEY, this);
        popup.setWrapText(true);
        popup.setMaxWidth(320);
        popup.getStyleClass().add("disgust-tooltip");
        
        javafx.application.Platform.runLater(()->relocate(GAP));
        
        node.focusedProperty().addListener(focusListener);
        node.sceneProperty().addListener(sceneListener);
        title(title);
    }

    public Tooltip title(String title) {
        ensureOpen();
        popup.setText(Objects.requireNonNull(title, "title"));
        popup.hide();
        boolean enabled = !title.isBlank();
        if (enabled && !installed) {
            javafx.scene.control.Tooltip.install(node, popup);
            installed = true;
        } else if (!enabled && installed) {
            javafx.scene.control.Tooltip.uninstall(node, popup);
            installed = false;
        }
        return this;
    }

    public Tooltip enterDelay(double milliseconds) {
        ensureOpen();
        popup.setShowDelay(duration(milliseconds));
        return this;
    }

    public Tooltip leaveDelay(double milliseconds) {
        ensureOpen();
        popup.setHideDelay(duration(milliseconds));
        return this;
    }

    public Tooltip showDuration(double milliseconds) {
        ensureOpen();
        popup.setShowDuration(duration(milliseconds));
        return this;
    }
    
    public enum Position{TOP, BOTTOM, LEFT, RIGHT}
    private Position position = Position.BOTTOM;
    
    public Tooltip position(Position position) {
        return this.position(position, 6);
    }
    
    public Tooltip position(Position position, double gap) {
        ensureOpen();
    	this.position = Objects.requireNonNull(position, "position");

        if (popup.isShowing()) {
            relocate(gap);
        }
        return this;
    }
    
    private void relocate(double gap) {
        if (closed || !popup.isShowing()) return;

        Bounds bounds = node.localToScreen(node.getBoundsInLocal());
        if (bounds == null) return;

        double width = popup.getWidth();
        double height = popup.getHeight();

        double x;
        double y;

        switch (position) {
            case TOP -> {
                x = bounds.getCenterX() - width / 2;
                y = bounds.getMinY() - height - gap;
            }
            case BOTTOM -> {
                x = bounds.getCenterX() - width / 2;
                y = bounds.getMaxY() + gap;
            }
            case LEFT -> {
                x = bounds.getMinX() - width - gap;
                y = bounds.getCenterY() - height / 2;
            }
            case RIGHT -> {
                x = bounds.getMaxX() + gap;
                y = bounds.getCenterY() - height / 2;
            }
            default -> throw new IllegalStateException(
                "Unexpected position: " + position
            );
        }

        popup.setX(x);
        popup.setY(y);
    }

    /** Native control for CSS, graphics and further JavaFX configuration. */
    public javafx.scene.control.Tooltip getJavaFxTooltip() { return popup; }

    private static Duration duration(double milliseconds) {
        if (!Double.isFinite(milliseconds) || milliseconds < 0) {
            throw new IllegalArgumentException("Duration must be finite and non-negative");
        }
        return Duration.millis(milliseconds);
    }

    private void showForKeyboard() {
        if (closed || !installed || node.isDisabled() || node.getScene() == null) return;

        var window = node.getScene().getWindow();
        Bounds bounds = node.localToScreen(node.getBoundsInLocal());

        if (window != null && window.isShowing() && bounds != null) {
            popup.show(node, bounds.getMinX(), bounds.getMaxY() + GAP);
        }
    }

    private void ensureOpen() {
        if (closed) throw new IllegalStateException("Tooltip has been closed");
    }

    /** Detaches listeners and dismisses the popup. Safe to call repeatedly. */
    @Override
    public void close() {
        if (closed) return;
        closed = true;
        if (installed) javafx.scene.control.Tooltip.uninstall(node, popup);
        installed = false;
        popup.hide();
        node.focusedProperty().removeListener(focusListener);
        node.sceneProperty().removeListener(sceneListener);
        node.getProperties().remove(OWNER_KEY, this);
    }
}
