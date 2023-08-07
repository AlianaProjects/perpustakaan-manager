package perpustakaanManager;

import java.sql.*;
public class dbConnect {
    private static dbConnect dbhandler;
    private Connection connection;
    private Statement statement;
    private ResultSet result;

    void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/perpustakaan", "root", "");

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static dbConnect getInstance() {
        if (dbhandler == null) {
            dbhandler = new dbConnect();
        }
        return dbhandler;
    }

    private dbConnect() {
        createConnection();
    }

    public ResultSet executeQuery(String query) {
        try {
            statement = connection.createStatement();
            result = statement.executeQuery(query);

        } catch (SQLException err) {
            System.out.println(err.getLocalizedMessage());
            return null;
        }

        return result;
    }

    public boolean executeAction(String query) {
        try {
            statement = connection.createStatement();
            statement.execute(query);
            return true;

        } catch (SQLException err) {
            System.out.println(err.getLocalizedMessage());
            return false;
        }
    }
}

