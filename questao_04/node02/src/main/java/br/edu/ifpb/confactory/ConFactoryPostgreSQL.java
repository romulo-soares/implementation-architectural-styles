package br.edu.ifpb.confactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConFactoryPostgreSQL {

    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";

    public static Connection getConnectionPostgres() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/questao04", USER, PASSWORD);
    }

}
