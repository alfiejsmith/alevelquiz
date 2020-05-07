package net.alfiesmith.alevelquiz.quiz;

import java.util.Optional;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import net.alfiesmith.alevelquiz.ALevelQuiz;
import net.alfiesmith.alevelquiz.gui.GUIProvider;

/**
 * @author AvroVulcan on 07/05/2020
 */
public class QuizManager {

    private final Stage primaryStage;
    private final QuestionManager questionManager;
    private QuizUser quizUser;

    private int correct;
    private int incorrect;

    private Scene cachedScene;
    private GridPane cachedPane;
    private Label cachedLabel;
    private TextField cachedTextField;
    private Button cachedAnswerButton;

    public QuizManager(Stage primaryStage, String first, String second, String user) {
        this.primaryStage = primaryStage;
        this.quizUser = new QuizUser(first, second, user);
        this.quizUser.loadData();
        this.questionManager = new QuestionManager();
    }

    public void start() {
        questionManager.prepare();
        setScene();
    }

    private void setScene() {
        primaryStage.setScene(generateNextScene());
    }

    private Scene generateNextScene() {
        Optional<Question> question = questionManager.getNextQuestion();
        return question.map(this::getNextQuestionScene).orElseGet(this::getFinishedScene);
    }

    private Scene getNextQuestionScene(Question question) {
        if (cachedPane == null) {
            cachedPane = GUIProvider.getGridPane();
        }

        if (cachedScene == null) {
            cachedScene = GUIProvider.getDefaultScene(cachedPane);
        }

        if (cachedLabel == null) {
            cachedLabel = new Label();
            cachedLabel.setMaxWidth(ALevelQuiz.WINDOW_WIDTH - 20); // - 20 for some padding around edges
            cachedLabel.setWrapText(true);
            GridPane.setConstraints(cachedLabel,0,0);
            cachedPane.getChildren().add(cachedLabel);
        }

        cachedLabel.setText(question.getQuestion());

        if (cachedTextField == null) {
            cachedTextField = new TextField();
            cachedTextField.setMaxWidth(ALevelQuiz.WINDOW_WIDTH - 20);
            cachedTextField.setMinWidth(ALevelQuiz.WINDOW_WIDTH - 20);
            cachedTextField.setPromptText("Enter answer here");
            GridPane.setConstraints(cachedTextField,0,2);
            cachedPane.getChildren().add(cachedTextField);
        }

        if (cachedAnswerButton == null) {
            cachedAnswerButton = new Button("Submit");
            GridPane.setConstraints(cachedAnswerButton,0,3);
            cachedAnswerButton.setOnAction(event -> {
                if (!cachedTextField.getText().isEmpty()) {
                    if (question.isCorrect(cachedTextField.getText())) {
                        correct++;
                    } else {
                        incorrect++;
                    }
                    setScene();
                }
            });
            cachedPane.getChildren().add(cachedAnswerButton);
        }



        return cachedScene;
    }

    private Scene getFinishedScene() {
        if (cachedPane == null) {
            cachedPane = GUIProvider.getGridPane();
        }

        if (cachedScene == null) {
            cachedScene = GUIProvider.getDefaultScene(cachedPane);
        }

        quizUser.incrementCorrect(correct);
        quizUser.incrementIncorrect(incorrect);
        quizUser.saveData();

        double percent = ((correct * 1.0) / (incorrect + correct)) * 100; // multiplied by 1.0 to get into double context
        double overallPercent = ((quizUser.getOverallCorrect() * 1.0) / (quizUser.getOverallIncorrect() + quizUser.getOverallCorrect())) * 100;

        Label correctLabel = new Label("Correct: " + correct);
        Label incorrectLabel = new Label("Incorrect: " + incorrect);
        Label percentLabel = new Label("Percentage Correct: " + String.format("%.2f", percent) + "%"); // Rounding decimal to 2 places

        Label overallCorrectLabel = new Label("Correct (lifetime): " + quizUser.getOverallCorrect());
        Label overallIncorrectLabel = new Label("Incorrect (lifetime): " + quizUser.getOverallIncorrect());
        Label overallPercentageLabel = new Label("Percentage Correct (lifetime): " + String.format("%.2f", overallPercent) + "%");

        Button retake = new Button("Retake");
        retake.setOnAction(event -> {
            primaryStage.close();
            new ALevelQuiz().start(new Stage()); // restarting the program
        });

        GridPane.setConstraints(correctLabel,0,0);
        GridPane.setConstraints(incorrectLabel,0,1);
        GridPane.setConstraints(percentLabel,0,2);
        GridPane.setConstraints(overallCorrectLabel,0,3);
        GridPane.setConstraints(overallIncorrectLabel,0,4);
        GridPane.setConstraints(overallPercentageLabel,0,5);
        GridPane.setConstraints(retake,1,0);

        cachedPane.getChildren().clear();
        cachedPane.getChildren().addAll(correctLabel, incorrectLabel, percentLabel, overallCorrectLabel, overallIncorrectLabel, overallPercentageLabel, retake);

        return cachedScene;
    }

}
