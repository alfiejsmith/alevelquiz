package net.alfiesmith.alevelquiz.quiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import net.alfiesmith.alevelquiz.ALevelQuiz;
import net.alfiesmith.alevelquiz.io.Database;

public class QuestionManager {

    private LinkedList<Question> questions; // List used to maintain ordering

    public QuestionManager() {
        this.questions = new LinkedList<>();
        loadQuestions();
    }

    public void addQuestion(String question, Set<String> answers) throws IllegalArgumentException{
        if (question.length() > 150) {
            throw new IllegalArgumentException("Question " + question + " is too long! Must be <= 150 characters");
        }
        questions.add(Question.newQuestion(question, answers));
    }

    public void prepare() {
        Collections.shuffle(questions);
    }

    public Optional<Question> getNextQuestion() {
        return Optional.ofNullable(questions.poll());
    }

    /**
     * Connects to MySQL to load questions
     */
    private void loadQuestions() {
        Database database = new Database(ALevelQuiz.SQL_ADDRESS, ALevelQuiz.SQL_PORT, ALevelQuiz.SQL_USER, ALevelQuiz.SQL_PASS, ALevelQuiz.SQL_DATABASE_NAME, ALevelQuiz.SQL_TABLE_NAME);
        Map<String, Set<String>> rawQuestions = new HashMap<>();

        try {
            // Opening the connection and creating the table
            database.openConnection();
            database.createTableIfNotExists();

            // Querying for the data
            ResultSet questionData = database.getQuestionData();

            // While there is still data to loop through add it to raw questions
            while (questionData.next()) {
                String question = questionData.getString("question");
                String answer = questionData.getString("answer");

                // Adding it to the set of answers or creating the set if it doesn't pre exist, allowing for multiple answers per question
                if (rawQuestions.containsKey(question)) {
                    rawQuestions.get(question).add(answer);
                } else {
                    Set<String> answers = new HashSet<>();
                    answers.add(answer);
                    rawQuestions.put(question, answers);
                }
            }

            for (Map.Entry<String, Set<String>> entry : rawQuestions.entrySet()) {
                addQuestion(entry.getKey(), entry.getValue());
            }
        } catch (SQLException | ClassNotFoundException | IllegalArgumentException err) {
            err.printStackTrace();
        }
    }
}
