package br.edu.ifpb.confactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConFactoryMySQL {

    private final static String USER = "mysql";
    private final static String PASSWORD = "mysql";

    public static Connection getConnectionMySql() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/questao04", USER, PASSWORD);
    }
}
