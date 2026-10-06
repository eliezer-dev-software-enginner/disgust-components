package disgust.io;

import disgust.icons.Feather;
import disgust.icons.Icon;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import megalodonte.base.components.Component;
import megalodonte.base.theme.ThemeManager;
import megalodonte.props.ButtonVariant;
import megalodonte.theme.DefaultTheme;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class IconsAndTooltipTest {
    @BeforeAll
    static void startJavaFx() throws Exception {
        var started = new CountDownLatch(1);
        Platform.startup(() -> {
            Platform.setImplicitExit(false);
            ThemeManager.setTheme(new DefaultTheme());
            started.countDown();
        });
        assertTrue(started.await(15, TimeUnit.SECONDS));
    }

    private void onFx(Runnable action) throws Exception {
        var task = new FutureTask<Void>(action, null);
        Platform.runLater(task);
        task.get(15, TimeUnit.SECONDS);
    }

    @Test
    void entireFeatherCatalogLoadsTheBundledFontAndCreatesFreshComponents() throws Exception {
        onFx(() -> {
            assertEquals(286, Feather.values().length);
            var codes = new HashSet<Integer>();
            for (Feather icon : Feather.values()) {
                assertTrue(codes.add(icon.getCode()));
                assertSame(icon, Feather.findByDescription(icon.getDescription()));
                Component first = icon.create(18, Color.BLUE);
                Component second = icon.create(18, Color.BLUE);
                assertNotSame(first, second);
                assertNotSame(first.getJavaFxNode(), second.getJavaFxNode());
                Text glyph = (Text) first.getJavaFxNode();
                assertEquals("FeatherIcons", glyph.getFont().getFamily());
                assertEquals(18, glyph.getFont().getSize());
                assertEquals(Color.BLUE, glyph.getFill());
                assertEquals(icon.getCode(), glyph.getText().codePointAt(0));
                assertTrue(glyph.getLayoutBounds().getWidth() > 0);
            }
            assertThrows(IllegalArgumentException.class, () -> Feather.SEARCH.create(0, Color.BLACK));
            assertThrows(IllegalArgumentException.class, () -> Feather.SEARCH.create(Double.NaN, Color.BLACK));
            assertThrows(IllegalArgumentException.class, () -> Feather.findByDescription("fth-unknown"));
        });
    }

    @Test
    void buttonsAcceptFeatherFactoriesAndCustomComponents() throws Exception {
        onFx(() -> {
            var end = ButtonsPack.OutlinedButton("Buscar", new ButtonsPack.Props()
                    .iconEnd(Feather.SEARCH).onClick(() -> {}));
            var endNode = (Button) end.getJavaFxNode();
            assertEquals(ContentDisplay.RIGHT, endNode.getContentDisplay());
            assertEquals(Color.web("#2563eb"), ((Text) endNode.getGraphic()).getFill());

            Component custom = Feather.HEART.create();
            var start = ButtonsPack.ContainedButton("Favorito", new ButtonsPack.Props()
                    .iconStart(custom).onClick(() -> {}));
            assertSame(custom.getJavaFxNode(), ((Button) start.getJavaFxNode()).getGraphic());

            AtomicInteger calls = new AtomicInteger();
            Icon factory = (size, color) -> {
                calls.incrementAndGet();
                return Feather.PLUS.create(size, color);
            };
            ButtonsPack.TextButtonWithIconStart("Adicionar", ButtonVariant.PRIMARY, factory, () -> {});
            ButtonsPack.TextButtonWithIconStart("Adicionar", ButtonVariant.PRIMARY, factory, () -> {});
            assertEquals(2, calls.get());
        });
    }

    @Test
    void iconButtonHasAnAccessibleName() throws Exception {
        onFx(() -> {
            var calls = new AtomicInteger();
            var button = (Button) ButtonsPack.IconButton(Feather.TRASH_2, "Excluir", calls::incrementAndGet).getJavaFxNode();
            assertEquals("Excluir", button.getAccessibleText());
            assertEquals(ContentDisplay.GRAPHIC_ONLY, button.getContentDisplay());
            assertNotNull(button.getGraphic());
            button.fire();
            assertEquals(1, calls.get());
            assertThrows(IllegalArgumentException.class,
                    () -> ButtonsPack.IconButton(Feather.TRASH_2, "", () -> {}));
        });
    }

    @Test
    void tooltipPreservesTheChildAndItsEventHandlers() throws Exception {
        onFx(() -> {
            var child = new Button("Excluir");
            var clicks = new AtomicInteger();
            child.setOnAction(event -> clicks.incrementAndGet());
            var handler = child.getOnAction();
            var component = Component.CreateFromJavaFxNode(child);
            try (var tooltip = new Tooltip("Excluir registro", component)
                    .enterDelay(150).leaveDelay(50).showDuration(3000)) {
                assertSame(child, tooltip.getJavaFxNode());
                assertSame(handler, child.getOnAction());
                child.fire();
                assertEquals(1, clicks.get());
                assertEquals("Excluir registro", tooltip.getJavaFxTooltip().getText());
                assertEquals(150, tooltip.getJavaFxTooltip().getShowDelay().toMillis());
                assertEquals(50, tooltip.getJavaFxTooltip().getHideDelay().toMillis());
                assertEquals(3000, tooltip.getJavaFxTooltip().getShowDuration().toMillis());
                assertThrows(IllegalArgumentException.class, () -> tooltip.enterDelay(-1));
                assertThrows(IllegalArgumentException.class, () -> tooltip.leaveDelay(Double.NaN));
                tooltip.title("");
                assertFalse(tooltip.getJavaFxTooltip().isShowing());
                tooltip.title("Novo título");
                assertEquals("Novo título", tooltip.getJavaFxTooltip().getText());
            }
            assertSame(handler, child.getOnAction());
        });
    }

    @Test
    void replacingOrClosingATooltipCleansUpItsOwnership() throws Exception {
        onFx(() -> {
            Component child = Component.CreateFromJavaFxNode(new Button("Ajuda"));
            var first = new Tooltip("Primeiro", child);
            var second = new Tooltip("Segundo", child);
            assertThrows(IllegalStateException.class, () -> first.title("Antigo"));
            first.close();
            second.title("Atual");
            second.close();
            second.close();
            assertThrows(IllegalStateException.class, () -> second.title("Fechado"));
            try (var third = new Tooltip("Terceiro", child)) {
                assertSame(child.getJavaFxNode(), third.getJavaFxNode());
            }
        });
    }
}
