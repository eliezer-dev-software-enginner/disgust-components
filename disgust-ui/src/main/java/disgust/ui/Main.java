package disgust.ui;

import megalodonte.application.MegalodonteApp;
import megalodonte.base.theme.ThemeManager;
import megalodonte.theme.DefaultTheme;

/** Local Megalodonte component showcase; no domain services or persistence. */
public final class Main {
    private Main() {}

    public static void main(String[] args) {
        ThemeManager.setTheme(new DefaultTheme());
        MegalodonteApp.appName("Disgust UI");
        var showcase = new ShowcaseScreen();
        MegalodonteApp.run(args, context -> {
            context.useView(showcase);
            var stage = context.javafxStage();
            stage.setTitle("Disgust UI · Mostruário de componentes");
            stage.setWidth(1180);
            stage.setHeight(820);
            stage.setMinWidth(1040);
            stage.setMinHeight(650);
        }, event -> showcase.onDestroy());
    }
}
