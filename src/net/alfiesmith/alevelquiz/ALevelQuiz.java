package net.alfiesmith.alevelquiz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import net.alfiesmith.alevelquiz.gui.GUIProvider;
import net.alfiesmith.alevelquiz.quiz.QuizManager;

// Main class
public class ALevelQuiz extends Application {

	// For the lack of a better place, I will dump all my constants here
	// It will automatically create the SQL table and some questions if they do not exist
	public final static int WINDOW_WIDTH = 310;
	public final static int WINDOW_HEIGHT = 200;

	public final static String CSV_PATH = System.getProperty("user.dir") + "\\data.csv";
	public final static String SQL_ADDRESS = "localhost";
	public final static String SQL_PORT = "3306";
	public final static String SQL_USER = "root";
	public final static String SQL_PASS = "root";
	public final static String SQL_DATABASE_NAME = "a_level_quiz";
	public final static String SQL_TABLE_NAME = "questions";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// Setting up the pane and scene
		GridPane pane = GUIProvider.getGridPane();
		Scene scene = GUIProvider.getDefaultScene(pane);

		// Creating fields on the scene
		TextField firstName = new TextField();
		firstName.setPromptText("Enter your first name...");
		firstName.setPrefColumnCount(10);
		GridPane.setConstraints(firstName, 0, 0);

		TextField secondName = new TextField();
		secondName.setPromptText("Enter your surname...");
		secondName.setPrefColumnCount(15);
		GridPane.setConstraints(secondName, 0, 1);

		TextField username = new TextField();
		username.setPromptText("Enter your username...");
		username.setPrefColumnCount(20);
		GridPane.setConstraints(username, 0, 2);

		Label warning = new Label();
		GridPane.setConstraints(warning,0, 3);
		GridPane.setRowSpan(warning, 2);

		Button submit = new Button("Submit");
		submit.setOnAction(event -> {
			if (firstName.getText().isEmpty() || secondName.getText().isEmpty() || username.getText().isEmpty()) {
				warning.setText("You must enter a first, last and user name");
			} else {
				// Starts quiz. All program will be handled in there now, so no need to store an instance
				new QuizManager(primaryStage, firstName.getText(), secondName.getText(), username.getText()).start();
			}
		});
		GridPane.setConstraints(submit,1, 0);

		Button clear = new Button("Clear");
		clear.setOnAction(event -> {
			firstName.setText("");
			secondName.setText("");
			username.setText("");
			warning.setText(null);
		});
		GridPane.setConstraints(clear, 1, 1);

		pane.getChildren().addAll(firstName, secondName, username, warning, submit, clear);

		// Setting the main scene and showing the stage
		primaryStage.setTitle("A Level Quiz");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
