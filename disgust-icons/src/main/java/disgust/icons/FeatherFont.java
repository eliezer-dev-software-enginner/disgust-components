package disgust.icons;

import javafx.scene.text.Font;
import java.io.IOException;
import java.io.InputStream;

final class FeatherFont {
    private static final Font FONT = load();

    private static Font load() {
        try (InputStream stream = FeatherFont.class.getResourceAsStream("feather.ttf")) {
            if (stream == null) throw new IllegalStateException("Missing bundled Feather font");
            Font font = Font.loadFont(stream, 24);
            if (font == null) throw new IllegalStateException("Unable to load bundled Feather font");
            return font;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read bundled Feather font", e);
        }
    }

    static Font at(double size) { return Font.font(FONT.getFamily(), size); }
}
