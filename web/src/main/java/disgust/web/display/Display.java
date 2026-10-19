package disgust.web.display;

import disgust.io.Material;
import disgust.web.Severity;
import java.util.List;
import megalodonte.base.components.Component;

/** Small read-only compositions; tables and lists remain native Megalodonte controls. */
public final class Display {
    private Display() { }

    public static Component avatar(String initials) { return Material.avatar(initials); }
    public static Component badge(Component content, int count) { return Material.badge(content, count); }
    public static Component chip(String label, Severity severity) {
        return Material.chip(label, severity.material());
    }
    public static Component divider() { return Material.divider(); }
    public static Component breadcrumbs(List<String> labels) { return Material.breadcrumbs(labels); }
}
