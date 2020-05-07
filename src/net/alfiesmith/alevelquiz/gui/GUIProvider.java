package net.alfiesmith.alevelquiz.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import net.alfiesmith.alevelquiz.ALevelQuiz;

public final class GUIProvider {

    private GUIProvider() {
        // Preventing instantiation
    }

    public static Scene getDefaultScene() {
        return getDefaultScene(getGridPane());
    }

    public static Scene getDefaultScene(GridPane pane) {
        return new Scene(pane, ALevelQuiz.WINDOW_WIDTH, ALevelQuiz.WINDOW_HEIGHT);
    }

    public static GridPane getGridPane() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setVgap(5D);
        pane.setHgap(5D);
        return pane;
    }
}
