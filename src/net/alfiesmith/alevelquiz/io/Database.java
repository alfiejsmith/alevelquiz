package net.alfiesmith.alevelquiz.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author AvroVulcan on 07/05/2020
 */
public class Database {

	// Most of these fields remain unused but are kept for further program development
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

        this.url = "jdbc:mysql://" + address + ":" + port + "/" + database + "?serverTimezone=UTC"; // SQL likes to complain about timezones (especially BST)
    }

    public void openConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, pass);
    }

    public void createTableIfNotExists() throws SQLException {
        String create = "create table if not exists " + table + " (id int not null, question varchar(150) not null, answer varchar(150) not null, primary key (id));";
        PreparedStatement statement = connection.prepareStatement(create);
        statement.execute();
    }

    public ResultSet getQuestionData() throws SQLException{
        PreparedStatement statement = connection.prepareStatement("SELECT * from " + table);
        return statement.executeQuery();
    }

}
