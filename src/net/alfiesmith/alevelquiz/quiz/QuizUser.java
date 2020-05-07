package net.alfiesmith.alevelquiz.quiz;

import net.alfiesmith.alevelquiz.ALevelQuiz;
import net.alfiesmith.alevelquiz.io.CSVWriter;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @author AvroVulcan on 07/05/2020
 */
public class QuizUser {

    private String firstName;
    private String lastName;
    private String username;

    private int overallCorrect;
    private int overallIncorrect;

    public QuizUser(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public void loadData() {
        CSVWriter writer = new CSVWriter(new File(ALevelQuiz.CSV_PATH));
        List<String[]> data = writer.read();

        for (String[] line : data) {
            if (line[0].equals(username)) {
                try {
                    this.overallCorrect = Integer.parseInt(line[1]);
                    this.overallIncorrect = Integer.parseInt(line[2]);
                    break;
                } catch (NumberFormatException err) {
                    System.out.println("Invalid CSV line starting with key " + line[0]);
                }
            }
        }
    }

    public void saveData() {
        CSVWriter writer = new CSVWriter(new File(ALevelQuiz.CSV_PATH));
        List<String[]> data = writer.read();

        boolean changed = false;
        for (String[] line : data) {
            if (line[0].equals(username)) {
                try {
                    line[1] = String.valueOf(overallCorrect);
                    line[2] = String.valueOf(overallIncorrect);
                    changed = true;
                } catch (IndexOutOfBoundsException err) {
                    System.out.println("Invalid CSV line starting with key " + line[0]);
                }
            }
        }

        if (!changed) {
            data.add(new String[]{ username, String.valueOf(overallCorrect), String.valueOf(overallIncorrect) });
        }

        writer.writeAll(data);
    }

    /*
     * Getters / Setters / hashcode & equals
     */

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public int getOverallCorrect() {
        return overallCorrect;
    }

    public void incrementCorrect(int overallCorrect) {
        this.overallCorrect += overallCorrect;
    }

    public int getOverallIncorrect() {
        return overallIncorrect;
    }

    public void incrementIncorrect(int overallIncorrect) {
        this.overallIncorrect += overallIncorrect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QuizUser quizUser = (QuizUser) o;
        return overallCorrect == quizUser.overallCorrect && overallIncorrect == quizUser.overallIncorrect && Objects.equals(firstName, quizUser.firstName) && Objects.equals(lastName, quizUser.lastName) && Objects.equals(username, quizUser.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, username, overallCorrect, overallIncorrect);
    }
}
