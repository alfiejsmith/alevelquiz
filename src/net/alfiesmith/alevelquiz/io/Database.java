package net.alfiesmith.alevelquiz.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.alfiesmith.alevelquiz.quiz.Question;

/**
 * @author AvroVulcan on 07/05/2020
 */
public class Database {

	// Most of these fields remain unused but are kept for further program
	// development
	private final String address;
	private final String port;
	private final String user;
	private final String pass;
	private final String database;
	private final String table;

	private final String url;

	private Connection connection;

	public Database(String address, String port, String user, String pass, String database, String table) {
		this.address = address;
		this.port = port;
		this.user = user;
		this.pass = pass;
		this.database = database;
		this.table = table;

		this.url = "jdbc:mysql://" + address + ":" + port + "/" + database + "?serverTimezone=UTC"; // SQL likes to
																									// complain about
																									// timezones
																									// (especially BST)
	}

	public void openConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(url, user, pass);
	}

	public void createTableIfNotExists() throws SQLException {
		String create = "create table if not exists " + table
				+ " (id int not null auto_increment, question varchar(150) not null, answer varchar(150) not null, primary key (id));";
		PreparedStatement statement = connection.prepareStatement(create);
		statement.execute();

		ResultSet questionData = getQuestionData();
		if (!questionData.next()) { // It is empty
			populateDefaultQuestions();
		}

	}

	public ResultSet getQuestionData() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT * from " + table + ";");
		return statement.executeQuery();
	}

	private void populateDefaultQuestions() throws SQLException {
		String base = "insert into " + table + "(question, answer) values (?, ?);";

		// No nice way to do this. Oh well...
		List<Question> questions = new ArrayList<>();
		questions.add(Question.newQuestion("How many bytes in a nibble", "0.5"));
		questions.add(Question.newQuestion("How many nibbles in a byte", "2"));
		questions.add(Question.newQuestion("1100 in denary", "12"));
		questions.add(Question.newQuestion("12 in hex", "C"));
		questions.add(Question.newQuestion("What is 0xB9 in denary", "185"));
		questions.add(Question.newQuestion("How many kilobytes in a megabyte", "1000"));
		questions.add(Question.newQuestion("How many bytes in a kilobyte", "1000"));
		questions.add(Question.newQuestion("How many bytes in a megabyte", "1000000"));
		questions.add(Question.newQuestion("What is bigger 1001 TB or 1 PB", "1001 TB"));
		questions.add(Question.newQuestion("What is bigger 14240 KB or 14.2 MB", "14240 KB"));
		questions.add(Question.newQuestion("What hex character represents 15", "F"));
		questions.add(Question.newQuestion("What does A (hex) represent in denary ", "10"));
		questions.add(Question.newQuestion("What base is hex", "16"));
		questions.add(Question.newQuestion("What base is denary", "10"));
		questions.add(Question.newQuestion("What base is binary", "2"));
		questions.add(Question.newQuestion("Why do we use hex", "Simpler to read"));
		questions.add(Question.newQuestion("What base do computers read", "2"));
		questions.add(Question.newQuestion("Max denary value of an unsigned byte?", "255"));
		questions.add(Question.newQuestion("Max denary value of signed byte", "127"));
		questions.add(Question.newQuestion("What is the name of the bit that determines sign in a signed byte", "MSB"));

		for (Question question : questions) {
			PreparedStatement statement = connection.prepareStatement(base);
			statement.setString(1, question.getQuestion());
			statement.setString(2, question.getValidAnswer());
			statement.execute();
		}
	}

}
