package com.softserveinc.ita.kaiji.database;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.*;


public class DataBaseConnectionTest {

    @Test
    public void connectionSetUpTest() {

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "toor");
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql:///game", connectionProps);

        } catch (SQLException e){}
        assertNotNull(conn);
    }

}
