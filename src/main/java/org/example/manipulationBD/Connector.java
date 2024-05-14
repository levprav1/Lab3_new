package org.example.manipulationBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connector {
    private String url;
    private String user;
    private String password;

    public Connector(){
        this.url = "jdbc:postgresql://dpg-cp0r8svjbltc73e2afs0-a.frankfurt-postgres.render.com:5432/reactors_os5p";
        this.user = "gusarov";
        this.password = "jC7ieOiq2WryZpx7sJtarBgEk2FuvBjc";
    }

    public Connector(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public Statement createStatement() throws SQLException {
        return getConnection().createStatement();
    }
}
