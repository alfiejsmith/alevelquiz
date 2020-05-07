package net.alfiesmith.alevelquiz.quiz;

import java.util.*;
import java.util.stream.Collectors;

public class Question {

    private String question;
    private Set<String> answers; // Using a set over a list as it will be O(1) as opposed to O(n) with #contains

    private Question(String question, Set<String> answers) {
        this.question = question;
        this.answers = answers;
    }

    // Getter method for question
    public String getQuestion() {
        return question;
    }

    public boolean isCorrect(String answer) {
        return answers.contains(answer.toLowerCase());
    }
    
    // Used for populating the database table - serves no other purpose
    public String getValidAnswer() {
    	return (String) answers.toArray()[0];
    }

    // Factory method (used mainly for it looking nicer in the code)
    public static Question newQuestion(String question, String... answers) {
        Set<String> lowercaseAnswers = new HashSet<>(answers.length);
        for (String answer : answers) {
            lowercaseAnswers.add(answer.toLowerCase());
        }
        return new Question(question, lowercaseAnswers);
    }
    
    public static Question newQuestion(String question, Set<String> answers) {
        answers = answers.stream().map(String::toLowerCase).collect(Collectors.toSet()); // Making them all lower case so answering is case insensitive
        return new Question(question, answers);
    }
    
 
    // Important to override hashCode for the performance of the HashMap in QuestionManager
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question1 = (Question) o;
        return Objects.equals(question, question1.question) && Objects.equals(answers, question1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, answers);
    }
}
