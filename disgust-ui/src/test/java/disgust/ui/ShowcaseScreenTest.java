package disgust.ui;

import disgust.icons.Feather;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import megalodonte.base.theme.ThemeManager;
import megalodonte.theme.DefaultTheme;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ShowcaseScreenTest {
    @BeforeAll
    static void startToolkit() throws Exception {
        var ready = new CountDownLatch(1);
        Platform.startup(() -> {
            Platform.setImplicitExit(false);
            ThemeManager.setTheme(new DefaultTheme());
            ready.countDown();
        });
        assertTrue(ready.await(15, TimeUnit.SECONDS));
    }

    private void onFx(Runnable action) throws Exception {
        var task = new FutureTask<Void>(action, null);
        Platform.runLater(task);
        task.get(30, TimeUnit.SECONDS);
    }

    @Test
    void allCategoriesRenderAndPreserveTheSceneRoot() throws Exception {
        Path previews = Path.of("build", "reports", "previews");
        Files.createDirectories(previews);
        onFx(() -> {
            var showcase = new ShowcaseScreen();
            var component = showcase.render();
            assertSame(component, showcase.render());
            Parent root = (Parent) component.getJavaFxNode();
            var scene = new Scene(root, 1180, 820);
            ((Region) root).resize(1180, 820);
            try {
                for (ShowcaseScreen.Section section : ShowcaseScreen.Section.values()) {
                    showcase.select(section);
                    root.applyCss();
                    root.layout();
                    assertSame(root, scene.getRoot());
                    assertNotNull(root.lookup("#nav-" + section.name().toLowerCase(java.util.Locale.ROOT)));
                    WritableImage snapshot = root.snapshot(null, null);
                    assertEquals(1180, snapshot.getWidth());
                    assertEquals(820, snapshot.getHeight());
                    write(snapshot, previews.resolve(section.name().toLowerCase(java.util.Locale.ROOT) + ".png"));
                }
            } finally {
                showcase.onDestroy();
            }
        });
    }

    @Test
    void iconSearchHandlesNamesCaseAndEmptyResults() throws Exception {
        onFx(() -> {
            var showcase = new ShowcaseScreen();
            showcase.render();
            showcase.select(ShowcaseScreen.Section.ICONS);
            assertEquals(286, showcase.filterIcons("").size());
            assertTrue(showcase.filterIcons("  ARROW_UP  ").contains(Feather.ARROW_UP));
            assertEquals(java.util.List.of(Feather.SEARCH), showcase.filterIcons("search"));
            assertTrue(showcase.filterIcons("does-not-exist").isEmpty());
            showcase.onDestroy();
        });
    }

    private static void write(WritableImage image, Path path) {
        var output = new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var pixels = image.getPixelReader();
        for (int y = 0; y < output.getHeight(); y++) {
            for (int x = 0; x < output.getWidth(); x++) output.setRGB(x, y, pixels.getArgb(x, y));
        }
        try { ImageIO.write(output, "png", path.toFile()); }
        catch (java.io.IOException e) { throw new java.io.UncheckedIOException(e); }
    }
}
